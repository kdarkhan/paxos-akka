package com.kdarkhan.actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.Cluster
import com.kdarkhan.Messages._
import com.kdarkhan.RoleEnum
import collection.JavaConverters._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


/**
  * Created by monstar on 12/2/16.
  */
class Proposer extends Actor with ActorLogging {
  val cluster = Cluster(context.system)
  var highestProposal = 0

  var fromAcceptors: List[MPromise] = Nil

  context.system.scheduler.schedule(0.seconds, 15.seconds, self, MTick)
  override def receive: Receive = {
    case MTick =>
      highestProposal += 1
      fromAcceptors = Nil
      ActorUtils.getClusterAddress(cluster.state.getMembers.asScala, RoleEnum.Acceptor) foreach { address =>
        log.debug(s"Proposer sending new proposal $highestProposal")
        context.actorSelection(s"$address/user/*").tell(MPrepare(highestProposal), self)
      }

    case mp @ MPromise(id, _, _) =>
      log.debug(s"Actor received MPromise for proposal $id")

      if (highestProposal == id) {
        fromAcceptors = mp :: fromAcceptors
        if (acceptorQuorum(fromAcceptors.size)) {
          val withValues = fromAcceptors filter (_.acceptedId.nonEmpty)
          if (withValues.nonEmpty) {
            val maxValue = withValues.maxBy(_.acceptedId)
            ActorUtils.getClusterAddress(cluster.state.getMembers.asScala, RoleEnum.Acceptor) foreach { address =>
              context.actorSelection(s"$address/user/*").tell(MAccept(highestProposal, maxValue.acceptedValue.get), self)
            }
          } else {
            ActorUtils.getClusterAddress(cluster.state.getMembers.asScala, RoleEnum.Acceptor) foreach { address =>
              context.actorSelection(s"$address/user/*").tell(MAccept(highestProposal, scala.util.Random.nextInt), self)
            }
          }
        }
      }


    case other =>
      log.error(s"========================================== Unhandled message: $other")

  }

  private def acceptorQuorum(count: Int): Boolean = {
    ActorUtils.countRoleMembers(cluster.state.getMembers.asScala, RoleEnum.Acceptor) / 2 + 1 >= count
  }
}

object Proposer {
  def props: Props = Props[Proposer]
}
