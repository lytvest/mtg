package ru.bdm.mtg

import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.layers.{DenseLayer, OutputLayer}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.h2.Driver
import org.nd4j.evaluation.classification.Evaluation
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.learning.config.Adam
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction
import org.nd4j.nativeblas.Nd4jCpu.NDArray
import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.cards.CarefulStudy
import ru.bdm.mtg.lands.Permanent

import java.io.{ByteArrayInputStream, File, ObjectInputStream}
import java.sql.DriverManager
import java.util.Base64
import scala.io.StdIn

object OptimalPlayFromDataBase  {

  DriverManager.registerDriver(new Driver)
  val conn = DriverManager.getConnection("jdbc:h2:~/mtg-db", "bdm", "1234")

  def readAllId() = {
    val sql = "select id, len, i from starts"
    val res = toSql(sql).executeQuery()
    var list = List[(Int, Int, Boolean)]()
    while(res.next()){
      list ::= (res.getInt("id"), res.getInt("len"), res.getBoolean("i"))
    }
    list
  }

  def getStateFromId(id:Int): State = {
    print(".")
    val sql = s"select state from starts where id = $id"
    val res = toSql(sql).executeQuery()
    if(!res.next()){
      throw new Exception("error res.next")
    }
    val in = new ObjectInputStream(res.getBinaryStream("state"))
    in.readObject().asInstanceOf[State]
  }

  def containInDB(id: Int, state: State): Option[Int] ={
    val hash = H.getHash(state)
    val sql = s"select value from mtg where state = ? and start = $id"
    val st = toSql(sql)
    st.setBinaryStream(1, new ByteArrayInputStream(hash))
    val res = st.executeQuery()
    if(res.next())
      Some(res.getInt("value"))
    else
      None
  }

  private def toSql(sql: String) = {
    conn.prepareStatement(sql)
  }

  def play(id: Int, len: Int): Unit ={
    println("play start")
    var state = getStateFromId(id)
    BattleObserver.N = 3
    var n_count = 0
    while (!BattleObserver.containsWinCard(state)) {
      n_count+=1
      println(s"$n_count : ${state.string}")
      val nextStates = BattleObserver.nextStates(state)
      println(s"all ${nextStates.size}")
      val ns = nextStates.map(st => containInDB(id, st) -> st).filter(_._1.isDefined).map(p => p._1.get -> p._2)
      val nextState =
      if (ns.nonEmpty) {
        ns.minBy(_._1)._2
      } else {
        println(nextStates.map(e => Base64.getEncoder.encodeToString(H.getHash(e))).zipWithIndex.mkString("\n"))
        print("enter:")
        nextStates(StdIn.readInt())
      }
      println(state.getChanges(nextState))
      println()
      state = nextState
    }
    println(state)
  }

  def main(args: Array[String]): Unit = {

    val ids = readAllId()
    println(ids.mkString("\n"))
    val id_list = StdIn.readLine().split(" ").map(_.toInt).toList
    val fileName = "ns/ns_1.save"
    val ns = try {
      MultiLayerNetwork.load(new File(fileName), true)
    } catch {
      case e: Exception =>
        println(s"fail load file $fileName")
        createNewNetwork()
    }
    for(j <- 1 to 1000000) {
      val res = createInputData(ns, id_list, 2)

      println("data created...")
      ns.setListeners(new ScoreIterationListener(10))
      for (i <- 1 to 100)
        ns.fit(res._1, res._2)

      ns.save(new File(fileName), true)

      val eval = new Evaluation(sizeOutput);
      eval.eval(res._2, ns.output(res._1))
      println(eval.stats())
    }
  }

  val sizeInput = InputCreate(State()).length
  val sizeOutput = 9

  def createNewNetwork(): MultiLayerNetwork = {
    val conf = new NeuralNetConfiguration.Builder()
      .activation(Activation.TANH)
      .weightInit(WeightInit.XAVIER)
      .updater(new Adam())
      .list()
      .layer(new DenseLayer.Builder().nIn(sizeInput).nOut(80).build())
      .layer(new DenseLayer.Builder().nOut(10).build())
      .layer(new DenseLayer.Builder().nOut(10).build())
      .layer(new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD)
        .nOut(sizeOutput)
        .activation(Activation.SOFTMAX).build())
      .build()
    val ns = new MultiLayerNetwork(conf)
    ns.init()
    ns
  }

  def getRightOutput(ns: MultiLayerNetwork, seq:Seq[State]): (Array[Double], Int, Int) = {
    val input = seq.map(InputCreate(_)).toArray
    val out = ns.output(Nd4j.create(input)).toDoubleMatrix.map { arr =>
      arr.zipWithIndex.maxBy(_._1).swap
    }.zipWithIndex.max
    (input(out._2) , out._2, out._1._1)
  }

  def createOutput(id: Int): Array[Double] = {
    val out = new Array[Double](9)
    out(id) = 1.0
    out
  }

  def createInputData(ns: MultiLayerNetwork, ids: List[Int], len: Int): (INDArray, INDArray) = {
    BattleObserver.N = len
    var listInput = List[Array[Double]]()
    var listOutput = List[Array[Double]]()
    for(id <- ids) {
      var state = getStateFromId(id)
      var count_curr = 0
      while (!BattleObserver.isEndStates(state)) {
        count_curr += 1
        if(BattleObserver.containsWinCard(state))
          println("Win!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        //println(count_curr)
        val nextStates = BattleObserver.nextStates(state)
        val l = getRightOutput(ns, nextStates)
        val right = containInDB(id, state).getOrElse(0)
        //println(s"actual=${l._3}  right=$right curr${state.numberTurn} ${BattleObserver.containsWinCard(state)}")
        state = nextStates(l._2)
        listInput ::= l._1
        listOutput ::= createOutput(right)
      }
    }
    Nd4j.create(listInput.toArray) -> Nd4j.create(listOutput.toArray)
  }


}
