package ru.bdm.mtg.teach

import org.neuroph.core.NeuralNetwork
import org.neuroph.core.data.DataSet
import org.neuroph.core.transfer.{Linear, TransferFunction}
import org.neuroph.nnet.MultiLayerPerceptron
import org.neuroph.nnet.learning.{BackPropagation, MomentumBackpropagation}
import ru.bdm.mtg.{Agent, InputCreate, State}

class SAgent() extends Agent {

  val speed = 0.01
  val moment = speed / 10
  var e = 1.0
  var gamma = 0.9
  var allScore = 0.0

  //  val ns =
  val ns = try {
    NeuralNetwork.createFromFile("ns.nnet").asInstanceOf[NeuralNetwork[MomentumBackpropagation]]
  } catch {
    case _: Throwable =>
      println("create random ns...")
      new MultiLayerPerceptron(94, 50, 20, 10, 1) {
        getOutputNeurons.get(0).setTransferFunction(new Linear())
      }
  }
  val rule = new MomentumBackpropagation {
    override def doOneLearningIteration(trainingSet: DataSet): Unit = {
      onStart()
      super.doOneLearningIteration(trainingSet)
    }
  }
  ns.setLearningRule(rule)
  rule.setMomentum(moment)
  rule.setLearningRate(speed)

  var answers: List[Answer] = Nil

  override def nextCourse(current: State, nextState: State): Unit = {
    answers ::= Answer(current, work(current), score)
  }

  def work(state: State): Double = {
    ns.setInput(InputCreate(state): _*)
    ns.calculate()
    ns.getOutput()(0)
  }

  override def endGame(): Unit = {

    allScore = score
    rule.setMomentum(moment)
    rule.setLearningRate(speed)

    for (Answer(state, result, score) <- answers) {
      //println(s"result = $result, right= ${allScore - score} all= $allScore")
      val set = new DataSet(94, 1)
      set.add(InputCreate(state).toArray, Array(allScore - score))
      rule.doOneLearningIteration(set)
      rule.setMomentum(rule.getMomentum * gamma)
      rule.setLearningRate(rule.getLearningRate * gamma)
    }
    reset()
    if (e > 0.01) e *= 0.998
    else e = 0.01

    answers = Nil

  }

  override def chooseState(current: State, outcomes: Seq[State]): Int = {
    EGreedy(outcomes.map(work), e)
  }
}

case class Answer(state: State, result: Double, score: Double)
