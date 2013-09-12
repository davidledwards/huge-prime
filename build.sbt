name := "huge-prime"

organization := "com.loopfor.prime"

version := "0.2"

homepage := Some(url("https://github.com/davidledwards/huge-prime"))

licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

scmInfo := Some(ScmInfo(
  url("https://github.com/davidledwards/huge-prime"),
  "scm:git:https://github.com/davidledwards/huge-prime.git",
  Some("scm:git:https://github.com/davidledwards/huge-prime.git")
))

scalaVersion := "2.10.2"

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-feature",
  "-encoding", "UTF-8"
)

javacOptions ++= Seq(
  "-source", "1.6",
  "-target", "1.6"
)
