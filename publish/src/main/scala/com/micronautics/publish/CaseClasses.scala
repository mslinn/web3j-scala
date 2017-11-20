package com.micronautics.publish

import java.io.File
import java.nio.file.{Files, Path}
import org.slf4j.Logger

/** @param deleteAfterUse Remove the temporary directory holding the GhPages content when the JVM shuts down
  * @param root directory to place the contents of GhPages */
case class GhPages(
  deleteAfterUse: Boolean = true,
  root: Path = Files.createTempDirectory(sys.props("java.io.tmpdir"))
)(implicit log: Logger) {
  if (deleteAfterUse)
    Runtime.getRuntime.addShutdownHook(new Thread {
      override def run(): Unit =
        try {
          Nuke.remove(root)
        } catch {
          case e: Exception => e.printStackTrace()
        }
    })

  /** Common root for Scaladoc for every SBT sub-project */
  lazy val apiRoots: Path = root.resolve(s"latest/api/").toAbsolutePath

  /** Root for Scaladoc for the given SBT sub-project */
  def apiRootFor(subProject: SubProject): Path = apiRoots.resolve(subProject.name).toAbsolutePath

  /** Does not mess with top-level contents */
  def deleteScaladoc(): Unit = Nuke.removeUnder(apiRoots)
}

/** @param gitHubName GitHub account id for the git repo for this SBT project
  * @param name GitHub repo name for this SBT project
  * @param version Git release version of this SBT project
  * @param title Full name of this project; this value is used to create the Scaladoc title
  * @param copyright Scaladoc copyright info for this project */
case class Project(
  gitHubName: String,
  override val name: String,
  version: String,
  title: String = "",
  copyright: String = "&nbsp;"
) extends SubProject(name, new File(sys.props("user.dir")).getAbsoluteFile) {
  val gitHubUserUrl = s"https://github.com/$gitHubName/$name"
  require(io.Source.fromURL(gitHubUserUrl).mkString.trim.nonEmpty, s"$gitHubUserUrl does not exist")
}

object ScalaCompiler {
  lazy val fullVersion: String = scala.util.Properties.versionNumberString
  lazy val majorMinorVersion: String = fullVersion.split(".").take(2).mkString(".")
}

class SubProject(val name: String, val baseDirectory: File) {
  def gitHubProjectUrl(implicit project: Project) = s"git@github.com:${ project.gitHubName }/$name.git"

//  lazy val crossTarget: File = new File(s"target/scala-${ ScalaCompiler.majorMinorVersion }")
}
