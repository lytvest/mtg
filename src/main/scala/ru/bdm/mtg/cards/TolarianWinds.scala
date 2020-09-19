package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet.{AllSetOps, empty}
import ru.bdm.mtg.actions.{Action, AddDiscard, TakeCards, PayDiscardHand}
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHandAndMana}
import ru.bdm.mtg.{Card, State}

class TolarianWinds extends Card {
  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, "CU") -> PayDiscardHand(this),
    Discard.standard(this)
  )
}
