package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.actions.{AddDifferentColors, RemoveMana}

object TestState extends App {

  println((ManaPool("WWUUBBRR") getCombinations 2) mkString("\n"))
  val state = State(manaPool = AllSet.empty[Char] ++~ "CU")
  println(AddDifferentColors("WWUUBBRR", 2) * RemoveMana("CU") act(state) mkString("\n"))

  val game = new Battle(Deck.allCard, new ConsolePlayer)
  game.run()

}
