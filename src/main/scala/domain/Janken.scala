package domain

import scala.collection.immutable.Set

final object Janken {

  sealed trait HandSign

  object Rock extends HandSign {
    override def toString = "グー"
  }

  object Paper extends HandSign {
    override def toString = "パー"
  }

  object Scissors extends HandSign {
    override def toString = "チョキ"
  }

  val allOfHandSigns = List(Rock, Paper, Scissors)

  sealed trait ResultState
  object Aiko extends ResultState
  case class Wins(handSign: HandSign) extends ResultState
  def resultState(hands: Set[HandSign]): ResultState = {
    if (isAiko(hands)) Aiko
    else Wins(witchIsWin(hands))
  }

  private def isAiko(x: Set[HandSign]): Boolean = {
    val handSignCount = x.count(z => true)

    if (handSignCount != 2) true
    else false
  }

  // どの手が勝つか？
  private def witchIsWin(x: Set[HandSign]): HandSign = {
    // もし、Set[HandSign]の要素が、1か3であれば、”あいこ”である
    // そのため、Set[HandSign]の要素が2の場合のみ考える
    if (x.count(z=>true) != 2) throw new Exception("あいこになります")

    if (x subsetOf Set(Rock, Paper)) Paper
    else if (x subsetOf Set(Paper, Scissors)) Scissors
    else Rock
  }
}
