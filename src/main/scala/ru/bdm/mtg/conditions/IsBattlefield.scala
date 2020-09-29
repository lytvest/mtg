package ru.bdm.mtg.conditions

import ru.bdm.mtg.State
import ru.bdm.mtg.lands.Permanent

case class IsBattlefield(card: Permanent) extends Condition {
  override def check(state: State): Boolean =
    state.battlefield.contains(card)
}
