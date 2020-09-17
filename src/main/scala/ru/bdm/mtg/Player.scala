package ru.bdm.mtg

trait Player {
  def chooseState(current:State, outcomes:Seq[State]): Int
}
