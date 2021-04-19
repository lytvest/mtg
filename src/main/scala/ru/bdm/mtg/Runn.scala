package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps

object Runn {

  def main(args: Array[String]): Unit = {
    println(DeckShuffler.allCard.getSeq.size)
  }
}
