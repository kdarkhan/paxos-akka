package com.kdarkhan

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.MemberUp
import com.kdarkhan.RoleEnum._

/**
  * Created by monstar on 12/1/16.
  */
class Processor(role: RoleEnum.RoleEnum) extends Actor with ActorLogging {
  val cluster = Cluster(context.system)

//  cluster.state

  // make sure we have at least one role
//  assert(roles.nonEmpty)
//  val handler: Role = Role.getRoleHandler(role)
  def receive: Receive = {
    case MemberUp(member) => log.debug(s"Got member $member")
  }
}

object Processor {
  def props(role: RoleEnum) = Props(classOf[Processor], role)
}
