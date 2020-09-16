package ru.bdm.mtg

case class AllSet[T](map:Map[T, Int] = Map.empty) {
  def +(t: T): AllSet[T] = {
    if (map.contains(t))
      AllSet(map + (t -> (map.apply(t) + 1)))
    else
      AllSet(map + (t -> 0))
  }
  def -(t: T): AllSet[T] = {
    if (map.contains(t) && map(t) > 0)
      AllSet(map + (t -> (map.apply(t) - 1)))
    else
      AllSet(map - t)
  }

  def ++(ts: Seq[T]): AllSet[T] =
    ts.foldRight(this){ case (t, set) => set + t}

  def --(ts: Seq[T]): AllSet[T] =
    ts.foldRight(this){ case (t, set) => set - t}

  def contains(t: T): Boolean =
    map.contains(t)
}
