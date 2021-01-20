package useCase

import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.duration._
import domain.Janken._
import domain.Player
import domain.Player._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


class JankenGame extends Actor{
  import JankenGame._

  implicit val timeout: Timeout = 1.second
  implicit val executionContext: ExecutionContext = context.dispatcher

  override def receive: Receive = field(Seq())

  def field(players: Seq[ActorRef]): Receive = {
    case EntryPlayer(player_name) =>
      val newPlayer = context.actorOf(Props(classOf[Player], player_name), s"player_${players.length}")
      val addedPlayer  = players :+ newPlayer
      context.become(field(addedPlayer))

    case Showdown(stage_count, stage) =>
      players.length match {
        case 1 => players(0) ! BecomeWinner
        case _ =>
          println("")
          if (stage == Aiko) println("あいこでしょ！ ")
          else println(s"第${stage_count}回戦！！")

          val playerHands = players.map { p =>
            (p ? OpenHand).mapTo[PlayerHand]
          }
          val waitPlayerHands = Future.sequence(playerHands)
          waitPlayerHands.onComplete {
            case Success(hands) =>
              self ! ResultAggregate(hands, stage_count)
            case Failure(_) => println("何かがあって失敗しました！")
          }
      }

      case ResultAggregate(playerHands, stage_count) =>

        val resultHands = playerHands.map { h =>
          h.hand
        }

        val resultStatus = resultState(resultHands.toSet[HandSign])
         resultStatus match {
          case Aiko =>
            self ! Showdown(stage_count, resultStatus)

          case Wins(handSign) =>
            // 負けた人の処理
            val loosePlayers = playerHands.filter {p =>
              p.hand != handSign
            }
            val loosePlayerRefs = loosePlayers.map { l => l.ref }
            loosePlayerRefs.foreach { looser => looser ! PoisonPill }

            // 勝った人の処理
            val winPlayers = playerHands.filter { p =>
              p.hand == handSign
            }
            println("")
            val winPlayerRefs = winPlayers.map { w => w.ref }
            if (winPlayerRefs.length == 1) winPlayerRefs(0) ! BecomeWinner
            else {
              println(s"第${stage_count}回戦の勝者は...")
              winPlayerRefs.foreach {w => w ! WhoAreYou}
              context.become(field(winPlayerRefs))
              self ! Showdown(stage_count + 1, resultStatus)
            }
        }
  }
}

object JankenGame {
  case class EntryPlayer(player_name: String)
  case class Showdown(stage_count: Int = 1, stage: ResultState = Wins(Rock))
  case class ResultAggregate(playerHands: Seq[PlayerHand], stage_count: Int)
}
