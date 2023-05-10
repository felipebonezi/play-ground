/*
 * Copyright (C) 2018-2022 Felipe Bonezi <https://about.me/felipebonezi>.
 * See the LICENCE.txt file distributed with this work for additional
 * information regarding copyright ownership.
 */

// Play Framework
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.19")

// Ebean plugin for play framework
addSbtPlugin("com.typesafe.play" % "sbt-play-ebean" % "6.2.0")

// Sbt Header check.
addSbtPlugin("de.heikoseeberger" % "sbt-header" % "5.9.0")

// Sbt-CI plugin
addSbtPlugin("com.github.sbt" % "sbt-ci-release" % "1.5.12")

// Visualize your project's dependencies.
// `dependencyTree`: Shows an ASCII tree representation of the project's dependencies
// `dependencyBrowseTree`: Opens a browser window with a visualization of
// the dependency tree (courtesy of jstree).
// See more: https://github.com/sbt/sbt-dependency-graph
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.10.0-RC1")

// Code formatter for Scala.
// See more: https://github.com/scalameta/sbt-scalafmt
// Oficial Website: https://scalameta.org/scalafmt/
addSbtPlugin("org.scalameta"   % "sbt-scalafmt"          % "2.5.0")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")

// Test Coverage plugin.
// sbt-scoverage is a plugin for SBT that integrates the scoverage code coverage library.
// See more: https://github.com/scoverage/sbt-scoverage
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.0.0")

// Java Checkstyle plugin.
// See more: https://github.com/etsy/sbt-checkstyle-plugin
addSbtPlugin("com.etsy"                       % "sbt-checkstyle-plugin" % "3.1.1")
dependencyOverrides += "com.puppycrawl.tools" % "checkstyle"            % "10.11.0"
