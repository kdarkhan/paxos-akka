package com.kdarkhan.paxakka.actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.Cluster

import collection.JavaConverters._
import com.kdarkhan.paxakka.Messages._
import com.kdarkhan.paxakka.Role

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by monstar on 12/2/16.
  */
class Acceptor extends Actor with ActorLogging {
  val cluster = Cluster(context.system)
  var highestResponded: Option[Int] = None

  private val accepted = collection.mutable.HashMap.empty[Int, Int]


  override def receive: Receive = active
  scheduleSleep()

  def active: Receive = {
    case MPrepare(id) =>
      log.debug(s"Acceptor received Prepare with id ${id}")
      if (!highestResponded.exists(_ >= id)) {
        highestResponded = Some(id)
        if (accepted.nonEmpty) {
          val maxKey = accepted.keysIterator.max
          sender ! MPromise(id, Some(maxKey), Some(accepted(maxKey)))
          log.debug(s"Acceptor responded with highest accepted $maxKey")
        } else {
          sender ! MPromise(id, None, None)
        }
      } else {
        log.debug(s"Acceptor ignored the message")
      }

    case MAccept(id, value) =>
      log.debug(s"Acceptor received MAccept($id, $value)")
      if (!highestResponded.exists(_ > id)) {
        accepted.put(id, value)

        ActorUtils.getClusterAddress(cluster.state.getMembers.asScala, Role.Learner) foreach { address =>
          log.debug(s"Acceptor notifying learner about $id")
          context.actorSelection(s"$address/user/*").tell(MLearn(id, value), self)
        }
      }

    case MSleep =>
      log.error(s"Process ${self.path} going to sleep")
      context.become(inactive)
      scheduleWakeup()

    case x => log.error(s"Unhandled message: $x")

  }

  def inactive: Receive = {
    case MWakeup =>
      log.debug("Learner is waking up")
      context.become(active)
      scheduleSleep()
    case x => log.debug("Acceptor is sleeping and ignoring messages")
  }

  private def scheduleSleep(from: Int = 3000, to: Int = 13000): Unit = {
    context.system.scheduler.scheduleOnce(ActorUtils.getRandomInt(from, to).milliseconds, self, MSleep)
  }
  private def scheduleWakeup(from: Int = 3000, to: Int = 5000): Unit = {
    context.system.scheduler.scheduleOnce(ActorUtils.getRandomInt(from, to).milliseconds, self, MWakeup)
  }
}

object Acceptor {
  def props: Props = Props[Acceptor]
}