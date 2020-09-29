package ru.bdm.mtg.actions

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.State
import ru.bdm.mtg.lands.{Permanent, PeatBog}

case class TemporaryLand(land: Permanent, count:Int, create: Int => Permanent) extends Action{
  override def act(state: State): Seq[State] =
    if (count - 1 > 0)
      Seq(state.copy(lands = (state.lands -~ land) +~ create(count - 1)))
    else
      Seq(state.copy(lands = state.lands -~ land))
}
