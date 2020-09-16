package ru.bdm.mtg

object TestState extends App {
  val manaPool = new ManaPool((new AllSet[Char]() ++ "WWWUUUBBBBBRRRRCC").map)
  val cost = "WUUBBRRCCCCC"
  println(manaPool.pay(cost).mkString("\n"))
}
