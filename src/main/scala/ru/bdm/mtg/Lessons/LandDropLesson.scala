package ru.bdm.mtg.Lessons

import ru.bdm.mtg.{Lesson, State}

object LandDropLesson extends Lesson {
  var eval = 0
  override def evaluate(stateOld: State, stateCurrent: State): Unit = {
    if(stateCurrent.lands.size > stateOld.lands.size) eval += 1
  }

  override def lessonEvaluation: Double = eval
}