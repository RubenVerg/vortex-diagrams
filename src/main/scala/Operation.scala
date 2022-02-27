package com.rubenverg.vortex

import Predef.*

trait Operation(val description: String):
  def apply(mod: Int)(op: Int)(n: Int): Int

  def edges(mod: Int)(op: Int): Seq[(Int, Int)] = (0 until mod).map(x => x -> this(mod)(op)(x))
  def edgesInto(mod: Int)(op: Int)(n: Int): Seq[Int] = edges(mod)(op).filter(_._2 %% mod == n %% mod).map(_._1)
  def edgesFrom(mod: Int)(op: Int)(n: Int): Seq[Int] = edges(mod)(op).filter(_._1 %% mod == n %% mod).map(_._2)
