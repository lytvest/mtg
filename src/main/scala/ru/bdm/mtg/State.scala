package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps
import ru.bdm.mtg.lands.Land


case class State(
                  manaPool: ManaPool.Type = AllSet.empty[Char],
                  hand: AllSet.Type[Card] = AllSet.empty[Card],
                  graveyard: AllSet.Type[Card] = AllSet.empty[Card],
                  battlefield: AllSet.Type[Card] = AllSet.empty[Card],
                  lands: AllSet.Type[Land] = AllSet.empty[Land],
                  library: Seq[Card] = Seq.empty[Card],
                  phase: Phase.Phase = Phase.play,
                  takeCards:Int = 0,
                  discard:Int = 0,
                  numberTurn:Int = 1,
                  endTurnDiscards:Int = 0,
                  playedLand:Boolean = false
                ){
  override def toString: String =
    s"   mana{${manaPool.mkString(", ")}}" +
      s"   turn=$numberTurn" +
      s"   hand{${hand.mkString(", ")}}" +
      s"   lands{${lands.mkString(", ")}}" +
      s"   takeCards=$takeCards" +
      s"   discard=$discard" +
      s"   playedLand=$playedLand" +
      s"   graveyard{${graveyard.mkString(", ")}}" +
      s"   battlefield{${battlefield.mkString(", ")}}" +
      s"   library{${library.size}}"

  def getChanges(next:State): State = {
    State(
      changes(manaPool, next.manaPool),
      changes(hand, next.hand),
      changes(graveyard, next.graveyard),
      changes(battlefield, next.battlefield),
      changes(lands, next.lands),
      next.library,
      next.phase,
      next.takeCards,
      next.discard,
      next.numberTurn,
      next.playedLand
    )
  }
  def changes[T](first: AllSet.Type[T], second: AllSet.Type[T]): Map[T, Int] = {
    var result = AllSet.empty[T]
    for((elem, num) <- first){
      if(second.contains(elem)) {
        if ((num - second(elem) != 0))
        result += elem -> -(num - second(elem))
      } else
        result += elem -> -num
    }
    for((elem, num) <- second){
      if(!first.contains(elem))
        result += elem -> num
    }
    result
  }
}
object State {

}


