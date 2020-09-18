package ru.bdm.mtg

import ru.bdm.mtg.AllSet.AllSetOps


case class State(
                  manaPool: ManaPool.Type = AllSet.empty[Char],
                  hand: AllSet.Type[Card] = AllSet.empty[Card],
                  graveyard: AllSet.Type[Card] = AllSet.empty[Card],
                  battlefield: AllSet.Type[Card] = AllSet.empty[Card],
                  lands: AllSet.Type[Land] = AllSet.empty[Land],
                  library: Seq[Card] = Seq.empty[Card]
                ){
  override def toString: String =
    s"   mana{${manaPool.mkString(", ")}}" +
      s"   hand{${hand.mkString(", ")}}" +
      s"   lands{${lands.mkString(", ")}}" +
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
      next.library
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


