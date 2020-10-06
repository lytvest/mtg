package ru.bdm.mtg

object LessonEmpty extends Lesson {
  var eval = 0
  override def evaluate(stateOld: State, stateCurrent: State): Unit = {
    eval += 1
  }

  override def lessonEvaluation: Double = eval
}
