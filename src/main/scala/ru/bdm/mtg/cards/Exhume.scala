package ru.bdm.mtg.cards

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.{Card, State}

class Exhume extends Card {
  override def cost: String = "CB"

  def exhume(current:State, choose:Card): State = {
    current.copy(hand = current.hand -~ this,
      battlefield = current.battlefield +~ choose,
      graveyard = current.graveyard -~ choose
    )
  }

  override def nextStates(current: State): Seq[State] = {
    val res = current.graveyard.keys.filter{card =>
      card.name == "UlamogsCrusher" || card.name == "HandOfEmrakul"
    }.map(exhume(current, _)).toSeq
    if (res.isEmpty)
      Seq(current)
    else res
  }

  override def isPlayable(current: State): Boolean = basicIsPlayable(current, "CB")
}
