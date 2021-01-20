package domain
import domain.Janken._

import org.scalatest.funspec.AnyFunSpec

class JankenSpec extends AnyFunSpec{
  describe("勝敗のチェック") {
    it("グーチョキパーがある場合はあいこ") {
      val hands = List(Rock, Paper, Scissors).toSet[HandSign]
      assert(resultState(hands) == Aiko)
    }

    it("同じ手の場合はあいこ") {
      val hands = List(Rock, Rock).toSet[HandSign]
      assert(resultState(hands) == Aiko)
    }

    it("チョキとグーではグーが勝つ") {
      val hands = List(Rock, Scissors, Rock, Scissors).toSet[HandSign]
      assert(resultState(hands) == Wins(Rock))
    }

    it("グーとパーではパーが勝つ") {
      val hands = List(Rock, Paper, Rock).toSet[HandSign]
      assert(resultState(hands) == Wins(Paper))
    }

    it("チョキとパーではチョキが勝つ") {
      val hands = List(Scissors, Paper).toSet[HandSign]
      assert(resultState(hands) == Wins(Scissors))
    }
  }
}
