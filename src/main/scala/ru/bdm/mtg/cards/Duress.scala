package ru.bdm.mtg.cards

import ru.bdm.mtg.{Card, State}
import ru.bdm.mtg.AllSet._
import ru.bdm.mtg.actions.{Action, RemoveFromHandAndMana}
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHandAndMana}

case class Duress() extends Card {
  val cost: String = "B"
  override def description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, cost) -> RemoveFromHandAndMana(this, cost),
    Discard.standard(this)
  )
}
