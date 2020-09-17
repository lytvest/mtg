package ru.bdm.mtg

import ru.bdm.mtg.ManaPool.ManaPoolOps
import ru.bdm.mtg.AllSet._
object TestState extends App {
  val manaPool: ManaPool.Type = ManaPool("WWRC")
  val cost = "WWRU"
  println(manaPool)
  println(manaPool.pay(cost))
  println(manaPool.canPay(cost))
}
