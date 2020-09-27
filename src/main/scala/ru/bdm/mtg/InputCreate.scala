package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.cards._
import ru.bdm.mtg.lands._

object InputCreate {

  private var cards: Seq[Any] = Seq(true, false).flatMap(bool =>
    Seq(
      new CrumblingVestige(bool),
      new Mountain(bool),
      new PeatBog(bool)
    )
  )

  cards ++= (for {
    color <- "RWUB"
    rest = "RWUB".filter(_ != color)
    choose <- rest.map(Some(_)) :+ None
    active <- Seq(true, false)
  } yield new Thriving(color, rest, active, choose))

  cards ++= (for {
    active <- Seq(true, false)
    count <- 0 to 2
  } yield new SandstoneNeedle(active, count))

  cards ++= Seq(
    new BreathOfLife,
    new CarefulStudy,
    new CatharticReunion,
    new DangerousWager,
    new DarkRitual,
    new DeepAnalysis,
    new Duress,
    new Exhume,
    new FaithlessLooting,
    new HandOfEmrakul,
    new IdeasUnbound,
    new InsolentNeonate,
    new LotusPetal,
    new Manamorphose,
    MerchantOfTheVale(),
    MerchantOfTheVale(true),
    new Ponder,
    new RiseAgain,
    new SimianSpiritGuide,
    new ThrillOfPossibility,
    new TolarianWinds,
    new UlamogsCrusher
  )
  cards ++= "WURBG"

  val values: Map[Any, Double] = cards.zipWithIndex.map{ case (card, index) =>
    (card, (index.toDouble - cards.size / 2 - (if (index <= cards.size / 2) 1 else 0)) / 10d)
  }.toMap
  println(values.toSeq.sortBy(_._2).mkString("\n"))


  def apply(state: State): Seq[Double] = {
    state.hand.getSeq.map(values.getOrElse(_, 0d)) ++
    state.battlefield.getSeq.map(values.getOrElse(_, 0d)) ++
    state.graveyard.getSeq.map(values.getOrElse(_, 0d)) ++
    state.manaPool.getSeq.map(values.getOrElse(_, 0d))
  }

}
