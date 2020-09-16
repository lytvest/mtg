package ru.bdm.mtg


case class State(
             graveyard: AllSet[Card] = AllSet[Card](),
             hand: AllSet[Card] = AllSet[Card](),
             battlefield: AllSet[Card] = AllSet[Card](),
             library: AllSet[Card] = AllSet[Card](),
             lands: AllSet[Card] = AllSet[Card](),
             manaPool: AllSet[Char] = AllSet[Char](),
             discard: Int = 0
           ) {


}


