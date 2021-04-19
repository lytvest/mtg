package ru.bdm.mtg.teach

import java.io.File

import org.nd4j.common.primitives.Pair
import org.deeplearning4j.datasets.iterator.{AbstractDataSetIterator, DoublesDataSetIterator}
import org.deeplearning4j.nn.conf.{MultiLayerConfiguration, NeuralNetConfiguration}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.dataset.AsyncDataSetIterator
import org.nd4j.linalg.factory.Nd4j

import scala.jdk.CollectionConverters.IterableHasAsJava

class NeuronSystem(val conf: MultiLayerConfiguration, val fileName:String = "ns.save", val printIteration:Int = 100) {


  val net = try {
      val n = MultiLayerNetwork.load(new File(fileName), false)
      println(s"load ok! [$fileName]")
      n
    } catch {
      case e: Exception =>
        println(s"load fail ${e.getMessage}")
        new MultiLayerNetwork(conf)
    }

  net.init()
  // add an listener which outputs the error every 100 parameter updates
  net.setListeners(new ScoreIterationListener(printIteration))
  println(net.summary())

  def calculate(input: Array[Double]): Array[Double] = {
    val in = Nd4j.create(Array(input))
    val out = net.output(in)
    out.toDoubleVector
  }

  def calculateAll(input: Array[Array[Double]]): INDArray = {
    net.output(Nd4j.create(input))
  }


  def learnOne(inputs: Array[Array[Double]], outputs: Array[Array[Double]]): Unit = {
    net.fit(Nd4j.create(inputs), Nd4j.create(outputs))
  }

  def epoch:Int = net.getIterationCount

  def dataSetIterator(inputs: Seq[Array[Double]], outputs: Seq[Array[Double]]): DoublesDataSetIterator = {
    val seq = inputs.zip(outputs).map{case (k,v) => {
      Pair.of(k, v)
    }}

    new DoublesDataSetIterator(seq.asJava, seq.size)
  }

  def save(_fileName: String = this.fileName): Unit ={
    val file = new File(_fileName)
    try {
      file.getParentFile.mkdirs()
    } catch {
      case e:Exception =>
    }
    try {
      net.save(file, false)
    } catch {
      case e: Exception =>
        System.err.println(s"save fail ${e.getMessage}")
    }
  }
}
