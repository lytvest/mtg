package ru.bdm.mtg.cards

import ru.bdm.mtg.Card
import ru.bdm.mtg.actions.{Action, AddBattlefield, AddDifferentColors, AddDiscard, DiscardCard, RemoveFromHandAndMana, RemoveMana, TakeCards}
import ru.bdm.mtg.conditions.{CanPay, Condition, Discard, IsBattlefield, InHand, Is, IsPlay, IsPlayFromHandAndMana}

case class MerchantOfTheVale(exile:Boolean = false) extends Card {

  val cost = "CCR"
  override val description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, cost) -> TakeCards(1) * AddDiscard(1) * RemoveFromHandAndMana(this, cost) * AddBattlefield(this),
    (Is(!exile) and CanPay(cost) and IsBattlefield(this)) -> TakeCards(1) * AddDiscard(1) * RemoveMana(cost),
    (IsPlay and InHand(this)) -> DiscardCard(this) * AddBattlefield(MerchantOfTheVale(true)),
    Discard(this) -> DiscardCard(this) * AddBattlefield(MerchantOfTheVale(true))
  )

  override def name: String = super.name + (if (exile) "-EXILE" else "")
}
