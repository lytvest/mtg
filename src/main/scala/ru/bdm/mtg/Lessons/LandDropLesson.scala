package ru.bdm.mtg.Lessons

import ru.bdm.mtg.{Lesson, State}

class LandDropLesson extends Lesson {
  var landsPlayed = 0.0
  var actionsDone = 0.0
  override def evaluate(stateOld: State, stateCurrent: State): Unit = {
    if(stateCurrent.lands.size > stateOld.lands.size) landsPlayed +=1
    if(stateOld.discard == 0) actionsDone += 1
  }

  override def lessonEvaluation: Double = 1 - landsPlayed / actionsDone
}