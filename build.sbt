name := "play-core"
version := "0.1"
organization := "br.com.ehureka"

scalacOptions ++= Seq("-feature")
javacOptions in compile ++= Seq("-Xlint:deprecation")
publishArtifact in (Compile, packageDoc) := false
resolvers += Resolver.mavenLocal

lazy val core = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  javaWs,
  ehcache,
  cacheApi,
  filters,
  guice,
  "org.mindrot" % "jbcrypt" % "0.4",
  "com.auth0" % "java-jwt" % "3.3.0"
)

