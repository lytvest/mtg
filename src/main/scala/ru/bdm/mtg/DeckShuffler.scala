package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.cards._
import ru.bdm.mtg.lands._

import scala.util.Random

case class DeckShuffler(seed:Long) {

  val random = new Random(seed)

  def shuffle(desk: Seq[Card]): Seq[Card] = {

    val array = Array.fill[Card](desk.length)(null)
    var i = 0
    desk.filter{ card =>
      val index = random.nextInt(desk.length)
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

object DeckShuffler {
  val allCard: AllSet.Type[Card] = AllSet.empty[Card] ++~ Seq(
    CrumblingVestige() * 4,
    Mountain() * 1,
    LotusPetal() * 4,
    SandstoneNeedle() * 4,
    ThrivingBluff() * 2,
    ThrivingIsle() * 2,
    ThrivingMoor() * 2,
//    BreathOfLife() * 4,
    CarefulStudy() * 4,
    CatharticReunion() * 3,
//  //  Ponder() * 4,
    MerchantOfTheVale() * 1,
//    DangerousWager() * 4,
    DarkRitual() * 2,
//    Duress() * 4,
    Exhume() * 4,
    FaithlessLooting() * 4,
    HandOfEmrakul() * 3,
    Manamorphose() * 4,
//    RiseAgain() * 4,
    SimianSpiritGuide() * 4,
//    ThrillOfPossibility() * 4,
    InsolentNeonate() * 2,
//    IdeasUnbound() * 4,
//    TolarianWinds() * 4,
//    DeepAnalysis() * 4,
//    TolarianWinds() * 4,
    UlamogsCrusher() * 4,
    PeatBog() * 2,
    DragonBreath() * 4,
  ).flatten

}
object SM extends App{
  println(DeckShuffler.allCard.getSeq.size)
  println(DeckShuffler.allCard.mkString("\n   ", "\n   ", ""))
}
