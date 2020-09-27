package ru.bdm.mtg.lands

import ru.bdm.mtg.actions._
import ru.bdm.mtg.conditions._

case class SandstoneNeedle(act: Boolean = false, count: Int = 2) extends Land(act) {


  override val description: Map[Condition, Action] = Map(
    (IsPlayFromHand(this) and NoPlayedLand) -> AddLand(this) * RemoveFromHand(this),
    IsTappedLand(this) -> AddMana("RR") * TemporaryLand(this, count, new SandstoneNeedle(false, _)),
    Discard.standard(this)
  )

  override def copy(active: Boolean): Land = new SandstoneNeedle(active, count)
}
