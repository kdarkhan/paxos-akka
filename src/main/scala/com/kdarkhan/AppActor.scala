package com.kdarkhan

import akka.actor.{Actor, ActorLogging, Props}
import com.kdarkhan.Messages._
/**
  * Created by monstar on 12/1/16.
  */
class AppActor extends Actor with ActorLogging {
  def receive: Receive = {
    case MStart(roles) =>
      log.debug(s"App received MStart $roles")
  }
}


object AppActor {
  val props: Props = Props(classOf[AppActor])
}