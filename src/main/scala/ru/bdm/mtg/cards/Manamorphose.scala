package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.ManaPool.ManaPoolOps
import ru.bdm.mtg.actions.{Action, AddDifferentColors, RemoveFromHandAndMana, TakeCards}
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHandAndMana}
import ru.bdm.mtg.{Card, ManaPool, State}

class Manamorphose extends Card {
  val cost: String = "CR"

  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, cost) -> AddDifferentColors("WWUUBBRR", 2) * TakeCards(1) * RemoveFromHandAndMana(this, cost),
    Discard.standard(this)
  )
}
