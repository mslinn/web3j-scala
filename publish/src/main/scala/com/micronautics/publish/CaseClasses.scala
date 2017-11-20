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

  lazy val apiRoot: Path = root.resolve(s"latest/api/").toAbsolutePath

  def apiDirFor(subProject: SubProject): Path = apiRoot.resolve(subProject.name).toAbsolutePath

  /** Does not mess with top-level contents */
  def deleteScaladoc(): Unit = Nuke.removeUnder(apiRoot)
}

case class Project(
  gitHubName: String,
  override val name: String,
  version: String,
  title: String = "",
  copyright: String = "&nbsp;"
) extends SubProject(name, new File(".").getAbsoluteFile) {
  val gitHub = s"https://github.com/$gitHubName/$name"
  assert(io.Source.fromURL(gitHub).mkString.nonEmpty, s"$gitHub does not exist")
}

case class ScalaCompiler(version: String) extends AnyVal {
  override def toString: String = version
}

class SubProject(val name: String, val baseDirectory: File) {
  def gitHubProjectUrl(implicit project: Project) = s"git@github.com:${ project.gitHubName }/$name.git"

  /*def crossTarget(implicit scalaCompilerVersion: ScalaCompiler): File =
    new File(s"root/target/scala-$scalaCompilerVersion/")*/
}
