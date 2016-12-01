package com.kdarkhan

/**
  * Created by monstar on 12/1/16.
  */

object Messages {
  case class MPrepare(id: Int)
  case class MPromise(id: Int, acceptedId: Option[Int], acceptedValue: Option[Int])
  case class MAccept(id: Int, value: Int)
  case class MAccepted(id: Int, value: Any)
  case class MLearn(id: Int, value: Any)
  object MSleep
  object MWakeup
  object MTick
}
