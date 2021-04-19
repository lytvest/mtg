package ru.bdm.mtg.Lessons

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.cards.{HandOfEmrakul, UlamogsCrusher}
import ru.bdm.mtg.{Lesson, State}

class SuperLesson extends Lesson {
  override def evaluate(stateOld: State, stateCurrent: State): Double = {
    var sum = 0.0
    sum += stateCurrent.lands.getSeq.size * 1
    sum += stateCurrent.battlefield.getSeq.size * 0.5
    sum += stateCurrent.battlefield.getSeq.count {
      case _: UlamogsCrusher => true
      case _: HandOfEmrakul => true
      case _ => false
    } * (1000.0 / stateCurrent.numberTurn)
    if (stateCurrent.numberTurn != stateOld.numberTurn){
      sum -= stateCurrent.manaPool.getSeq.size
    }
    sum
  }
}
