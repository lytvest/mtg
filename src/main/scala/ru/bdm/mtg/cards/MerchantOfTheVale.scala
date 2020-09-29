package ru.bdm.mtg.cards

import ru.bdm.mtg.actions.{Action, AddBattlefield, AddDiscard, DiscardCard, RemoveFromBattlefield, RemoveFromHandAndMana, RemoveMana, TakeCards}
import ru.bdm.mtg.conditions.{CanPay, Condition, CountInHand, Discard, Is, IsBattlefield, IsPlayFromHandAndMana}
import ru.bdm.mtg.lands.Permanent

case class MerchantOfTheVale(exile: Boolean = false, override val active: Boolean = false) extends Permanent(active) {

  val cost = "CCR"

  override def description: Map[Condition, Action] = Map(
    IsPlayFromHandAndMana(this, cost) -> TakeCards(1) * AddDiscard(1) * RemoveFromHandAndMana(this, cost) * AddBattlefield(this),
    (Is(!exile) and CanPay(cost) and IsBattlefield(this) and CountInHand(_ > 1)) -> TakeCards(1) * AddDiscard(1) * RemoveMana(cost),
    IsPlayFromHandAndMana(this, "R") -> RemoveFromHandAndMana(this, "R") * AddBattlefield(MerchantOfTheVale(exile = true)),
    (Is(exile) and IsBattlefield(this)) -> RemoveFromBattlefield(this) * AddBattlefield(MerchantOfTheVale()),
    Discard(this) -> DiscardCard(this) * AddBattlefield(MerchantOfTheVale(exile = true))
  )

  override def name: String = super.name + (if (exile) "-EXILE" else "")

  override def copy(active: Boolean): Permanent = MerchantOfTheVale(exile, active)
}
