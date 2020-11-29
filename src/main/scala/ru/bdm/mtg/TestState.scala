package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.Lessons.{LandDropLesson, SkipTurnLesson}
import ru.bdm.mtg.actions.{AddDifferentColors, RemoveMana}
import ru.bdm.mtg.teach.NeuronAgent
import ru.bdm.neurons.{Layer, NeuronSystem}
import ru.bdm.neurons.gen.GeneticAlgorithmForNeuron


object TestState extends App {

//  println((ManaPool("WWUUBBRR") getCombinations 2) mkString("\n"))
//  val state = State(manaPool = AllSet.empty[Char] ++~ "CU")
//  println(AddDifferentColors("WWUUBBRR", 2) * RemoveMana("CU") act(state) mkString("\n"))

  val nNStructure = Layer(94) * Layer(20) * Layer(10) * Layer(1)
  val nN = new NeuronSystem(nNStructure)
  val agent = new NeuronAgent(nN, false, true)
  val lesson = new SkipTurnLesson()
  for(i <- 1 to 1000){
    val game = new Battle(DeckShuffler.allCard.getSeq, agent, lesson)
    game.run()
    println(s"${i/10.0}%")
  }

  for(i <- 1 to 3) {
    val lesson = new SkipTurnLesson()
    val game = new Battle(DeckShuffler.allCard.getSeq, new NeuronAgent(nN, true), lesson)
    game.run()
    println(s"Res lesson value ${lesson.lessonEvaluation}")
  }

  nN.saveToFile("SkipTurnReinfTry2.json")


  //val game = new Battle(DeckShuffler.allCard.getSeq, new ConsolePlayer, LessonEmpty)
  def geneticAlg(){
    var max = 0d
    var iter = 0
    val nNStructure = Layer(94) * Layer(20) * Layer(1)
    val genAlg = new GeneticAlgorithmForNeuron(nNStructure, nNEvaluationInGames, true, 100)

    for(i <- 1 to 10) {
      genAlg.learnOne()
      val res = genAlg.bestResult
      println(s"$i $res")
    }

    val best = genAlg.chromosomes.head
    best.saveToFile("Best.json")
    for(i <- 1 to 10) {
      val game = new Battle(DeckShuffler.allCard.getSeq, new NeuronAgent(best, log = true), lesson = new LandDropLesson, seed = i)
      game.run()
    }



  }
  def nNEvaluationInGames(nN: NeuronSystem): Double = {
    println("kek")
    var overallValue = 0.0
    val numberOfGames = 10
    for(i <- 1 to numberOfGames) {
      val game = new Battle(DeckShuffler.allCard.getSeq, new NeuronAgent(nN), lesson = new LandDropLesson)
      game.run()
      overallValue += game.lesson.lessonEvaluation
    }
    overallValue
  }
}

