package ru.bdm.mtg.teach

import org.neuroph.core.NeuralNetwork
import org.neuroph.core.data.DataSet
import org.neuroph.core.transfer.{Linear, TransferFunction}
import org.neuroph.nnet.MultiLayerPerceptron
import org.neuroph.nnet.learning.{BackPropagation, MomentumBackpropagation}
import ru.bdm.mtg.{Agent, InputCreate, State}

class SAgent(nameFile: String) extends Agent {

  val speed = 0.001
  var e = 1.0
  var gamma = 0.7
  var allScore = 0.0
  var error = 0.0
  var s_answer = 0.0
  var s_answer_count = 0
  //  val ns =
  val ns = try {
    print(s"load ns from [$nameFile] ")
    val ns = NeuralNetwork.createFromFile(nameFile).asInstanceOf[NeuralNetwork[BackPropagation]]
    println("ok!")
    ns
  } catch {
    case e: Throwable =>
      print(s"fail! (${e.getMessage}) \ncreate random ns... ")
      val ns = new MultiLayerPerceptron(189, 90, 40, 1) {
        getOutputNeurons.get(0).setTransferFunction(new Linear())
      }
      println("ok!")
      ns
  }
  val rule = new BackPropagation()
  ns.setLearningRule(rule)
  rule.setLearningRate(speed)

  var answers: List[Answer] = Nil


  override def startGame(): Unit = {
    s_answer = 0
    s_answer_count = 0
  }

  override def nextCourse(current: State, nextState: State): Unit = {
    answers ::= Answer(InputCreate.plusScore(current, nextState), score)
    s_answer = work(current, nextState)
    s_answer_count += 1
  }

  def work(old: State, state: State): Double = {
    ns.setInput(InputCreate.plusScore(old, state): _*)
    ns.calculate()
    ns.getOutput()(0)
  }

  override def endGame(): Unit = {

    allScore = score
    rule.setLearningRate(speed)
    error = 0
    var s_answer_right = 0.0
    for (Answer(input, score) <- answers) {
      //println(s"result = $result, right= ${allScore - score} all= $allScore")
      val set = new DataSet(189, 1)
      set.add(input.toArray, Array(allScore))
      s_answer_right += allScore
      rule.doOneLearningIteration(set)
      error += rule.getTotalNetworkError
      rule.setLearningRate(rule.getLearningRate * gamma)
    }
    error /= answers.size
    reset()
    if (e > 0.01) e *= 0.9998
    else e = 0.01

    s_answer = s_answer / s_answer_count
    s_answer_right /= answers.size
    s_answer = s_answer - s_answer_right

    answers = Nil

  }

  override def chooseState(current: State, outcomes: Seq[State]): Int = {
    EGreedy(outcomes.map(work(current, _)), e)
  }


  def waitAnswer(time: Long) = {
    //math.lo
  }
}

case class Answer(input: Seq[Double], score: Double)
