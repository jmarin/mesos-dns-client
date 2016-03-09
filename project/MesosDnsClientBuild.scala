import sbt._
import sbt.Keys._
import sbtassembly.AssemblyPlugin.autoImport._

object BuildSettings {
  val buildOrganization = "com.github.jmarin"
  val buildVersion      = "0.0.1"
  val buildScalaVersion = "2.11.7"

  val buildSettings = Defaults.coreDefaultSettings ++
    Seq(
      organization := buildOrganization,
      version      := buildVersion,
      scalaVersion := buildScalaVersion,
      scalacOptions ++= Seq(
        "-Xlint",
        "-deprecation",
        "-unchecked",
        "-feature"
      )
    )

}

object MesosDnsClientBuild extends Build {
  import BuildSettings._
  import Dependencies._

  val commonDeps = Seq(logback, scalaTest, scalaCheck)
  val akkaDeps = commonDeps ++ Seq(akkaSlf4J, akkaStream)

  lazy val mesosDnsClient = (project in file("."))
    .settings(buildSettings: _*)
    .settings(
      Seq(
        assemblyJarName in assembly := {s"${name.value}.jar"},
        assemblyMergeStrategy in assembly := {
          case "application.conf" => MergeStrategy.concat
          case x =>
            val oldStrategy = (assemblyMergeStrategy in assembly).value
            oldStrategy(x)
        }
      )
    )



}