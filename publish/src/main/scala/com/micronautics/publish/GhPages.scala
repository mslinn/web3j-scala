package com.micronautics.publish

import java.nio.file.{Files, Path}
import com.micronautics.publish.Documenter.{file, log}
import org.slf4j.Logger
import org.slf4j.event.Level.DEBUG

/** @param deleteAfterUse Remove the temporary directory holding the GhPages content when the JVM shuts down
  * @param root directory to place the contents of GhPages */
case class GhPages(
  deleteAfterUse: Boolean = true,
  root: Path = Files.createTempDirectory(sys.props("java.io.tmpdir"))
)(implicit log: Logger) {
  if (deleteAfterUse)
    Runtime.getRuntime.addShutdownHook(new Thread {
      override def run(): Unit = deleteTempDir()
    })

  /** Create common root for Scaladoc for every SBT sub-project, and return the [[Path]] */
  lazy val apisRoot: Path = {
    val dir = root.resolve(s"latest/api/").toAbsolutePath
    dir.toFile.mkdirs()
    dir
  }

  /** Root for Scaladoc for the given SBT sub-project; does not create the [[Path]];
    * this is done by Scaladoc when it runs (todo or is it?) */
  def apiRootFor(subProject: SubProject): Path = apisRoot.resolve(subProject.name).toAbsolutePath

  protected[publish] def checkoutOrClone(gitWorkPath: Path)
                                        (implicit commandLine: CommandLine, project: Project, subProject: SubProject): Unit = {
    import commandLine.run

    val gitGitPath: Path = gitWorkPath.resolve(".git")
    dumpDirs(gitWorkPath, subProject, gitGitPath)

    if (gitGitPath.toFile.exists) {
      LogMessage(DEBUG, "gitGit exists; about to git checkout gh-pages into gitParent").display()
      run(apisRoot, "git checkout gh-pages")
    } else {
      LogMessage(DEBUG, "gitGit does not exist; about to create it in 2 steps.\n#  1) git clone the gh-pages branch into gitParent").display()
      run(apiRootFor(subProject), s"git clone -b gh-pages ${ subProject.gitHubProjectUrl }.git")

      LogMessage(DEBUG, s"  2) rename ${ subProject.name } to ${ subProject.baseDirectory.getName }").display()
      file(project.name).renameTo(file(subProject.baseDirectory.getName))
    }
  }

  /** Delete any previous Scaladoc while keeping top 3 directories (does not mess with top-level contents). */
  def deleteScaladoc(): Unit = Nuke.removeUnder(apisRoot)

  def deleteTempDir(): Unit =
    try {
      Nuke.remove(root)
    } catch {
      case e: Exception => e.printStackTrace()
    }

  @inline protected[publish] def dumpDirs(gitWorkPath: Path, subProject: SubProject, gitGit: Path): Unit =
    log.debug(s"""baseDirectory    = ${ subProject.baseDirectory.getAbsolutePath }
                 |cwd              = ${ sys.props("user.dir") }
                 |ghPages.root     = ${ root }
                 |ghPages.apiRoots = ${ apisRoot }
                 |gitWorkPath      = ${ gitWorkPath }
                 |gitGit           = $gitGit
                 |""".stripMargin)
}
