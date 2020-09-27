package ru.bdm.mtg.cards

import ru.bdm.mtg.Card
import ru.bdm.mtg.actions.{Action, AddBattlefield, AddDifferentColors, RemoveFromBattlefield, RemoveFromHand}
import ru.bdm.mtg.conditions.{Condition, Discard, InHand, IsBattlefield, IsPlay}

case class LotusPetal() extends Card {

  override val description: Map[Condition, Action] = Map(
    (IsPlay and InHand(this)) -> RemoveFromHand(this) * AddBattlefield(this),
    (IsPlay and IsBattlefield(this)) -> RemoveFromBattlefield(this) * AddDifferentColors("WUBR", 1),
    Discard.standard(this)
  )
}
