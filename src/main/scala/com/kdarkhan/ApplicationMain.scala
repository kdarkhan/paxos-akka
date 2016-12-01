package com.kdarkhan

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import com.kdarkhan.actors._

/**
  * Created by monstar on 12/1/16.
  */
object ApplicationMain extends App {
  val proposers = startup(2551, RoleEnum.Proposer, 1, Proposer.props, "proposers")
  val acceptors = startup(2552, RoleEnum.Acceptor, 5, Acceptor.props, "acceptors")

//  Await.result(proposers.whenTerminated, Duration.Inf)
//  Await.result

  def startup(port: Int,
              role: RoleEnum.RoleEnum,
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
