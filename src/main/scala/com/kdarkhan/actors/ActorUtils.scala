package com.kdarkhan.actors

import akka.actor.ActorRef
import akka.cluster.Member
import com.kdarkhan.Role.Role

/**
  * Created by monstar on 12/2/16.
  */
object ActorUtils {
  def getByRole(roleEnum: Role): ActorRef = {
    ???
  }

  def countRoleMembers(members: Iterable[Member], role: Role): Int = {
    members.count(_.roles.contains(role.toString))
  }

  def getClusterAddress(members: Iterable[Member], role: Role): Option[String] = {
    members.find(_.roles.contains(role.toString)) map (_.address.toString)
  }
}
