package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.{Card, State}

trait DiscardToGraveyard extends Card{
  override def discard(current: State): State = current.copy(
    graveyard = current.graveyard +~ this
  )
}
