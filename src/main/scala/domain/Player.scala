package domain

import akka.actor.{Actor, ActorRef}
import domain.Janken.HandSign

import scala.util.Random

class Player(name: String) extends Actor {
  import Player._
  import Janken._

  override def receive: Receive = {
    case OpenHand =>
      val random_index = Random.nextInt(allOfHandSigns.length)
      val handSign = allOfHandSigns(random_index)
      println(s"$name : ${handSign.toString}")
      sender() ! PlayerHand(self, handSign)

    case BecomeWinner =>
      println("優勝者は...")
      println(s"$name そう、私です！")

    case WhoAreYou =>
      println(s"$name です")
  }
}

object Player {
  case object OpenHand
  case class PlayerHand(ref: ActorRef, hand: HandSign)
  case object BecomeWinner
  case object WhoAreYou
}
