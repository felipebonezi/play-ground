/*
 *  Copyright (C) 2018-2022 Felipe Bonezi <https://about.me/felipebonezi>.
 *  See the LICENCE.txt file distributed with this work for additional
 *  information regarding copyright ownership.
 */

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
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
    name := conf.getString("play.app.name"),
    unmanagedBase := baseDirectory.value / "routing",
    Test / scalacOptions ++= Seq("-Yrangepos"),
    Test / javaOptions ++= Seq("-Dconfig.resource=application.test.conf"),
    autoAPIMappings := true
  )
}
