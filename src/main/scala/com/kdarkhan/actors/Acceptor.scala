package com.kdarkhan.actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.Cluster
import com.kdarkhan.Messages._

/**
  * Created by monstar on 12/2/16.
  */
class Acceptor extends Actor with ActorLogging {
  val cluster = Cluster(context.system)
  var highestResponded: Option[Int] = None

  private val accepted = collection.mutable.HashMap.empty[Int, Int]


  override def receive: Receive = {
    case MPrepare(id) =>
      log.debug(s"Acceptor received Prepare with id ${id}")
      if (!highestResponded.exists(_ >= id)) {
        highestResponded = Some(id)
        if (accepted.nonEmpty) {
          val maxKey = accepted.keysIterator.max
          sender ! MPromise(id, Some(maxKey), Some(accepted(maxKey)))
          log.debug(s"Acceptor responded with highest accepted $maxKey")
        } else {
          sender ! MPromise(id, None, None)
        }
      } else {
        log.debug(s"Acceptor ignored the message")
      }

    case MAccept(id, value) =>
      log.debug(s"Acceptor received MAccept($id, $value)")
      if (!highestResponded.exists(_ > id)) {
        accepted.put(id, value)
      }

    case x => log.error(s"Unhandled message: $x")
  }
}

object Acceptor {
  def props: Props = Props[Acceptor]
}