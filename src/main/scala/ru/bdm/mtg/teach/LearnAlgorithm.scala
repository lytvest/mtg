package ru.bdm.mtg.teach

import org.deeplearning4j.nn.conf.layers.{DenseLayer, OutputLayer}
import org.deeplearning4j.nn.conf.{MultiLayerConfiguration, NeuralNetConfiguration}
import org.deeplearning4j.nn.weights.WeightInit
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.learning.config.Nesterovs
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction
import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.Lessons.SuperLesson
import ru.bdm.mtg._

import scala.collection.mutable
import scala.util.Random


object LearnAlgorithm extends App {


  case class Info(inputs: Array[Double], right: Double, ver: Double)

  val numInputs = 188
  val conf = createConfig()
  val ns = new NeuronSystem(conf, "ns.save", 100)

  var queue = mutable.Queue.empty[Info]
  var e = 1.0
  val e_delta = 0.99999

  class LAgent extends Agent {
    var res = List.empty[(Array[Double], Double)]

    override def chooseState(current: State, outcomes: Seq[State]): Int = {
      e *= e_delta
      EGreedy(outcomes.map(s => ns.calculate(InputCreate(current, s).toArray).head), e)
    }

    override def nextCourse(current: State, nextState: State): Unit = {
      res ::= InputCreate(current, nextState).toArray -> score
    }

    override def endGame(): Unit = {
      var a = 1.0
      val coef = 0.9
      // println("ans=" + res.map(_._2).mkString(", "))
      res.foreach { case (v, e) =>
        queue.enqueue(Info(v, score - e, a))
        a *= coef
      }
      res = List.empty
    }
  }

  val battle = new Battle(DeckShuffler.allCard.getSeq, new LAgent, new SuperLesson)

  val queueSize = 5000
  val batchSize = 200

  def fillQueue(): Unit = {
    while (queue.size < batchSize) {
      battle.run()
    }
    if (ns.epoch % ns.printIteration == 0) {
      println("queue = " + queue.size + " e=" + e)
    }
    battle.run()
  }

  def getBatch(batchSize: Int = this.batchSize): (Array[Array[Double]], Array[Array[Double]]) = {
    val arr = new Array[Array[Double]](batchSize)
    val out = new Array[Array[Double]](batchSize)
    var index = 0
    while (index < batchSize) {
      val i = Random.nextInt(queue.size)
      if (Random.nextDouble() < queue(i).ver) {
        arr(index) = queue(i).inputs
        out(index) = Array(queue(i).right)
        index += 1
      }
    }
    (arr, out)
  }

  def learnOne(): Unit = {
    fillQueue()
    val (in, out) = getBatch()
    ns.learnOne(in, out)
    while (queue.size > queueSize)
      queue.dequeue()
    printPickRandom()
  }

  def printPickRandom(): Unit = {
    if (ns.epoch % ns.printIteration == 0) {
      val (q, a) = getBatch(3)
      println(ns.calculateAll(q).toDoubleVector.mkString("{", ", ", "}") + s" answer( ${a.map(_.mkString("..")).mkString(", ")} )")
    }
  }

  while (true) {
    learnOne()
    if (ns.epoch % 100 == 0) {}
    ns.save()
    if (ns.epoch % 1000 == 0) {}
    ns.save(s"ns/ns_${(ns.epoch / 500) % 1000}.save")
  }


  private def createConfig(): MultiLayerConfiguration = {
    val learningRate = 0.001
    new NeuralNetConfiguration.Builder()
      .seed(40658)
      .weightInit(WeightInit.NORMAL)
      //.l2(0.001)
      .updater(new Nesterovs(learningRate, 0.4))
      .list()
      .layer(new DenseLayer.Builder().nIn(numInputs).nOut(30)
        .activation(Activation.SIGMOID)
        .build())
      .layer(new DenseLayer.Builder().nIn(30).nOut(50)
        .activation(Activation.SIGMOID)
        .build())
      .layer(new DenseLayer.Builder().nIn(50).nOut(20)
        .activation(Activation.SIGMOID)
        .build())
      .layer(new DenseLayer.Builder().nIn(20).nOut(20)
        .activation(Activation.SIGMOID)
        .build())

      //      .layer(new DenseLayer.Builder().nIn(20).nOut(20)
      //        .activation(Activation.SIGMOID)
      //        .build())
      //
      //      .layer(new DenseLayer.Builder().nIn(20).nOut(20)
      //        .activation(Activation.SIGMOID)
      //        .build())

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
