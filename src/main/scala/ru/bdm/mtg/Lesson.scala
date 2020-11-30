package ru.bdm.mtg

trait Lesson {

  def isEnd(state:State):Boolean = state.numberTurn > 10

  def evaluate(stateOld:State, stateCurrent:State):Double = 0
}

object Lesson {
  val empty: Lesson = new Lesson{}
}
