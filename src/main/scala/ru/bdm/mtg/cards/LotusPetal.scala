package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet._
import ru.bdm.mtg.{Card, State}
import ru.bdm.mtg.AllSet._

class LotusPetal extends Card {
  override def cost: String = ""
  override def nextStates(current: State): Seq[State] = {
    "WUBR" map { color =>
      current.copy(manaPool = current.manaPool +~ color)
    }
  }
}
