package com.kdarkhan

import akka.actor.Actor.Receive

/**
  * Created by monstar on 12/1/16.
  */
/*sealed trait Role {
  def receive: Receive
}

class Client extends Role {
  override def receive: Receive = ???
}

class Acceptor extends Role {
  override def receive: Receive = ???
}

class Proposer extends Role {
  override def receive: Receive = ???
}

class Learner extends Role {
  override def receive: Receive = ???
}

class Leader extends Proposer {
  override def receive: Receive = ???
}*/

object RoleEnum extends Enumeration {
  type RoleEnum = Value
  val Client, Acceptor, Proposer, Learner, Leader = Value
}

/*object Role {
  def getRoleHandler(role: RoleEnum.RoleEnum): Role = role match {
    case RoleEnum.Client => new Client()
    case RoleEnum.Acceptor => new Acceptor()
    case RoleEnum.Proposer => new Proposer()
    case RoleEnum.Learner => new Learner()
    case RoleEnum.Leader => new Leader()
  }
}*/
