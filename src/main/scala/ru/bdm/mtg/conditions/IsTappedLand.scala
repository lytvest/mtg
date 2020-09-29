package ru.bdm.mtg.conditions

import ru.bdm.mtg.State
import ru.bdm.mtg.lands.Permanent

case class IsTappedLand(land:Permanent) extends Condition {
  override def check(state: State): Boolean =
    IsPlay.check(state) && land.active && state.lands.contains(land)
}
