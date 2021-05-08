/*
 * Copyright (C) 2018-2021 Justa Serviços Financeiros LTDA.
 * See the LICENCE.txt file distributed with this work for additional
 * information regarding copyright ownership.
 */

maintainer := "it@justa.com.vc"

lazy val root = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)
  .enablePlugins(PlayJusta, Dependencies)
  .enablePlugins(DockerPlugin, AshScriptPlugin)

sources in (Compile, doc) := Seq.empty
routesGenerator := InjectedRoutesGenerator
fork in run := true

crossScalaVersions := Seq("2.13.5", "2.12.13")

// Docker config.
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.sbt.packager.docker.DockerChmodType
import com.typesafe.sbt.packager.docker.DockerPermissionStrategy

lazy val conf: Config = ConfigFactory.parseFile(new File("conf/core.conf"))
dockerBaseImage := "openjdk:11.0.11-jdk"
dockerExposedPorts := Seq(9000)
dockerUpdateLatest := true
dockerChmodType := DockerChmodType.UserGroupWriteExecute
dockerPermissionStrategy := DockerPermissionStrategy.CopyChown

Docker / maintainer := "it@justa.com.vc"
Docker / packageName := conf.getString("play.app.name")
Docker / version := conf.getString("play.app.version")

// Coverage config.
coverageEnabled in Test := true

// Checkstyle config.
checkstyleSeverityLevel := Some(CheckstyleSeverityLevel.Info)
checkstyleConfigLocation := CheckstyleConfigLocation.File("conf/checkstyle/checkstyle-config.xml")
//(checkstyle in Compile) := (checkstyle in Compile).triggeredBy(compile in Compile).value
//(checkstyle in Test) := (checkstyle in Test).triggeredBy(compile in Test).value

// GitHub Packages config.
githubOwner := "justapagamentos"
githubRepository := "playframework-core"
githubTokenSource :=
  TokenSource.Environment("GITHUB_TOKEN") || TokenSource.GitConfig("github.token")
