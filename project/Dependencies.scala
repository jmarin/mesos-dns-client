import sbt._

object Dependencies {
  val repos = Seq(
    "Local Maven Repo"  at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    "Typesafe Repo"     at "http://repo.typesafe.com/typesafe/releases/",
    "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases"
  )


  val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest % "it, test"
  val scalaCheck = "org.scalacheck" %% "scalacheck" % Version.scalaCheck % "it, test"
  val logback = "ch.qos.logback" % "logback-classic" % Version.logback
  val akkaSlf4J = "com.typesafe.akka" %% "akka-slf4j" % Version.akka
  val akkaStream = "com.typesafe.akka" %% "akka-stream" % Version.akka
  val akkaHttp = "com.typesafe.akka" %% "akka-http-experimental" % Version.akka
  val akkaHttpJson = "com.typesafe.akka" %% "akka-http-spray-json-experimental" % Version.akka


}