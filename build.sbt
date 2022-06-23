/*
 * Copyright (C) 2018-2022 Felipe Bonezi.
 * See the LICENCE.txt file distributed with this work for additional
 * information regarding copyright ownership.
 */

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
lazy val conf: Config = ConfigFactory.parseFile(new File("conf/reference.conf"))

maintainer := conf.getString("play.app.maintainer")

lazy val root = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)
  .enablePlugins(PlayCore, Dependencies)
  .enablePlugins(DockerPlugin, AshScriptPlugin)

sources in (Compile, doc) := Seq.empty
routesGenerator := InjectedRoutesGenerator
fork in run := true

crossScalaVersions := Seq("2.13.5")

// Coverage config.
coverageEnabled in Test := true

// Checkstyle config.
checkstyleSeverityLevel := Some(CheckstyleSeverityLevel.Info)
checkstyleConfigLocation := CheckstyleConfigLocation.File("conf/checkstyle/checkstyle-config.xml")
//(checkstyle in Compile) := (checkstyle in Compile).triggeredBy(compile in Compile).value
//(checkstyle in Test) := (checkstyle in Test).triggeredBy(compile in Test).value

// Scalastyle Configuration
import java.io.File
scalastyleFailOnError := true
scalastyleTarget := new File("target/scalastyle-report/scalastyle-result.xml")
Test / scalastyleTarget := new File("target/scalastyle-report/scalastyle-test-result.xml")

// GitHub Packages config.
githubOwner := "felipebonezi"
githubRepository := "playframework-core"
githubTokenSource :=
  TokenSource.Environment("GITHUB_TOKEN") || TokenSource.GitConfig("github.token")
