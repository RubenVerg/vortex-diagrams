package com.rubenverg.vortex

import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.intl.DateTimeFormatOptions

import java.time.Instant

object Predef:
  inline def debug(what: Any): Unit =
    inline if BuildInfo.production then ()
    else js.Dynamic.global.console.debug(what.asInstanceOf[js.Any])
  inline def log(what: Any): Unit = js.Dynamic.global.console.log(what.asInstanceOf[js.Any])
  inline def info(what: Any): Unit = js.Dynamic.global.console.info(what.asInstanceOf[js.Any])
  inline def warn(what: Any): Unit = js.Dynamic.global.console.warn(what.asInstanceOf[js.Any])
  inline def error(what: Any): Unit = js.Dynamic.global.console.error(what.asInstanceOf[js.Any])

  @js.annotation.JSExportTopLevel("$", "com.rubenverg.vortex.debug")
  def $[A <: dom.HTMLElement](selector: String) = dom.window.document.querySelector(selector).asInstanceOf[A]
  @js.annotation.JSExportTopLevel("$$", "com.rubenverg.vortex.debug")
  def $$[A <: dom.HTMLElement](selector: String) = dom.window.document.querySelectorAll(selector).asInstanceOf[js.Array[A]].toSeq

  object intl:
    lazy val collator = new dom.intl.Collator()
    lazy val dateTime = new dom.intl.DateTimeFormat(js.Array(), new DateTimeFormatOptions {
      val dateStyle = "medium"
      val timeStyle = "medium"
    })
    lazy val number = new dom.intl.NumberFormat()

  extension (instant: Instant) inline def toJSDate = new js.Date(instant.toEpochMilli)
  extension (d: Double) inline def isInt = d == math.floor(d)
  extension (a: Int) inline def %%(b: Int) = if a >= 0 then a % b else b + (a % b)