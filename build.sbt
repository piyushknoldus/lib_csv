ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "lib-csv-v1"
  )

val Slf4jVersion = "2.0.5"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % Slf4jVersion,
  "org.slf4j" % "slf4j-simple" % Slf4jVersion,
  "commons-io" % "commons-io" % "2.11.0", // Do not update to "20030203.000550"
  "com.github.tototoshi" %% "scala-csv" % "1.3.10",
  "org.scalatest" %% "scalatest" % "3.2.15" % Test
)

scalafmtOnCompile := true