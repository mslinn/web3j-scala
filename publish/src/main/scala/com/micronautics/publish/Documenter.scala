package com.micronautics.publish

import java.io.File
import java.nio.file.Path
import org.slf4j.event.Level._

object Documenter {
  @inline def file(name: String): File = new File(name)

  implicit val log: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger("pub")
}

class Documenter(val subProjects: List[SubProject])
                (implicit commandLine: CommandLine, config: Config, project: Project) {
  import Documenter._

  import commandLine.run

  protected[publish] lazy val ghPages = GhPages(deleteAfterUse=config.deleteAfterUse)

  def publish(): Unit = {
    try {
      setup()
      gitPull()
      cautiousGitCommitPush()
      writeIndex(overWriteIndex = config.overWriteIndex)

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

  protected def writeIndex(overWriteIndex: Boolean = false): Unit = {
    val ghFile = ghPages.root.toFile
    if (overWriteIndex || ghFile.list.isEmpty) {
      import java.io.PrintWriter
      val pw = new PrintWriter(new File(ghFile, "index.html" ))
      val contents: String = subProjects.map { sb =>
        s"<a href='api/latest/${ sb.name }/index.html' class='extype'><code>${ sb.name }</code></a><br/>"
      }.mkString("<p>", "\n", "</p>")

      pw.write(
        s"""
           |<!DOCTYPE html >
           |<html>
           |<head>
           |  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
           |  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
           |  <title>${ project.name } v${ project.version } API</title>
           |  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
           |  <link href="lib/index.css" media="screen" type="text/css" rel="stylesheet" />
           |  <link href="lib/template.css" media="screen" type="text/css" rel="stylesheet" />
           |</head>
           |<body>
           |<div id="content-scroll-container" style="-webkit-overflow-scrolling: touch;">
           |  <div id="content-container" style="-webkit-overflow-scrolling: touch;">
           |    <div id="subpackage-spacer">
           |      <div id="packages">
           |        $contents
           |      </div>
           |    </div>
           |  </div>
           |</div>
           |</body>
           |</html>
           |""".stripMargin
      )
      pw.close()
    }
  }

  protected[publish] def gitPull(): Unit =
    run("git pull")(LogMessage(INFO, "Fetching latest updates for this git repo"), log)

  protected[publish] def gitAddCommitPush(message: LogMessage = LogMessage.empty)
                                         (implicit subProject: SubProject): Unit = {
    run(gitWorkPath, "git add -a")(message, log)
    run(gitWorkPath, "git commit -m -")
    run(gitWorkPath, "git push origin HEAD")
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

//  @inline protected[publish] def gitWorkTree(implicit subProject: SubProject): String =
//    s"--work-tree=$gitWorkPath"

  protected[publish] def runScaladoc(subProject: SubProject): Unit = {
    log.info(s"Creating Scaladoc for ${ subProject.name }.")

    val classPath: String =
      run("sbt", "-no-colors", s"; project ${ subProject.name }; export runtime:fullClasspath")
        .split("\n")
        .last
    val sourceUrl: String = s"https://github.com/${ project.gitHubName }/${ project.name }/tree/master€{FILE_PATH}.scala"
    val outputDirectory: Path = ghPages.apiRootFor(subProject)

    Scaladoc(
      classPath = classPath,
      externalDoc = "", // todo figure this out
      footer = project.copyright,
      outputDirectory = outputDirectory,
      sourcePath = new File(subProject.baseDirectory, "src/main/scala").getAbsolutePath, // todo is this correct?
      sourceUrl = sourceUrl,
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
