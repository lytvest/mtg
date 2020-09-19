package ru.bdm.mtg.actions

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.{Card, Phase, State}

case class PayDiscardHand(card: Card) extends Action{
  override def act(state: State): Seq[State] = {
      val cards = state.hand.values.sum
      val newState = state.copy(phase = Phase.discard)
      state.hand.getSeq flatMap (card => card.description)
      getActiveActions(newState, state.hand.getSeq) flatMap { action =>
        action.act(newState) map
          (state => state.copy(phase = Phase.play)) flatMap
          (state => TakeCards(cards - 1) act(state))
      }
  }

  private def getActiveActions(state:State, cards: Seq[Card]):Seq[Action] = {
    cards.flatMap(_.description.filter{ case (condition, action) =>
      condition.check(state)
    }) map(_._2) toSeq
  }

}
