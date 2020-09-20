package ru.bdm.mtg.cards

import ru.bdm.mtg.{Card, State}
import ru.bdm.mtg.AllSet._
import ru.bdm.mtg.actions.{Action, AddDiscard, RemoveFromHandAndMana, TakeCards}
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHandAndMana}

class CarefulStudy extends Card {
  val cost: String = "U"


  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, cost) ->AddDiscard(2) * TakeCards(2) * RemoveFromHandAndMana(this, cost),
    Discard.standard(this)
  )
}
