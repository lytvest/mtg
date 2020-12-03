package ru.bdm.mtg.Lessons

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.cards.{HandOfEmrakul, UlamogsCrusher}
import ru.bdm.mtg.{Lesson, State}

class SuperLesson extends Lesson {
  override def evaluate(stateOld: State, stateCurrent: State): Double = {
    var sum = 0.0
    sum += stateCurrent.lands.getSeq.size * 1
    sum += stateCurrent.battlefield.getSeq.size * 0.1
    sum += stateCurrent.battlefield.getSeq.count {
      case _: UlamogsCrusher => true
      case _: HandOfEmrakul => true
      case _ => false
    } * 10
    sum -= stateCurrent.numberTurn * 0.3
    sum
  }
}
