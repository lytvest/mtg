package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.actions.{Action, Reanimation, RemoveFromHandAndMana}
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHandAndMana}
import ru.bdm.mtg.{Card, State}

class BreathOfLife extends Card {

  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, "CCCW") -> Reanimation * RemoveFromHandAndMana(this, "CCCW"),
    Discard.standard(this)
  )
}
