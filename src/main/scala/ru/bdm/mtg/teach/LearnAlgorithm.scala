package ru.bdm.mtg.teach

import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.layers.{DenseLayer, OutputLayer}
import org.deeplearning4j.nn.weights.WeightInit
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.learning.config.Nesterovs
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction
import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.{Agent, Battle, DeckShuffler, InputCreate, State}

object LearnAlgorithm extends App {


  case class Info(inputs:Array[Double], right: Double, ver:Double)

  val numInputs = 188
  val conf = createConfig()
  //println(InputCreate(new Battle(DeckShuffler.allCard.getSeq, (current: State, outcomes: Seq[State]) => 0).currentState).size)

  class LAgent extends Agent{
    override def chooseState(current: State, outcomes: Seq[State]): Int = {
      outcomes.map(s => InputCreate())
    }
  }


  val battle = new Battle()


  private def createConfig() = {
    val learningRate = 0.1
    new NeuralNetConfiguration.Builder()
      .seed(4058)
      .weightInit(WeightInit.XAVIER)
      .l2(0.001)
      .updater(new Nesterovs(learningRate, 0.4))
      .list()
      .layer(new DenseLayer.Builder().nIn(numInputs).nOut(100)
        .activation(Activation.SIGMOID)
        .build())
      .layer(new DenseLayer.Builder().nIn(100).nOut(20)
        .activation(Activation.SIGMOID)
        .build())
      .layer(new DenseLayer.Builder().nIn(20).nOut(20)
        .activation(Activation.SIGMOID)
        .build())

      .layer(new DenseLayer.Builder().nIn(20).nOut(20)
        .activation(Activation.SIGMOID)
        .build())

      .layer(new DenseLayer.Builder().nIn(20).nOut(20)
        .activation(Activation.SIGMOID)
        .build())

      .layer(new DenseLayer.Builder().nIn(20).nOut(20)
        .activation(Activation.SIGMOID)
        .build())

      .layer(new DenseLayer.Builder().nIn(20).nOut(20)
        .activation(Activation.SIGMOID)
        .build())
      .layer(new OutputLayer.Builder(LossFunction.MSE)
        .weightInit(WeightInit.XAVIER)
        .activation(Activation.ELU)
        .nIn(20).nOut(1).build())
      .build()
  }
}
