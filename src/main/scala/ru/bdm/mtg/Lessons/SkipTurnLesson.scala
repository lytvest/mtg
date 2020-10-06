package ru.bdm.mtg.Lessons

import ru.bdm.mtg.{Lesson, State}

object SkipTurnLesson extends Lesson {
  var eval = 0
  override def evaluate(stateOld: State, stateCurrent: State): Unit = {
    if(stateCurrent.numberTurn > stateOld.numberTurn) eval += 1
  }

  override def lessonEvaluation: Double = eval
}