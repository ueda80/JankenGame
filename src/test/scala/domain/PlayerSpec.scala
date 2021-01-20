package domain

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

import domain.Janken._
import domain.Player._

class PlayerSpec extends TestKit(ActorSystem("PlayerSpec"))
  with ImplicitSender
  with AnyWordSpecLike
  with BeforeAndAfterAll {

  override def afterAll() = {
    TestKit.shutdownActorSystem(system)
  }

  "プレイヤーが出す手のチェック" should {
    "グーかチョキかパーが返る" in {
      val player = system.actorOf(Props(classOf[Player], "太郎"), "player")
      player ! OpenHand
      expectMsgAnyOf(Rock, Scissors, Paper)
    }
  }
}
