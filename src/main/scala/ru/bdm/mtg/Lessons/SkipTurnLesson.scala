package ru.bdm.mtg.Lessons

import ru.bdm.mtg.{Lesson, State}

class SkipTurnLesson extends Lesson {
  override def evaluate(stateOld: State, stateCurrent: State): Double =
    if(stateOld.numberTurn < stateCurrent.numberTurn)
      1
    else
      -1
}