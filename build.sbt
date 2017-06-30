import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.11.11",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Hello",
    libraryDependencies += "org.apache.spark" % "spark-mllib_2.11" % "2.1.1" ,
    libraryDependencies += scalaTest % Test
  )
