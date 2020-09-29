package ru.bdm.mtg.lands

import ru.bdm.mtg.actions._
import ru.bdm.mtg.conditions._

case class Mountain(override val active: Boolean = true) extends Permanent(active) {

  override def copy(active: Boolean): Permanent = new Mountain(active)

  override def description: Map[Condition, Action] = Map(
    (IsPlayFromHand(this) and NoPlayedLand) -> AddLand(this) * RemoveFromHand(this),
    IsTappedLand(this) -> Tap(this) * AddMana("R"),
    Discard.standard(this)
  )
}
