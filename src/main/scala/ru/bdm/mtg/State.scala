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
                  shuffle: Boolean = false,
                  score: Double = .0
                ) extends Serializable {
  def string: String =
    s" mana{${manaPool.mkString(", ")}}" +
      s"   turn=$numberTurn" +
      s"   phase=$phase" +
      s"   hand{${hand.mkString(", ")}}" +
      s"   lands{${lands.mkString(", ")}}" +
      s"   takeCards=$draw" +
      s"   discard=$discard" +
      s"   playedLand=$playedLand" +
      s"   shuffle=$shuffle" +
      s"   graveyard{${graveyard.mkString(", ")}}" +
      s"   battlefield{${battlefield.mkString(", ")}}" +
      s"   topDeck{${topOfLibrary.mkString(", ")}}" +
      s"   score{${score}}" +
      s"   library{${library.size}}"


  def getChanges(next: State): Diff = {
    Diff(
      changes(hand, next.hand) ++
      changes(graveyard, next.graveyard) ++
      changes(battlefield, next.battlefield) ++
      changes(lands, next.lands),
      next.numberTurn,
      next.manaPool
    )
  }

  def changes[T](first: Map[T, Int], second: Map[T, Int]): Seq[Card] = {
    var result = Seq.empty[Card]
    for ((elem, num) <- first) {
      if (second.contains(elem)) {
        if (num - second(elem) != 0)
          result :+= elem.asInstanceOf[Card]
      } else
        result :+= elem.asInstanceOf[Card]
    }
    for ((elem, num) <- second) {
      if (!first.contains(elem))
        result :+= elem.asInstanceOf[Card]
    }
    result
  }
}

case class Diff(seq: Seq[Card], turn: Int, mana: ManaPool.Type)



