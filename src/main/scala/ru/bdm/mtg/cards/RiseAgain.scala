package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.actions.{Action, Reanimation, RemoveFromHandAndMana}
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHandAndMana}
import ru.bdm.mtg.{Card, State}

case class RiseAgain() extends Card {
  val cost: String = "CCCCB"
  override def description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, cost) -> Reanimation * RemoveFromHandAndMana(this, cost),
    Discard.standard(this)
  )
}
