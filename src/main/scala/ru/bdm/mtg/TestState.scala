package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps

object TestState extends App {




  val game = new Battle(Deck.allCard, new ConsolePlayer)

  game.run()
}
