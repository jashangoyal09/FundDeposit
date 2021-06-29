name := "FundDeposit"

version := "0.1"

scalaVersion := "2.12.12"

val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
).map(_ % circeVersion) :+ "org.scalatest" %% "scalatest" % "3.0.8" % Test :+
  "org.mockito" % "mockito-core" % "2.8.47" % "test" :+
  "com.typesafe" % "config" % "1.4.1" :+
  "ch.qos.logback" % "logback-classic" % "1.2.3"