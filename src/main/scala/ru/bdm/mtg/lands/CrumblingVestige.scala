package ru.bdm.mtg.lands

import ru.bdm.mtg.State
import ru.bdm.mtg.AllSet._
import ru.bdm.mtg.actions.{Action, AddDifferentColors, AddLand, AddMana, RemoveFromHand, Rotate}
import ru.bdm.mtg.conditions.{Condition, Discard, InHand, Is, IsPlay, IsPlayFromHand, IsPlayFromHandAndMana, IsTappedLand, NoPlayedLand, Not}

class CrumblingVestige(active:Boolean = false) extends Land(active) {


  override val description: Map[Condition, Action] = Map(
    (IsPlayFromHand(this) and NoPlayedLand) -> AddLand(this) * RemoveFromHand(this) * AddDifferentColors("WUBR", 1),
    IsTappedLand(this) -> Rotate(this) * AddMana("C"),
    Discard.standard(this)
  )


  override def copy(active: Boolean): Land = new CrumblingVestige(active)
}
