package ru.bdm.mtg

import ru.bdm.mtg.ManaPool.ManaPoolOps
import ru.bdm.mtg.AllSet._
object TestState extends App {
  val game = new Engine(Deck.allCard, new ConsolePlayer)
  while (true)
    game.one()
}
