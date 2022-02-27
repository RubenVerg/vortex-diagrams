package com.rubenverg.vortex

import org.scalatest.*
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

abstract class Spec extends funspec.AnyFunSpec, matchers.should.Matchers, OptionValues, Inside, ScalaCheckPropertyChecks, Inspectors
