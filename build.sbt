name := "mtg"

version := "0.1.3"

scalaVersion := "2.13.3"

val json4sNative = "org.json4s" %% "json4s-native" % "3.7.0-M6"

mainClass in Compile := Some("ru.bdm.mtg.TestState")

libraryDependencies += "org.deeplearning4j" % "deeplearning4j-core" % "1.0.0-beta7"

libraryDependencies += "org.nd4j" % "nd4j-native-platform" % "1.0.0-beta7"

libraryDependencies += "org.deeplearning4j" % "rl4j-api" % "1.0.0-beta7"

libraryDependencies += "org.deeplearning4j" % "rl4j-core" % "1.0.0-beta7"

// https://mvnrepository.com/artifact/org.bytedeco/ale-platform
libraryDependencies += "org.bytedeco" % "ale-platform" % "0.6.1-1.5.3"



