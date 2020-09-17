package ru.bdm.mtg.cards


import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.{Card, State}

trait Discarded extends Card{
  def numberDiscard:Int
  def states(current:State, cards:Seq[Card]): Seq[State]

  override def nextStates(current:State): Seq[State] = {
    current.hand getCombinations numberDiscard flatMap { map =>
      val cards = map.getSeq
      val st = states(current.copy(hand = current.hand --~ cards), cards)
      for {
        state <- st
        card <- cards
      } yield card.discard(state)
    }
  }
}
