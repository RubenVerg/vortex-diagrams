package com.rubenverg.vortex

import Predef.*

object PowerOperation extends Operation("Raise the number to the operand-th power"):
  def apply(mod: Int)(op: Int)(n: Int) = math.pow(n, op).toInt %% mod

  // override def edgesInto(mod: Int)(op: Int)(n: Int)
  override def edgesFrom(mod: Int)(op: Int)(n: Int) = Seq(this(mod)(op)(n))