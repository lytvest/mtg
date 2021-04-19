package ru.bdm.mtg.cards

import ru.bdm.mtg.Card
import ru.bdm.mtg.actions.{Action, Reanimation, RemoveFromHandAndMana}
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHandAndMana}

case class Exhume() extends Card {

  override def description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, "CB") -> Reanimation * RemoveFromHandAndMana(this, "CB"),
    Discard.standard(this)
  )
}
