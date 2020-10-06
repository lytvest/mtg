package ru.bdm.mtg.teach
import ru.bdm.mtg.{Agent, InputCreate, State}
import ru.bdm.neurons.{Layer, NeuronSystem}

import scala.util.Random
class EGreedyNeuronAgent(log: Boolean = false, e: Double = 0.1) extends NeuronAgent(log)
{
  private val rand = Random
  override def chooseState(current: State, outcomes: Seq[State]): Int = {

    val outputs = outcomes.map(state => ns.work(InputCreate(state)).head)
    val max = outputs.zipWithIndex.max
    var chosen = rand.nextInt(outcomes.size)
    if(rand.nextDouble() > e)
      chosen = max._2
    if(log)
      printInfo(current, outcomes, chosen, outputs)
    chosen
  }
}
