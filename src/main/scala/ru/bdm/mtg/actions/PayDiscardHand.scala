package ru.bdm.mtg.actions

import ru.bdm.mtg.{Card, State}

case class PayDiscardHand(card: Card) extends Action {
  override def act(state: State): Seq[State] = {
    val cards = state.hand.values.sum
    TakeCards(cards - 1) * AddDiscard(cards) act (state)
  }

}
