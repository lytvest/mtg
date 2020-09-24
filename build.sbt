name := "mtg"

version := "0.1"

scalaVersion := "2.13.3"

lazy val root = (project in file("."))
  .aggregate(neurons, mtg, server)

lazy val neurons = RootProject(uri("https://github.com/bdm2505/neurons"))

lazy val mtg = project
  .dependsOn(neurons)

lazy val server = project
  .dependsOn(mtg)