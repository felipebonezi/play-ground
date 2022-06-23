/*
 *  Copyright (C) 2018-2022 Felipe Bonezi <https://about.me/felipebonezi>.
 *  See the LICENCE.txt file distributed with this work for additional
 *  information regarding copyright ownership.
 */

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.sbt.SbtNativePackager.Universal
import sbt.Keys.javacOptions
import sbt.Keys.publishArtifact
import sbt.Keys._
import sbt.Def
import sbt._
import sbt.plugins.JvmPlugin

object PlayCore extends AutoPlugin {
  private lazy val conf: Config = ConfigFactory.parseFile(new File("conf/reference.conf"))

  override def trigger: PluginTrigger = allRequirements
  override def requires: sbt.Plugins  = JvmPlugin

  override def projectSettings
      : Seq[Def.Setting[_ >: String with File with Task[Seq[String]] with Boolean]] = Seq(
    organization := "br.com.felipebonezi",
    name := conf.getString("play.app.name"),
    version := conf.getString("play.app.version"),
    unmanagedBase := baseDirectory.value / "routing",
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8",
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Ywarn-numeric-widen",
      "-Xfatal-warnings"
    ),
    Test / scalacOptions ++= Seq("-Yrangepos"),
    Test / javaOptions ++= Seq("-Dconfig.resource=application.test.conf"),
    autoAPIMappings := true
  )
}
