package com.kdarkhan.actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.Cluster
import com.kdarkhan.Messages.MLearn
import com.kdarkhan.Role
import collection.JavaConverters._

/**
  * Created by monstar on 12/2/16.
  */
class Learner extends Actor with ActorLogging {
  val cluster = Cluster(context.system)
  override def receive: Receive = {
    case MLearn(id, value) =>
      log.debug(s"Learner learned about $id $value")
  }

  private def acceptorQuorum(count: Int): Boolean = {
    ActorUtils.countRoleMembers(cluster.state.getMembers.asScala, Role.Acceptor) / 2 + 1 >= count
  }
}

object Learner {
  def props: Props = Props[Learner]
}
