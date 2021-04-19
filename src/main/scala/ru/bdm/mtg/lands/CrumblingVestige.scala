package ru.bdm.mtg.lands

import ru.bdm.mtg.actions._
import ru.bdm.mtg.conditions._

case class CrumblingVestige(override val active: Boolean = false) extends Permanent(active) {


  override def description: Map[Condition, Action] = Map(
    (IsPlayFromHand(this) and NoPlayedLand) -> AddLand(this) * RemoveFromHand(this) * AddDifferentColors("WUBR", 1),
    IsTappedLand(this) -> Tap(this) * AddMana("C"),
    Discard.standard(this)
  )


  override def copy(active: Boolean): Permanent = new CrumblingVestige(active)
}
