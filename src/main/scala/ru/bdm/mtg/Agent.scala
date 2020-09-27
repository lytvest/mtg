package ru.bdm.mtg

trait Agent {

  def name:String = "empty"

  var list: List[Int] = Nil

  def chooseStateServer(current: State, outcomes:Seq[State]): Int = {
    val next = chooseState(current, outcomes)
    list ::= next
    next
  }
  def chooseState(current:State, outcomes:Seq[State]): Int

}
