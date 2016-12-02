package com.kdarkhan.actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.Cluster
import com.kdarkhan.Messages.MLearn
import com.kdarkhan.Role

import collection.JavaConverters._
import scala.collection.mutable

/**
  * Created by monstar on 12/2/16.
  */
class Learner extends Actor with ActorLogging {
  val cluster = Cluster(context.system)
  val values: mutable.HashMap[Int, (Boolean, List[Int])] = collection.mutable.HashMap.empty[Int, (Boolean, List[Int])]

  override def receive: Receive = {
    case MLearn(id, value) =>
      log.debug(s"Learner received MLearn about $id $value")
      if (values.contains(id)) {
        val (wasQuorum, lst) = values(id)
        if (!wasQuorum) {
          if (acceptorQuorum(values(id)._2.count(_ == value) + 1)) {
            values.put(id, (true, value :: lst))
            log.debug(s"Learner reached quorum about MLearn($id $value")
          } else {
            values.put(id, (false, value :: lst))
          }
        } else {
          values.put(id, (true, value :: lst))
        }
      } else {
        values.put(id, (false, List(value)))
      }
  }

  private def acceptorQuorum(count: Int): Boolean = {
    ActorUtils.countRoleMembers(cluster.state.getMembers.asScala,
                                Role.Acceptor) / 2 + 1 <= count
  }
}

object Learner {
  def props: Props = Props[Learner]
}
