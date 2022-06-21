name := "final-project"
version := "0.1"

scalaVersion := "3.1.2"

scalacOptions ++= Seq(
  "-new-syntax",
  "-indent"
)
//
//libraryDependencies ++= Seq(
//  // Enables logging if required by some library
//  // You can use it via https://github.com/lightbend/scala-logging
//  "ch.qos.logback" % "logback-classic" % "1.2.11",
//  "org.scalatest" %% "scalatest" % "3.2.11" % Test
//)
//
//assembly / assemblyMergeStrategy := {
//  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
//  case x =>
//    val oldStrategy = (assembly / assemblyMergeStrategy).value
//    oldStrategy(x)
//}

lazy val ello = (project in file("."))
  .settings(
    name := "Hello",
    scalaVersion := "3.1.2",
    //    libraryDependencies += "org.apache.spark" %% "spark-core" % "3.2.1"
    libraryDependencies += "com.github.haifengl" % "smile-core" % "2.6.0"
    //    libraryDependencies += "org.apache.spark" % "spark-core" % sparkVersion
  )
lazy val root = project in file(".")
libraryDependencies ++= Seq(
  "com.lihaoyi" %% "requests" % "0.7.0",
  "org.json4s" %% "json4s-jackson" % "4.0.5",
  "org.json4s" %% "json4s-jackson" % "4.0.5",
  //  "com.crealytics" %% "spark-excel" % "0.13.0",
  "org.apache.poi" % "poi" % "5.2.2",
  "org.apache.poi" % "poi-ooxml" % "5.2.2",
  "org.apache.poi" % "poi-ooxml-lite" % "5.2.2"
)

lazy val hello = (project in file("."))
  .settings(
    name := "Hello",
    libraryDependencies += "com.lihaoyi" %% "requests" % "0.7.0",
    libraryDependencies += "org.json4s" %% "json4s-jackson" % "4.0.5",
    libraryDependencies += "org.json4s" %% "json4s-jackson" % "4.0.5",
    libraryDependencies += "com.norbitltd" %% "spoiwo" % "2.2.1"
    //    libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.3",
  )
