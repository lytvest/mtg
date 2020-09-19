package ru.bdm.mtg.lands

import ru.bdm.mtg.State
import ru.bdm.mtg.AllSet._
import ru.bdm.mtg.actions.{Action, AddDifferentColors, AddLand, AddMana, RemoveFromHand, Rotate}
import ru.bdm.mtg.conditions.{Condition, Discard, IsPlayFromHand, IsTappedLand, NoPlayedLand}

class Mountain(active: Boolean = true) extends Land(active) {

  override def copy(active: Boolean): Land = new Mountain(active)

  override val description: Map[Condition, Action] = Map(
    (IsPlayFromHand(this) and NoPlayedLand) -> AddLand(this) * RemoveFromHand(this),
    IsTappedLand(this) -> Rotate(this) * AddMana("R"),
    Discard.standard(this)
  )
}
