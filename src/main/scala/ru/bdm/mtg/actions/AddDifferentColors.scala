package ru.bdm.mtg.actions
import ru.bdm.mtg.AllSet.AllSetOps

import ru.bdm.mtg.{ManaPool, State}

case class AddDifferentColors(colors:String, number: Int) extends Action{
  override def act(state: State): Seq[State] = {
    val a = ManaPool(colors).getCombinations(number) map { pool =>
      val mana = ManaPool(colors) --~ pool.getSeq
      state.copy(manaPool = state.manaPool ++ mana)
    }
    println(a)
    a
  }
}
