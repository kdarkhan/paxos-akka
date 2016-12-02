package com.kdarkhan.paxakka

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import com.kdarkhan.paxakka.actors.{Acceptor, Learner, Proposer}

/**
  * Created by monstar on 12/1/16.
  */
object ApplicationMain extends App {
  val proposers = startup(2551, Role.Proposer, 1, Proposer.props, "proposers")
  val acceptors = startup(2552, Role.Acceptor, 5, Acceptor.props, "acceptors")
  val learners = startup(2553, Role.Learner, 1, Learner.props, "learners")

//  Await.result(proposers.whenTerminated, Duration.Inf)
//  Await.result

  def startup(port: Int,
              role: Role.Role,
              nodeCount: Int,
              props: Props,
              path: String): ActorSystem = {
    val config = ConfigFactory
      .parseString(s"""
           |akka.remote.netty.tcp.port=$port
           |akka.cluster.roles=[$role]""".stripMargin)
      .withFallback(ConfigFactory.load())

    val system: ActorSystem = ActorSystem("ClusterSystem", config)
    1 to nodeCount foreach { index => system.actorOf(props, path + index) }
    system
  }
}
