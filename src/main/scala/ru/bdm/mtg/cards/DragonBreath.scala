package ru.bdm.mtg.cards

import ru.bdm.mtg.Card
import ru.bdm.mtg.actions._
import ru.bdm.mtg.conditions._

case class DragonBreath() extends Card {

  override def description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, "CR") -> HasteChoose * RemoveFromHandAndMana(this, "CR"),
    (IsPlay and InGraveyard(this)) -> HasteChoose * RemoveFromGraveyard(this),
    Discard(this) -> DiscardCard(this) * AddGraveyard(this)
  )

}
