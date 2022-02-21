import org.scalajs.linker.interface.{ ModuleSplitStyle, Report }
import java.time.Instant

enablePlugins(ScalaJSPlugin)

val production = settingKey[Boolean]("Whether this is a production build.")
val fast = taskKey[Attributed[Report]]("")
val full = taskKey[Attributed[Report]]("")

Compile / sourceGenerators += Def.task {
  val file = (Compile / sourceManaged).value / "BuildInfo.scala"
  println(s"Writing build information to $file")
  IO.write(file,
    s"""package com.rubenverg.vortex
       |
       |import java.time.Instant
       |
       |object BuildInfo:
       |  inline val production = ${production.value}
       |  inline val version = \"\"\"${version.value}\"\"\"
       |  inline val scalaVersion = \"\"\"${scalaVersion.value}\"\"\"
       |  inline val sbtVersion = \"\"\"${sbtVersion.value}\"\"\"
       |  inline val scalaJsVersion = \"\"\"$scalaJSVersion\"\"\"
       |  val buildTime = Instant.ofEpochSecond(${Instant.now().getEpochSecond})
       |""".stripMargin)
  Seq(file)
}.taskValue

fast := {
  val prod = production.value
  production := false
  val result = (Compile / fastLinkJS).value
  production := prod
  result
}

full := {
  val prod = production.value
  production := true
  val result = (Compile / fullLinkJS).value
  production := prod
  result
}

name := "vortex-diagrams"
idePackagePrefix := Some("com.rubenverg.vortex")
libraryDependencies ++= Seq(
  "org.scalatest" %%% "scalatest" % "3.2.11" % Test,
  "org.scalacheck" %%% "scalacheck" % "1.15.4" % Test,
  "org.scala-js" %%% "scalajs-dom" % "2.1.0",
  "com.lihaoyi" %%% "scalatags" % "0.11.1",
  "io.github.cquiroz" %%% "scala-java-time" % "2.4.0-M1",
)
version := "1.2.0"
scalaVersion := "3.1.1"
developers ++= List(Developer("rubenverg", "Ruben Vergani", "me@rubenverg.com", new java.net.URL("https://rubenverg.com")))
production := false

scalaJSUseMainModuleInitializer := true
scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule).withModuleSplitStyle(ModuleSplitStyle.SmallestModules) }