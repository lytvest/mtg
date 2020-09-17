package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.{Card, State}

class FaithlessLooting(override val cost: String = "R") extends Card with DiscardToGraveyard with Discarded {

  override def numberDiscard = 2
  override def states(current:State, cards:Seq[Card]): Seq[State] = {
    Seq(
      if (cost == "R") current.copy(
        hand = current.hand ++~ (RandomCard * 2) ,
        graveyard = current.graveyard +~ new FaithlessLooting("CCR")
      )
      else current.copy(
        hand = current.hand ++~ (RandomCard * 2)
      )
    )
  }
}
