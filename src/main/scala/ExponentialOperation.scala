package com.rubenverg.vortex

import Predef.*

object ExponentialOperation extends Operation("Raise the operand to the n-th power"):
  def apply(mod: Int)(op: Int)(n: Int) = math.pow(op, n).toInt %% mod

  // override def edgesInto(mod: Int)(op: Int)(n: Int)
  override def edgesFrom(mod: Int)(op: Int)(n: Int) = Seq(this(mod)(op)(n))