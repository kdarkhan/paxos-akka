package com.kdarkhan.actors

import akka.actor.ActorRef
import akka.cluster.Member
import com.kdarkhan.RoleEnum.RoleEnum

/**
  * Created by monstar on 12/2/16.
  */
object ActorUtils {
  def getByRole(roleEnum: RoleEnum): ActorRef = {
    ???
  }

  def countRoleMembers(members: Iterable[Member], role: RoleEnum): Int = {
    members.count(_.roles.contains(role.toString))
  }

  def getClusterAddress(members: Iterable[Member], role: RoleEnum): Option[String] = {
    members.find(_.roles.contains(role.toString)) map (_.address.toString)
  }
}
