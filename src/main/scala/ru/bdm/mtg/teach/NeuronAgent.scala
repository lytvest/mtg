package ru.bdm.mtg.teach

import ru.bdm.mtg.{Agent, InputCreate, State}
import ru.bdm.neurons.{BackpropagationAlgorithm, Layer, NeuronSystem}

import scala.util.Random

class NeuronAgent(ns: NeuronSystem, log: Boolean = false, teacher: Boolean = false, fileName: String = "", inpustLog: Boolean = false) extends Agent {

  val speed = 0.00005
  val alg = new BackpropagationAlgorithm(ns, moment = 0.1)
  var countRight = 0
  var countNoRight = 0

  if (log) {
    val input = InputCreate(State())
    println(s"Количество входных нейронов: ${ns.model.inputs.size}")
    println(s"Количество входных данных: ${input.size}")
    println(s"Модель нейронной сети: ${ns.model}")
  }

  override def name: String = "neurons"

  def softMax(values: Seq[Double]): Int ={
    val max = values.max
    var allMax = List[Int]()

    for(index <- values.indices) {
      if(values(index) == max)
        allMax ::= index
    }
    allMax(Random.nextInt(allMax.size))
  }

  def printLog(current: State, outcomes: Seq[State], chosen: Int, outputs: Seq[Double]): Unit ={
    println("Текущее состояние:\n" + current + "\n")
    print(outcomes.zipWithIndex.map { case (state, index) =>
      s"$index ${current.getChanges(state)}" + (if (inpustLog) s"\n             -inputs-    ${InputCreate(state)}" else "")
    }.mkString("Возможные состояния:\n", "\n", "\nВыборано --> "))
    println(chosen)
    println(outputs)
  }

  def teach(chosen: Int, outputs: Seq[Double], outcomes: Seq[State]): Int ={
      val answer = outputs.size - 1 //ConsolePlayer.readInt(outputs.size)
      if (answer == chosen)
        countRight += 1
      else
        countNoRight += 1
      outcomes.zipWithIndex.foreach { case (state, index) =>
        if (index == answer) {
          alg.speed = speed
          alg.teachOne(InputCreate(state), Seq(1d))
        } else {
          alg.speed = speed / Math.max(outputs.size - 1, 1)
          alg.teachOne(InputCreate(state), Seq(0d))
        }
      }
    answer
  }

  override def chooseState(current: State, outcomes: Seq[State]): Int = {

    val outputs = outcomes.map(state => ns.work(InputCreate(state)).head)
    val chosen = softMax(outputs)

    if (log) printLog(current, outcomes, chosen, outputs)
    if(teacher){
      teach(chosen,outputs, outcomes)
    } else {
      chosen
    }
  }

  def save(fileName: String = this.fileName): Unit = {
    ns.saveToFile(fileName)
  }
}
