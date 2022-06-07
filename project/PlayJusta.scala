/*
 *  Copyright (C) 2018-2021 Justa Serviços Financeiros LTDA.
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

object PlayJusta extends AutoPlugin {
  private lazy val conf: Config = ConfigFactory.parseFile(new File("conf/reference.conf"))

  override def trigger: PluginTrigger = allRequirements
  override def requires: sbt.Plugins  = JvmPlugin

  override def projectSettings
      : Seq[Def.Setting[_ >: String with Task[Seq[String]] with Boolean with Task[Seq[File]]]] =
    Seq(
      organization := "br.com.felipebonezi",
      name := conf.getString("play.app.name"),
      version := conf.getString("play.app.version"),
      javacOptions in compile ++= Seq("-Xlint:deprecation"),
      javaOptions in Universal ++= Seq(
        "-J-Xmx2500m",
        "-J-Xms1500m",
        "-XX:InitialRAMPercentage=50.0",
        "-Dpidfile.path=/dev/null"
      ),
      scalacOptions ++= Seq(
        "-encoding",
        "UTF-8",
        "-deprecation",
        "-feature",
        "-unchecked",
        "-Ywarn-numeric-widen",
        "-Xfatal-warnings"
      ),
      scalacOptions in Test ++= Seq("-Yrangepos"),
      javaOptions in Test ++= Seq("-Dconfig.resource=reference.test.conf"),
      publishArtifact in (Compile, packageDoc) := false,
      sources in (Compile, doc) := Seq.empty,
      autoAPIMappings := true
    )
}
