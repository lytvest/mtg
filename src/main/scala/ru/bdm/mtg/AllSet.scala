package ru.bdm.mtg

import ru.bdm.mtg.ManaPool.ManaPool

object AllSet {

  type Type[T] = Map[T, Int]


  implicit class AllSetOps[T](val map: Map[T, Int]) {

    def +~(t: T): Map[T, Int] = {
      if (map.contains(t))
        map + (t -> (map.apply(t) + 1))
      else
        map + (t -> 1)
    }

    def -~(t: T): Map[T, Int] = {
      if (map.contains(t) && map(t) > 1)
        map + (t -> (map.apply(t) - 1))
      else
        map - t
    }

    def ++~(ts: Seq[T]): Map[T, Int] =
      ts.foldRight(this) { case (t, set) => set +~ t }.map

    def --~(ts: Seq[T]): Map[T, Int] =
      ts.foldRight(this) { case (t, set) => set -~ t }.map

    def contains(t: T): Boolean =
      map.contains(t)

    def getCombinations(numberOfObjects: Int): List[Type[T]] = {
      getCombinationsRecursion(numberOfObjects, map)
    }

    private def getCombinationsRecursion(numberOfObjects: Int, set: Type[T], prev: Int = 0): List[AllSet.Type[T]] = {
      if (numberOfObjects == 0)
        return set :: Nil
      var currentSet = List.empty[AllSet.Type[T]]
      for ((mana, index) <- set.keys.zipWithIndex.drop(prev)) {
        currentSet ++= getCombinationsRecursion(numberOfObjects - 1, set -~ mana, index)
      }
      currentSet
    }
  }

  def empty[T] = Map.empty[T, Int]
}
