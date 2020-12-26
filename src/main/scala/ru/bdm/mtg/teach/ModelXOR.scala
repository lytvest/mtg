package ru.bdm.mtg.teach


import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.layers.{DenseLayer, OutputLayer}
import org.deeplearning4j.nn.weights.WeightInit
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.learning.config.Nesterovs
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction
import org.slf4j.LoggerFactory


/**
 *
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
    val outputs = Array(
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

    val net = new NeuronSystem(conf,"ns.save", 99)
    for (i <- 0 until nEpochs) {
      net.learnOne(inputs, outputs)
    }

    val output = net.calculateAll(inputs)

    System.out.println(output)

  }
}


