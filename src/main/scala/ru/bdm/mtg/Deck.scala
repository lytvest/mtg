package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.lands._
import ru.bdm.mtg.cards._

import scala.util.Random

object Deck {
  val allCard: AllSet.Type[Card] = AllSet.empty[Card] ++~ Seq(
    new CrumblingVestige() * 4,
    new Mountain() * 4,
    new PeatBog() * 4,
    new SandstoneNeedle() * 4,
    new ThrivingBluff * 4,
    new ThrivingIsle * 4,
    new ThrivingMoor * 4,
    new BreathOfLife * 4,
    new CarefulStudy * 4,
    new CatharticReunion * 4,
    new DangerousWager * 4,
    new DarkRitual * 4,
    new Duress * 4,
    new Exhume * 4,
    new FaithlessLooting() * 4,
    new HandOfEmrakul * 4,
    new LotusPetal * 4,
    new Manamorphose * 4,
    new RiseAgain * 4,
    new SimianSpiritGuide * 4,
    new ThrillOfPossibility * 4,
    new TolarianWinds * 4,
    new UlamogsCrusher * 4
  ).flatten

  def shuffleTheDeck(desk: Seq[Card]): Seq[Card] = {
    val array = Array.fill[Card](desk.length)(null)
    var i = 0
    desk.filter{ card =>
      val index = Random.nextInt(desk.length)
      if(array(index) == null){
        array(index) = card
        false
      } else true
    } foreach { card =>
      while (array(i) != null)
        i += 1
      array(i) = card
    }
    array
  }
}
