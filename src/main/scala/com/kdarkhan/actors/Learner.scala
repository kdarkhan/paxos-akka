package com.kdarkhan.actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.Cluster

/**
  * Created by monstar on 12/2/16.
  */
class Learner extends Actor with ActorLogging {
  val cluster = Cluster(context.system)
  override def receive: Receive = ???
}

object Learner {
  def props: Props = Props[Learner]
}
