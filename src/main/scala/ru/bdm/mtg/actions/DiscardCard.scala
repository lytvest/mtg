package ru.bdm.mtg.actions
import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.{Card, State}

case class DiscardCard(card:Card) extends Action{
  override def act(state: State): Seq[State] =
    Seq(state.copy(discard = state.discard - 1, hand = state.hand -~ card))
}
