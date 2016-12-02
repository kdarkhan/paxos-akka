package com.kdarkhan.paxakka

object Role extends Enumeration {
  type Role = Value
  val Client, Acceptor, Proposer, Learner, Leader = Value
}
