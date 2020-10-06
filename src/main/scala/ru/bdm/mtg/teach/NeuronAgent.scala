package ru.bdm.mtg.teach

import ru.bdm.mtg.{Agent, InputCreate, State}
import ru.bdm.neurons.{Layer, NeuronSystem}

class NeuronAgent(log: Boolean = false) extends Agent {
  val layer = Layer(94) * Layer(10) * Layer(1)

  val ns = NeuronSystem(layer).setRandomWeights()

  if(log){
    val input = InputCreate(State())
    println(s"Количество входных нейронов: ${layer.inputs.size}")
    println(s"Количество входных данных: ${input.size}")
    println(s"Модель нейронной сети: ${layer}")
  }

  override def name: String = "neurons"



  override def chooseState(current: State, outcomes: Seq[State]): Int = {

    val outputs = outcomes.map(state => ns.work(InputCreate(state)).head)
    val max = outputs.zipWithIndex.max

    printInfo(current, outcomes, max._2, outputs)
    max._2
  }

  def printInfo(current: State, outcomes: Seq[State], chosen: Int, outputs: Seq[Double]): Unit ={
    if (log) {

      println("Текущее состояние:\n" + current + "\n")
      print(outcomes.zipWithIndex.map { case (f, s) => (s, current.getChanges(f)) }.mkString("Возможные состояния:\n", "\n", "\nВыборано --> "))
      println(chosen)
      println(outputs)
    }
  }
}
