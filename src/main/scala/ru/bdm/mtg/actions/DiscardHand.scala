package ru.bdm.mtg.actions

import ru.bdm.mtg.{Card, State}

object DiscardHand extends Action {
  override def act(state: State): Seq[State] = {
    val cards = state.hand.values.sum
    AddDiscard(cards) act (state)
  }
}
