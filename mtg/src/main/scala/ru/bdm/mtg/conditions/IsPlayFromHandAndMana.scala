package ru.bdm.mtg.conditions

import ru.bdm.mtg.ManaPool.ManaPoolOps
import ru.bdm.mtg.{Card, Phase, State}

case class IsPlayFromHandAndMana(card:Card, cost:String) extends Condition {
  override def check(state: State): Boolean =
    IsPlay.check(state) && state.hand.contains(card) && state.manaPool.canPay(cost)
}
