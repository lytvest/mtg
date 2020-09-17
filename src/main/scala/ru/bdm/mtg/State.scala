package ru.bdm.mtg


case class State(
                  manaPool: ManaPool.Type = AllSet.empty[Char],
                  hand: AllSet.Type[Card] = AllSet.empty[Card],
                  graveyard: AllSet.Type[Card] = AllSet.empty[Card],
                  battlefield: AllSet.Type[Card] = AllSet.empty[Card],
                  lands: AllSet.Type[Land] = AllSet.empty[Land],
                  library: Seq[Card] = Seq.empty[Card]
                )


