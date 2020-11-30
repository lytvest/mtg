package ru.bdm.mtg

trait Agent extends Lesson {

  def name:String = "empty"

  var list: List[Int] = Nil
  var score = 0.0

  def chooseStateServer(current: State, outcomes:Seq[State]): Int = {
    val next = chooseState(current, outcomes)
    score += evaluate(current, outcomes(next))
    nextCourse(current, outcomes(next))
    list ::= next
    next
  }
  def chooseState(current:State, outcomes:Seq[State]): Int

  def nextCourse(current: State, nextState:State): Unit = {}

  def endGame(): Unit = {}

}
