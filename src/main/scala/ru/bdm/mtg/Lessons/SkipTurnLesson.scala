package ru.bdm.mtg.Lessons

import ru.bdm.mtg.{Lesson, State}

trait SkipTurnLesson extends Lesson {
  override def evaluate(stateOld: State, stateCurrent: State): Double =
    if(stateOld.numberTurn < stateCurrent.numberTurn)
      0.01
    else
      -0.01
}