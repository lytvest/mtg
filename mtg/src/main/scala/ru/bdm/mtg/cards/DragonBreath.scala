package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.actions.{Action, AddGraveyard, Aura, DiscardCard, RemoveFromGraveyard, RemoveFromHandAndMana}
import ru.bdm.mtg.conditions.{Condition, Discard, InGraveyard, IsPlay, IsPlayFromHandAndMana}
import ru.bdm.mtg.{Card, State}

case class DragonBreath(target: Option[Card] = None) extends Card {

  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, "CR") -> Aura(target => DragonBreath(Some(target))) * RemoveFromHandAndMana(this, "CR"),
    (IsPlay and InGraveyard(this)) -> Aura(target => DragonBreath(Some(target))) * RemoveFromGraveyard(this),
    Discard(this) -> DiscardCard(this) * AddGraveyard(this)
  )

  override def toString: String = super.toString + s"[${target.getOrElse("")}]"
}
