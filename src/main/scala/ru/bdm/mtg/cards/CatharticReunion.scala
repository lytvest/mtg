package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.ManaPool.ManaPoolOps
import ru.bdm.mtg.actions.{Action, AddDiscard, RemoveMana, TakeCards}
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHandAndMana}
import ru.bdm.mtg.{Card, ManaPool, State}

class CatharticReunion extends Card {
  val cost: String = "CR"

  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, cost) -> TakeCards(3) * AddDiscard(2) * RemoveMana(cost),
    Discard.standard(this)
  )
}
