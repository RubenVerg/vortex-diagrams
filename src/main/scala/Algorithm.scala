package com.rubenverg.vortex

trait Algorithm:
  def apply(a: Int, b: Int, mod: Int, mult: Int): String
  def reset(): Unit
