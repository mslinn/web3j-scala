package com.micronautics.publish

import java.io.File
import java.nio.file.Path
import org.slf4j.event.Level._

object Documenter {
  @inline def file(name: String): File = new File(name)

  implicit val log: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger("pub")
}

class Documenter(config: Config, val subProjects: List[SubProject])
                (implicit project: Project) {
  import Documenter._

  implicit protected [publish] val commandLine: CommandLine = new CommandLine(config = config)
  import commandLine.run

  protected[publish] lazy val ghPages = GhPages(deleteAfterUse=config.deleteAfterUse)

  def publish(): Unit = {
    try {
      setup()
      gitPull()
      cautiousGitCommitPush()

      log.info(s"Making Scaladoc for ${ subProjects.size } SBT subprojects.")
      subProjects.foreach(runScaladoc)

      gitAddCommitPush(LogMessage(INFO, "Uploading Scaladoc to GitHub Pages"))
    } catch {
      case e: Exception => log.error(e.getMessage)
    }
    ()
  }


  protected[publish] def cautiousGitCommitPush(): Unit = {
    // Ensure that everything is committed
    run("git diff --name-only").replace("\n", ", ") foreach { changedFileNames =>
      if (config.autoCheckIn) {
        run("git add -A")(LogMessage(INFO, s"About to commit these changed files: $changedFileNames"), log)
        run("git commit -m -")
      } else {
        log.error(s"These changed files need to be checked in: $changedFileNames")
        System.exit(0)
      }
    }

    if (log.isDebugEnabled) {
      val stagedFileNames = run("git diff --cached --name-only").replace("\n", ", ")
      if (stagedFileNames.nonEmpty) {
        log.debug(s"These files have not yet been git pushed: ${ stagedFileNames.mkString(", ") }")
        if (!config.autoCheckIn) System.exit(0)
      }
    }

    // See https://stackoverflow.com/a/20922141/553865
    run("git push origin HEAD")(LogMessage(INFO, "About to git push to origin"), log)
  }

  protected[publish] def gitPull(): Unit =
    run("git pull")(LogMessage(INFO, "Fetching latest updates for this git repo"), log)

  protected[publish] def gitAddCommitPush(message: LogMessage = LogMessage.empty)
                                         (implicit subProject: SubProject): Unit = {
    val workTree: String = gitWorkTree
    run(s"git $workTree add -a")(message, log)
    run(s"git $workTree commit -m -")
    run(s"git $workTree push origin HEAD")
  }

  protected[publish] def gitTag(cwd: File)
                               (implicit subProject: SubProject): Unit = {
    LogMessage(INFO, s"Creating git tag for v${ project.version }").display()
    run(s"""git tag -a ${ project.version } -m ${ project.version }""")
    run(s"""git push origin --tags""")
    ()
  }

 /* protected @inline def gitWorkParent(implicit subProject: SubProject, scalaCompiler: ScalaCompiler): File =
    new File(subProject.crossTarget, "api")*/

  @inline protected[publish] def gitWorkPath(implicit subProject: SubProject): Path =
    ghPages.apiRootFor(subProject)

  @inline protected[publish] def gitWorkTree(implicit subProject: SubProject): String =
    s"--work-tree=$gitWorkPath"

  protected[publish] def runScaladoc(subProject: SubProject): Unit = {
    log.info(s"Creating Scaladoc for ${ subProject.name }.")

    val classPath: String = run("sbt", s"; project ${ subProject.name }; export runtime:fullClasspath")
    val externalDoc: String =
      s"https://github.com/${ project.gitHubName }/${ project.name }/tree/master€{FILE_PATH}.scala"
    val outputDirectory: Path = ghPages.apiRootFor(subProject)

    Scaladoc(
      classPath = classPath,
      externalDoc = externalDoc, // todo is this correct?
      footer = project.copyright,
      outputDirectory = outputDirectory,
      sourcePath = new File(subProject.baseDirectory, "src/main/scala").getAbsolutePath, // todo is this correct?
      sourceUrl = externalDoc, // todo is this correct?
      title = project.title,
      version = project.version
    ).run(subProject.baseDirectory, commandLine)
    ()
  }

  /** 1) Creates any temporary directories that might be needed
    * 2) Just checks out the top 2 directories
    * 3) Creates new 3rd level directories to hold sbt subproject Scaladoc */
  protected[publish] def setup()(implicit project: Project, subProject: SubProject): Unit = {
    try {
      ghPages.checkoutOrClone(gitWorkPath)
      ghPages.deleteScaladoc()
      gitAddCommitPush()
    } catch {
      case e: Exception => log.error(e.getMessage)
    }
    ()
  }
}
