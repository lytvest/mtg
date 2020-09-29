package ru.bdm.mtg.actions

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.State
import ru.bdm.mtg.lands.Permanent

case class AddLand(land:Permanent) extends Action{
  override def act(state: State): Seq[State] =
    Seq(state.copy(lands = state.lands +~ land, playedLand = true))
}
