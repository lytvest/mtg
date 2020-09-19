package ru.bdm.mtg.conditions

import ru.bdm.mtg.ManaPool.ManaPoolOps
import ru.bdm.mtg.{Card, State}

case class CanPay(cost: String) extends Condition {
  override def check(state: State): Boolean =
    state.manaPool.canPay(cost)
}
