package com.kdarkhan.actors

import akka.cluster.Member
import com.kdarkhan.Role.Role

/**
  * Created by monstar on 12/2/16.
  */
object ActorUtils {

  def countRoleMembers(members: Iterable[Member], role: Role): Int = {
    members.count(_.roles.contains(role.toString))
  }

  def getClusterAddress(members: Iterable[Member], role: Role): Option[String] = {
    members.find(_.roles.contains(role.toString)) map (_.address.toString)
  }

  private val r = new scala.util.Random()

  r.setSeed(System.currentTimeMillis())

  def getRandomInt(from: Int, to: Int): Int = {
    r.nextInt(to - from) + from
  }
}
