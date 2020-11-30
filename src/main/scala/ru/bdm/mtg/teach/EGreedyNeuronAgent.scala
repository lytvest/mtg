package ru.bdm.mtg.teach
import ru.bdm.mtg.{Agent, InputCreate, State}
import ru.bdm.neurons.{Layer, NeuronSystem}

import scala.util.Random
class EGreedyNeuronAgent(ns: NeuronSystem, log: Boolean = false, e: Double = 0.1) extends NeuronAgent(ns, log)
{
  private val rand = Random
  override def chooseState(current: State, outcomes: Seq[State]): Int = {

    val outputs = outcomes.map(state => ns.work(InputCreate(state)).head)
    EGreedy(outputs, e)
  }
}
