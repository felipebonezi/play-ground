import com.typesafe.config.ConfigFactory

val conf = ConfigFactory.parseFile(new File("conf/eds-version.conf")).resolve()

name := "play-core"
version := "0.1"
organization := "br.com.ehureka"

scalacOptions ++= Seq("-feature")
javacOptions in compile ++= Seq("-Xlint:deprecation")
publishArtifact in (Compile, packageDoc) := false
resolvers += Resolver.mavenLocal

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  ehcache,
  cacheApi,
  filters,
  guice
)

publishTo := Some(
  "My resolver" at "https://mycompany.com/repo"
)

credentials += Credentials(
  "Repo", "https://mycompany.com/repo", "admin", "admin123"
)
