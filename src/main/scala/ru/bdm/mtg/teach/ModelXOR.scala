package ru.bdm.mtg.teach


import org.deeplearning4j.datasets.iterator.DoublesDataSetIterator
import org.deeplearning4j.nn.conf.MultiLayerConfiguration
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.distribution.UniformDistribution
import org.deeplearning4j.nn.conf.layers.DenseLayer
import org.deeplearning4j.nn.conf.layers.OutputLayer
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.dataset.DataSet
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.learning.config.{Nesterovs, Sgd}
import org.nd4j.linalg.lossfunctions.LossFunctions
import org.nd4j.evaluation.classification.Evaluation
import org.nd4j.linalg.dataset.api.iterator.{DataSetIterator, DataSetIteratorFactory}
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction
import org.slf4j.Logger
import org.slf4j.LoggerFactory


/**
 * This basic example shows how to manually create a DataSet and train it to an basic Network.
 * <p>
 * The network consists in 2 input-neurons, 1 hidden-layer with 4 hidden-neurons, and 2 output-neurons.
 * <p>
 * I choose 2 output neurons, (the first fires for false, the second fires for
 * true) because the Evaluation class needs one neuron per classification.
 * <p>
 * +---------+---------+---------------+--------------+
 * | Input 1 | Input 2 | Label 1(XNOR) | Label 2(XOR) |
 * +---------+---------+---------------+--------------+
 * |    0    |    0    |       1       |       0      |
 * +---------+---------+---------------+--------------+
 * |    1    |    0    |       0       |       1      |
 * +---------+---------+---------------+--------------+
 * |    0    |    1    |       0       |       1      |
 * +---------+---------+---------------+--------------+
 * |    1    |    1    |       1       |       0      |
 * +---------+---------+---------------+--------------+
 *
 * @author Peter Großmann
 * @author Dariusz Zbyrad
 */
object ModelXOR {

  private val log = LoggerFactory.getLogger(this.getClass)
  def main(args: Array[String]): Unit = {
    val seed = 1234 // number used to initialize a pseudorandom number generator.
    val nEpochs = 1000 // number of training epochs
    val learningRate = 0.1

    val numInputs = 2
    val numOutputs = 1
    val numHiddenNodes = 50

    val inputs = Array(
      Array(1.0, 1.0),
      Array(1.0, 0.0),
      Array(0.0, 1.0),
      Array(0.0, 0.0),
    )
    val outputs = Seq(
      Array(0.0),
      Array(1.0),
      Array(1.0),
      Array(0.0),
    )

    log.info("Network configuration and training...")
    val conf = new NeuralNetConfiguration.Builder()
      .seed(seed)
      .weightInit(WeightInit.XAVIER)
      .updater(new Nesterovs(learningRate, 0.9))

      .list()
      .layer(new DenseLayer.Builder().nIn(numInputs).nOut(numHiddenNodes)
        .activation(Activation.SIGMOID)
        .build())
      .layer(new OutputLayer.Builder(LossFunction.MSE)
        .weightInit(WeightInit.XAVIER)
        .activation(Activation.IDENTITY)
        .nIn(numHiddenNodes).nOut(numOutputs).build())
      .build()

    val net = new NeuronSystem(conf, 99)
    for (i <- 0 until nEpochs) {
      net.learnOne(inputs, outputs)
    }
    // create output for every training sample

    val output = net.calculateAll(inputs)

    System.out.println(output)
    // let Evaluation prints stats how often the right output had the highest value

  }
}


