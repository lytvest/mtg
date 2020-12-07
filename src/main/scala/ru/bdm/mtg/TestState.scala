package ru.bdm.mtg

import java.io.{File, FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.{Calendar, Date, TimeZone}

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.Lessons.{LandDropLesson, SkipTurnLesson, SuperLesson}
import ru.bdm.mtg.actions.{AddDifferentColors, RemoveMana}
import ru.bdm.mtg.cards.{HandOfEmrakul, UlamogsCrusher}
import ru.bdm.mtg.teach.{AIAgent, NeuronAgent, SAgent}
import ru.bdm.neurons.{BackpropagationAlgorithm, Func, Layer, NeuronModel, NeuronSystem}
import ru.bdm.neurons.gen.GeneticAlgorithmForNeuron


object TestState extends App {

  val format = new SimpleDateFormat("HH:mm:ss.SSS")
  println("starting...")
  val fileName = "all.ns"
  val info = InfoStart(fileName)
  val agent = new SAgent(fileName)


  while (true) {

    val startTime = System.currentTimeMillis()

    val battle = new Battle(DeckShuffler.allCard.getSeq, agent, new SuperLesson)
    info.epoh += 1
    var score = 0.0

    var countCrusher = 0
    var error = 0.0
    for (i <- 0 until info.K) {
      if(i % (info.K / 10.0) == 0) print(".")
      battle.run()
      countCrusher += battle.currentState.battlefield.getSeq.count {
        case _: UlamogsCrusher => true
        case _: HandOfEmrakul => true
        case _ => false
      }
      score += agent.allScore
      error += agent.error
    }
    val time = System.currentTimeMillis() - startTime
    info.time += time

    agent.ns.save(fileName)
    info.write(fileName)

    println(s"  [${info.epoh*info.K}] :   score = ${score / info.K}   e=${agent.e}    crushers=${countCrusher}   error=${error / info.K} time=${ptime(time)}  all_time=${ptime(info.time)} s_answer=${agent.s_answer}    ${battle.currentState.battlefield}")
    //print("save...")
    //println(  "ok!")
  }

  def ptime(t: Long) = {
    format.format(t - (5 * 60 * 60 * 1000))
  }
  case class InfoStart(var epoh: Int, var K: Int, var time: Long) extends Serializable{

    def write(fileName: String):Unit = {
      try {
        val stream = new ObjectOutputStream(new FileOutputStream(fileName + InfoStart.format))
        stream.writeObject(this)
        stream.flush()
        stream.close()
      } catch {
        case e: Exception =>
          println(e.getMessage)
      }
    }
  }
  object InfoStart{
    val format = ".info"
    def apply(fileName:String):InfoStart = {
      try {
        val stream = new ObjectInputStream(new FileInputStream(fileName + format))
        stream.readObject().asInstanceOf[InfoStart]
      } catch {
        case e: Throwable =>
          InfoStart(0, 50, 0)
      }
    }
  }

}

