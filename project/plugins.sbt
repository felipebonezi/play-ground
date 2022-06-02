/*
 * Copyright (C) 2018-2021 Justa Serviços Financeiros LTDA.
 * See the LICENCE.txt file distributed with this work for additional
 * information regarding copyright ownership.
 */

// Play Framework
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.8")

// Ebean plugin for play framework
addSbtPlugin("com.typesafe.sbt" % "sbt-play-ebean" % "6.0.0")

// Visualize your project's dependencies.
// `dependencyTree`: Shows an ASCII tree representation of the project's dependencies
// `dependencyBrowseTree`: Opens a browser window with a visualization of
// the dependency tree (courtesy of jstree).
// See more: https://github.com/sbt/sbt-dependency-graph
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.10.0-RC1")

// Code formatter for Scala.
// See more: https://github.com/scalameta/sbt-scalafmt
// Oficial Website: https://scalameta.org/scalafmt/
addSbtPlugin("org.scalameta"   % "sbt-scalafmt"          % "2.4.2")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")

// Test Coverage plugin.
// sbt-scoverage is a plugin for SBT that integrates the scoverage code coverage library.
// See more: https://github.com/scoverage/sbt-scoverage
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.7.3")

// Java Checkstyle plugin.
// See more: https://github.com/etsy/sbt-checkstyle-plugin
addSbtPlugin("com.etsy"                       % "sbt-checkstyle-plugin" % "3.1.1")
dependencyOverrides += "com.puppycrawl.tools" % "checkstyle"            % "10.3"

// GitHub Packages plugin.
// See more: https://dev.to/gjuoun/publish-your-scala-library-to-github-packages-4p80
addSbtPlugin("com.codecommit" % "sbt-github-packages" % "0.5.2")
