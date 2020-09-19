package ru.bdm.mtg.actions

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.State
import ru.bdm.mtg.lands.Land

case class Rotate(land: Land) extends Action {
  override def act(state: State): Seq[State] =
    Seq(state.copy(lands = state.lands -~ land +~ land.copy(!land.active)))
}
