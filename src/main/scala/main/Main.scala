package main

import akka.actor.{ActorSystem, Props}
import useCase.JankenGame
import useCase.JankenGame._

object Main extends App {
  val system = ActorSystem("JankenGame")
  val game = system.actorOf(Props[JankenGame], "game")

  game ! EntryPlayer("太郎")
  game ! EntryPlayer("二郎")
  game ! EntryPlayer("三郎")
  game ! EntryPlayer("四郎")
  game ! Showdown()
}
