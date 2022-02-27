package com.rubenverg.vortex

import Predef.*

object MultiplicationOperation extends Operation("Multiply the number with the operand"):
  def apply(mod: Int)(op: Int)(n: Int) = (n * op) %% mod

  override def edgesInto(mod: Int)(op: Int)(n: Int) = (math.min(0, op) until math.max(0, op)).map(s => (n + mod * s) / op.toDouble).filter(_.isInt).map(_.toInt)
  override def edgesFrom(mod: Int)(op: Int)(n: Int) = Seq(this(mod)(op)(n))