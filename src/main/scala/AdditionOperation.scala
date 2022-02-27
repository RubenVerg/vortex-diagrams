package com.rubenverg.vortex

import Predef.*

object AdditionOperation extends Operation("Add the number and the operand"):
  def apply(mod: Int)(op: Int)(n: Int) = (n + op) %% mod

  override def edgesInto(mod: Int)(op: Int)(n: Int) = Seq((n - op) %% mod)
  override def edgesFrom(mod: Int)(op: Int)(n: Int) = Seq(this(mod)(op)(n))