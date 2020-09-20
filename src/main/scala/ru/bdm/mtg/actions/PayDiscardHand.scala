package ru.bdm.mtg.actions

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.{Card, Phase, State}

case class PayDiscardHand(card: Card) extends Action {
  override def act(state: State): Seq[State] = {
    val cards = state.hand.values.sum - 1
    val newState: State = state.copy(phase = Phase.discard)

    val discardStates = getActiveActions(newState, state.hand.getSeq).foldRight(Seq(newState)) { case (action, seq) =>
      seq flatMap { state =>
        action.act(state)
      }
    }
    discardStates.map(_.copy(phase = Phase.play)).flatMap{ state =>
      TakeCards(cards).act(state)
    }
  }

  private def getActiveActions(state: State, cards: Seq[Card]): Seq[Action] = {
    cards.flatMap(_.description.filter { case (condition, action) =>
      condition.check(state)
    }) map (_._2) toSeq
  }

}
