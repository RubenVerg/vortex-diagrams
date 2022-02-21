package com.rubenverg.vortex

import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.intl.DateTimeFormatOptions
import org.scalajs.dom.window

import scala.util.Random
import scala.math.*
import java.time.Clock

import Arrows.*

object VortexDiagrams extends Predef:
  val algos: Map[String, Algorithm] = Map(
    "black" -> PureAlgorithm { (a, b, mult, mod) => "black" },
    "length" -> PureAlgorithm { (a, b, mult, mod) => s"hsl(${number(a).distance(number(b)) / diameter}turn, 100%, 50%)" },
    "loops" -> LoopAlgorithm,
  )

  val canvas: html.Canvas = window.document.querySelector("canvas").asInstanceOf[html.Canvas]
  val ctx: dom.CanvasRenderingContext2D = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  val multiplierInput: html.Input = window.document.querySelector("#multiplier").asInstanceOf[html.Input]
  val moduloInput: html.Input = window.document.querySelector("#modulo").asInstanceOf[html.Input]
  val arrowsInput: html.Input = window.document.querySelector("#arrows").asInstanceOf[html.Input]
  val algorithmInput: html.Select = window.document.querySelector("#algorithm").asInstanceOf[html.Select]

  def valid: Boolean = multiplierInput.value.toIntOption.isDefined && moduloInput.value.toIntOption.isDefined && algos.contains(algorithmInput.value)

  inline val padding = 50

  def multiplier: Int = multiplierInput.value.toInt
  def modulo: Int = moduloInput.value.toInt
  def algorithm: Algorithm = algos(algorithmInput.value)
  def arrows: Boolean = arrowsInput.checked

  def radius =
    inline val wanted = 400
    if window.document.documentElement.clientHeight / 2 < wanted then
      window.document.documentElement.clientHeight / 2 - padding
    else if window.document.documentElement.clientWidth / 2 < wanted then
      window.document.documentElement.clientWidth / 2 - padding
    else wanted
  inline def diameter = radius * 2

  lazy val versionInfo: String =
    s"""Vortex Diagrams version ${BuildInfo.version}
       |Compiled on Scala.JS ${BuildInfo.scalaJsVersion} for Scala ${BuildInfo.scalaVersion} with SBT ${BuildInfo.sbtVersion}
       |${if BuildInfo.production then "Production" else "Development"} build
       |Built ${intl.dateTime.format(BuildInfo.buildTime.toJSDate)}
       |""".stripMargin

  def clear(): Unit =
    canvas.width = diameter + padding
    canvas.height = diameter + padding
    algorithm.reset()

  def width: Int = canvas.width
  def height: Int = canvas.height
  def center: Point = Point(canvas.width / 2, canvas.height / 2)
  def number(n: Int): Point = center + Point.unitCircle(2 * n * Pi / modulo).rotate(-Pi / 2) * radius

  def draw(): Unit =
    debug("drawing")

    if valid then
      clear()
      ctx.strokeStyle = "black"
      ctx.beginPath()
      ctx.arc(center.x, center.y, radius, 0, 2 * Pi, false)
      ctx.stroke()
      for ((pa, pb), n) <- Edges.all(modulo, multiplier).zipWithIndex do
        val a = number(pa)
        val b = number(pb)
        val style = algorithm(n, (multiplier * n) % modulo, modulo, multiplier)
        ctx.fillStyle = style
        ctx.strokeStyle = style
        ctx.beginPath()
        if a == b then
          ctx.arc(a.x, a.y, 2.5, 0, 2 * Pi, false)
          ctx.fill()
        else if arrows then
          ctx.arrow(a, b)
          ctx.fill()
        else
          ctx.moveTo(a.x, a.y)
          ctx.lineTo(b.x, b.y)
          ctx.stroke()
    else
      debug("invalid!")

  def main(args: Array[String]): Unit =
    info(versionInfo)
    window.document.querySelector("#build-info").textContent = versionInfo

    draw()

    multiplierInput.addEventListener("input", evt => draw())
    moduloInput.addEventListener("input", evt => draw())
    algorithmInput.addEventListener("input", evt => draw())
    arrowsInput.addEventListener("input", evt => draw())
    window.addEventListener("resize", evt => draw())
