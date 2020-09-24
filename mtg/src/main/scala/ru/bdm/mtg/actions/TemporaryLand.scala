package ru.bdm.mtg.actions

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.State
import ru.bdm.mtg.lands.{Land, PeatBog}

case class TemporaryLand(land: Land,count:Int, create: Int => Land) extends Action{
  override def act(state: State): Seq[State] =
    if (count - 1 > 0)
      Seq(state.copy(lands = (state.lands -~ land) +~ create(count - 1)))
    else
      Seq(state.copy(lands = state.lands -~ land))
}
