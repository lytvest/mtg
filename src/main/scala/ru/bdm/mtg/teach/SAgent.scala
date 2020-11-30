package ru.bdm.mtg.teach

import ru.bdm.mtg.{Agent, InputCreate, State}
import ru.bdm.neurons.{BackpropagationAlgorithm, NeuronSystem}

class SAgent(val ns: NeuronSystem) extends Agent{

  val speed = 0.01
  val moment = speed / 10
  var e = 1.0
  var gamma = 0.9
  var allScore = 0.0

  val alg = new BackpropagationAlgorithm(ns, speed, moment)

  var answers = List.empty[Answer]

  override def chooseState(current: State, outcomes: Seq[State]): Int = {
    val outputs = outcomes.map(work)
    EGreedy(outputs, e)
  }


  override def nextCourse(current: State, nextState: State): Unit = {
    answers ::= Answer(current, work(current), score)
  }

  def work(state:State): Double = {
    ns.work(InputCreate(state)).head
  }

  override def endGame(): Unit = {
    allScore = score
    alg.speed = speed
    alg.moment = speed / 10
    for(Answer(state, result, score) <- answers){
      //println(s"result = $result, right= ${allScore - score} all= $allScore")
      alg.teachOne(InputCreate(state), Seq(allScore - score))
      alg.speed *= gamma
      alg.moment *= gamma
    }
    score = 0
  }
}
case class Answer(state:State, result:Double, score:Double)
