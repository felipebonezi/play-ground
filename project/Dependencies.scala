/*
 *  Copyright (C) 2018-2022 Felipe Bonezi <https://about.me/felipebonezi>.
 *  See the LICENCE.txt file distributed with this work for additional
 *  information regarding copyright ownership.
 */

import play.sbt.PlayImport._
import sbt.Keys._
import sbt.Def
import sbt._
import sbt.plugins.JvmPlugin

object Dependencies extends AutoPlugin {
  private val jbcryptVersion = "0.4"
  private val javaJwtVersion = "4.4.0"

  override def trigger: PluginTrigger = allRequirements
  override def requires: sbt.Plugins  = JvmPlugin

  override def projectSettings
      : Seq[Def.Setting[_ >: Seq[ModuleID] with Seq[Resolver] <: Seq[Serializable]]] =
    Seq(
      libraryDependencies ++= Seq(
        javaWs,
        ehcache,
        cacheApi,
        filters,
        guice,
        "org.mindrot" % "jbcrypt"  % jbcryptVersion,
        "com.auth0"   % "java-jwt" % javaJwtVersion
      )
    )

}
