name := "mtg"

version := "0.1.3"

scalaVersion := "2.13.3"

val json4sNative = "org.json4s" %% "json4s-native" % "3.7.0-M6"

mainClass in Compile := Some("ru.bdm.mtg.TestState")
