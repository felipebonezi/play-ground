import de.heikoseeberger.sbtheader.HeaderPlugin
import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

object Common extends AutoPlugin {

  import HeaderPlugin.autoImport._

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = JvmPlugin && HeaderPlugin

  val repoName = "play-ground"

  override def globalSettings: Seq[Setting[_]] =
    Seq(
      // project
      description := "A Play! Framework plugin that helps you to " +
        "develop faster with wrapped operations such as CRUD, Repositories, JWT, etc.",
      // organization
      organization := "io.github.felipebonezi",
      organizationName := "Felipe Bonezi",
      organizationHomepage := Some(url("https://about.me/felipebonezi")),
      // scala settings
      scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-encoding", "utf8"),
      javacOptions ++= Seq("-encoding", "UTF-8"),
      // legal
      licenses := Seq(
        "Apache License 2.0" ->
          url(s"https://github.com/felipebonezi/$repoName/blob/main/LICENSE")
      ),
      // on the web
      homepage := Some(url(s"https://github.com/felipebonezi/$repoName")),
      scmInfo := Some(
        ScmInfo(
          url(s"https://github.com/felipebonezi/$repoName"),
          s"scm:git:git@github.com:felipebonezi/$repoName.git"
        )
      ),
      developers += Developer(
        "contributors",
        "Contributors",
        s"https://github.com/felipebonezi/$repoName/graphs/contributors",
        url("https://github.com/felipebonezi")
      )
    )

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      headerEmptyLine := true,
      headerLicense :=
        Some(HeaderLicense.ALv2("2022", "Felipe Bonezi <https://about.me/felipebonezi>"))
    )
}
