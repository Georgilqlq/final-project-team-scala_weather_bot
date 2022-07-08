name := "final-project"
version := "0.1"

scalaVersion := "3.1.2"

scalacOptions ++= Seq(
  "-new-syntax",
  "-indent"
)

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
  "org.apache.poi" % "poi-ooxml-lite" % "5.2.2",
  //  "org.apache.logging.log4j" %% "log4j-api-scala" % "12.1",
  "org.apache.logging.log4j" % "log4j-api" % "2.17.2",
  "org.apache.logging.log4j" % "log4j-core" % "2.17.2" % Runtime,
  //  "org.apache.logging.log4j" %% "log4j-api-scala" % "11.0",
  "org.scalatest" %% "scalatest" % "3.2.12" % Test,
  "org.scalatestplus" %% "mockito-3-4" % "3.2.10.0" % Test,
  "org.springframework.scala" %% "spring-scala" % "1.0.0.RC1" % Test
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
