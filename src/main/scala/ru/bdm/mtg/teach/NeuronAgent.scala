package ru.bdm.mtg.teach

import ru.bdm.mtg.{Agent, InputCreate, State}
import ru.bdm.neurons.{Layer, NeuronSystem}

class NeuronAgent(log: Boolean = false) extends Agent {
  val layer = Layer(3) * Layer(3) * Layer(1)

  val ns = NeuronSystem(layer).setRandomWeights()

  override def chooseState(current: State, outcomes: Seq[State]): Int = {
    val outputs = outcomes.map(state => ns.work(InputCreate(state)).head)
    val max = outputs.zipWithIndex.max

    if (log) {
      println("Текущее состояние:\n" + current + "\n")
      print(outcomes.zipWithIndex.map { case (f, s) => (s, current.getChanges(f)) }.mkString("Возможные состояния:\n", "\n", "\nВыборано --> "))
      println(max._2)
      println(outputs)
    }

    max._2
  }
}
