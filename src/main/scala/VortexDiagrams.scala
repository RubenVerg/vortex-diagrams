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
import Predef.*

object VortexDiagrams:
  val colorings: Map[String, Coloring] = Map(
    "black" -> PureColoring("""All lines are black, no coloring is applied""") { (a, b, mod, op, operation) => "black" },
    "length" -> PureColoring("""Lines are colored with hue based on their length in proportion to the circle diameter""") { (a, b, mod, op, operation) => s"hsl(${number(a).distance(number(b)) / diameter}turn, 100%, 50%)" },
    "loops" -> LoopColoring,
  )

  val operations: Map[String, Operation] = Map(
    "multiply" -> MultiplicationOperation,
    "power" -> PowerOperation,
    "exp" -> ExponentialOperation,
    "add" -> AdditionOperation,
  )

  val canvas: html.Canvas = $("canvas")
  val ctx: dom.CanvasRenderingContext2D = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  val operationInput: html.Select = $("#operation")
  val operandInput: html.Input = $("#operand")
  val moduloInput: html.Input = $("#modulo")
  val arrowsInput: html.Input = $("#arrows")
  val labelsInput: html.Input = $("#labels")
  val coloringInput: html.Select = $("#coloring")

  val operationHelp: html.Paragraph = $("#operation-help")
  val coloringHelp: html.Paragraph = $("#coloring-help")

  val downloadButton: html.Anchor = $("#download-button").asInstanceOf[html.Anchor]

  def valid: Boolean =
    debug(s"valid? operand = ${operandInput.value}, modulo = ${moduloInput.value}, coloring = ${coloringInput.value} in ${colorings.keys}, operation = ${operationInput.value} in ${operations.keys}")
    operandInput.value.toIntOption.isDefined && moduloInput.value.toIntOption.isDefined && colorings.contains(coloringInput.value) && operations.contains(operationInput.value)

  inline val padding = 50

  inline def operation: Operation = operations(operationInput.value)
  inline def operand: Int = operandInput.value.toInt
  inline def modulo: Int = moduloInput.value.toInt
  inline def coloring: Coloring = colorings(coloringInput.value)
  inline def arrows: Boolean = arrowsInput.checked
  inline def labels: Boolean = labelsInput.checked

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
    coloring.reset()

  def width: Int = canvas.width
  def height: Int = canvas.height
  def center: Point = Point(canvas.width / 2, canvas.height / 2)
  def number(n: Int): Point = center + Point.unitCircle(2 * n * Pi / modulo).rotate(-Pi / 2) * radius
  def numberText(n: Int): (String, Point) =
    val text = n.toString
    val point = center + Point.unitCircle(2 * n * Pi / modulo).rotate(-Pi / 2) * (radius + padding / 4)
    (text, point)

  def updateLabels(): Unit =
    coloringHelp.textContent = coloring.description
    operationHelp.textContent = operation.description

  def draw(): Unit =
    debug("drawing")
    clear()
    ctx.strokeStyle = "black"
    ctx.beginPath()
    ctx.arc(center.x, center.y, radius, 0, 2 * Pi, false)
    ctx.stroke()
    ctx.textAlign = "center"
    ctx.textBaseline = "middle"
    if labels then
      for
        i <- 0 until modulo
        (text, Point(x, y)) = numberText(i)
      do
        ctx.fillText(text, x, y)
    for ((pa, pb), n) <- operation.edges(modulo)(operand).zipWithIndex do
      val a = number(pa)
      val b = number(pb)
      val style = coloring(n, operation(modulo)(operand)(n) %% modulo, modulo, operand, operation)
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
    // Adapted from https://stackoverflow.com/a/44487883, shared under CC-BY-SA 3.0
    downloadButton.setAttribute("download", s"Vortex Diagram (modulo = $modulo, operation = ${operationInput.value} $operand, ${if arrows then "arrows, " else ""}${coloringInput.value}).png")
    downloadButton.href = canvas.toDataURL("image/png").replace("image/png", "image/octet-stream")

  def update(): Unit =
    if valid then
      updateLabels()
      draw()
    else
      warn("invalid configuration!")

  def main(args: Array[String]): Unit =
    info(versionInfo)
    window.document.querySelector("#build-info").textContent = versionInfo

    update()

    operationInput.addEventListener("input", evt => update())
    operandInput.addEventListener("input", evt => update())
    moduloInput.addEventListener("input", evt => update())
    coloringInput.addEventListener("input", evt => update())
    arrowsInput.addEventListener("input", evt => update())
    labelsInput.addEventListener("input", evt => update())
    window.addEventListener("resize", evt => update())
