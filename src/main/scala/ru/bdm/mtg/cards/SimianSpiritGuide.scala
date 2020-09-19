package ru.bdm.mtg.cards

import ru.bdm.mtg.Card
import ru.bdm.mtg.actions.{Action, AddMana, RemoveFromHand}
import ru.bdm.mtg.conditions.{Condition, Discard, InHand, IsPlay}

class SimianSpiritGuide extends Card {

  override val description: Map[Condition, Action] = Map(
    (IsPlay and InHand(this)) -> AddMana("R") * RemoveFromHand(this),
    Discard.standard(this)
  )
}
