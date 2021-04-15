package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.Lessons.{LandDropLesson, SkipTurnLesson}
import ru.bdm.mtg.actions.{AddDifferentColors, RemoveMana}
import ru.bdm.mtg.teach.{AIAgent, NeuronAgent, SAgent}
import ru.bdm.neurons.{BackpropagationAlgorithm, Func, Layer, NeuronModel, NeuronSystem}
import ru.bdm.neurons.gen.GeneticAlgorithmForNeuron


object TestState extends App {

//  println((ManaPool("WWUUBBRR") getCombinations 2) mkString("\n"))
//  val state = State(manaPool = AllSet.empty[Char] ++~ "CU")
//  println(AddDifferentColors("WWUUBBRR", 2) * RemoveMana("CU") act(state) mkString("\n"))





  val agent = new AIAgent

  val game = new Battle(DeckShuffler.allCard.getSeq, agent, new SkipTurnLesson)
  game.run()

  println(agent.score)

  val ns = NeuronSystem(Layer(3) * Layer(10) * Layer(10) * Layer(1, Func.linear))
  println(ns.neurons.flatMap(_.weights).sum)

  val bp = new BackpropagationAlgorithm(ns,0.0001, 0.00001)
  while (true) {
    bp.teachOne(Seq(0.0,1.0,3.0), Seq(4.0))
    println(s"error=${bp.error} all=" + bp.errors.mkString("(", ", ", ")"))
    println("ns work = " + ns.work(InputCreate(game.state)))
  }

  //  var i = 0
//  while (!Thread.interrupted()) {
//    i += 1
//    var s_err = 0.0
//    var s_score = 0.0
//    for (i <- 1 to 100) {
//      val game = new Battle(DeckShuffler.allCard.getSeq, agent)
//      game.run()
//      s_err += agent.alg.error
//      s_score += agent.allScore
//    }
//    println(s"${i}:    err=${s_err / 100}    score=${s_score / 100}")
//    ns.saveToFile("super_ns.json")
//  }
//
//  for(i <- 1 to 3) {
//    val lesson = new SkipTurnLesson()
//    val game = new Battle(DeckShuffler.allCard.getSeq, new NeuronAgent(nN, true), lesson)
//    game.run()
//    println(s"Res lesson value ${lesson.lessonEvaluation}")
//  }
//
//  nN.saveToFile("SkipTurnReinfTry2.json")


}

