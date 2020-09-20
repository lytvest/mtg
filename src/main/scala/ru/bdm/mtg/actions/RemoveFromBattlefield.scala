package ru.bdm.mtg.actions

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.{Card, State}

case class RemoveFromBattlefield(card:Card) extends Action{
  override def act(state: State): Seq[State] =
    state.copy(battlefield = state.battlefield -~ card)
}
