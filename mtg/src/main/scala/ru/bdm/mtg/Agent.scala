package ru.bdm.mtg

trait Agent {
  def chooseState(current:State, outcomes:Seq[State]): Int
}
