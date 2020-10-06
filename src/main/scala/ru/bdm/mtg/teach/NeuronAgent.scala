package ru.bdm.mtg.teach

import ru.bdm.mtg.{Agent, InputCreate, State}
import ru.bdm.neurons.{BackpropagationAlgorithm, Layer, NeuronSystem}

class NeuronAgent(log: Boolean = false, teacher: Boolean = false, fileName: String = "", inpustLog: Boolean = false) extends Agent {


  val ns = if (fileName == "") {
    val layer = Layer(94) * Layer(20) * Layer(10) * Layer(5) * Layer(1)
    NeuronSystem(layer).setRandomWeights()
  } else {
    NeuronSystem.readFromFile(fileName)
  }

  val speed = 0.7
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

  override def chooseState(current: State, outcomes: Seq[State]): Int = {

    val outputs = outcomes.map(state => ns.work(InputCreate(state)).head)
    val max = outputs.zipWithIndex.max

    if (log) {

      println("Текущее состояние:\n" + current + "\n")
      print(outcomes.zipWithIndex.map { case (state, index) =>
        s"$index ${current.getChanges(state)}" + (if (inpustLog) s"\n             -inputs-    ${InputCreate(state)}" else "")
      }.mkString("Возможные состояния:\n", "\n", "\nВыборано --> "))
      println(max._2)
      println(outputs)
    }
    if (teacher) {
      val answer = outputs.size - 1 //ConsolePlayer.readInt(outputs.size)
      if (answer == max._2)
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
    } else {
      max._2
    }
  }

  def save(fileName: String = this.fileName): Unit = {
    ns.saveToFile(fileName)
  }
}
