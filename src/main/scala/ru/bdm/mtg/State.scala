package ru.bdm.mtg

import ru.bdm.mtg.lands.Permanent


case class State(
                  manaPool: ManaPool.Type = AllSet.empty,
                  hand: AllSet.card = AllSet.empty,
                  graveyard: AllSet.card = AllSet.empty,
                  battlefield: AllSet.permanent = AllSet.empty,
                  lands: AllSet.permanent = AllSet.empty,
                  library: Seq[Card] = Seq.empty[Card],
                  topOfLibrary: Seq[Card] = Seq.empty[Card],
                  phase: Phase.Phase = Phase.play,
                  draw: Int = 0,
                  discard: Int = 0,
                  numberTurn: Int = 1,
                  endTurnDiscards: Int = 0,
                  playedLand: Boolean = false,
                  shuffle: Boolean = false
                ) {
  override def toString: String =
    s"   mana{${manaPool.mkString(", ")}}" +
      s"   turn=$numberTurn" +
      s"   hand{${hand.mkString(", ")}}" +
      s"   lands{${lands.mkString(", ")}}" +
      s"   takeCards=$draw" +
      s"   discard=$discard" +
      s"   playedLand=$playedLand" +
      s"   shuffle=$shuffle" +
      s"   graveyard{${graveyard.mkString(", ")}}" +
      s"   battlefield{${battlefield.mkString(", ")}}" +
      s"   верхКолоды{${topOfLibrary.mkString(", ")}}" +
      s"   library{${library.size}}"


  def getChanges(next: State): State = {
    State(
      changes(manaPool, next.manaPool),
      changes(hand, next.hand),
      changes(graveyard, next.graveyard),
      changes(battlefield, next.battlefield),
      changes(lands, next.lands),
      next.library,
      next.topOfLibrary,
      next.phase,
      next.draw,
      next.discard,
      next.numberTurn,
      next.endTurnDiscards,
      next.playedLand,
      next.shuffle
    )
  }

  def changes[T](first: AllSet.Type[T], second: AllSet.Type[T]): Map[T, Int] = {
    var result = AllSet.empty[T]
    for ((elem, num) <- first) {
      if (second.contains(elem)) {
        if (num - second(elem) != 0)
          result += elem -> -(num - second(elem))
      } else
        result += elem -> -num
    }
    for ((elem, num) <- second) {
      if (!first.contains(elem))
        result += elem -> num
    }
    result
  }
}




