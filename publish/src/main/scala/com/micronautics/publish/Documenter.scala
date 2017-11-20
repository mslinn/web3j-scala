package com.micronautics.publish

import java.io.File
import java.nio.file.Path
import com.micronautics.publish.CommandLine.run
import org.slf4j.event.Level._
import LogMessage.{empty => emptyMessage}

object Documenter {
  implicit val log: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger("pub")
}

class Documenter(implicit project: Project) {
  import Documenter._

  lazy val ghPages = GhPages()

  def publishFor(subprojects: List[SubProject])
                (implicit scalaCompiler: ScalaCompiler): Unit = {
    try {
      setup()

      run("git pull")(LogMessage(INFO, "Fetching latest updates for this git repo"))

      val changedFileNames: String = run("git diff --name-only")(emptyMessage).replace("\n", ", ")
      if (changedFileNames.nonEmpty) {
        run("git add -A")(LogMessage(INFO, s"About to commit these changed files: $changedFileNames"))
        run("git commit -m -")(emptyMessage)
      }

      /*val stagedFileNames = "git diff --cached --name-only".!!.trim.replace("\n", ", ")
      if (stagedFileNames.nonEmpty) {
        log.info(s"About to push these staged files: $stagedFileNames")
      }*/

      run("git push origin HEAD")(LogMessage(INFO, "About to git push to origin"))  // See https://stackoverflow.com/a/20922141/553865

      subprojects.foreach(x => makeScaladoc(x.baseDirectory))

      gitPush()
    } catch {
      case e: Exception => log.error(e.getMessage)
    }
    ()
  }

  @inline def file(name: String): File = new File(name)

  @inline def gitWorkPath(implicit subProject: SubProject, scalaCompiler: ScalaCompiler): Path =
    ghPages.apiDirFor(subProject)

 /* @inline def gitWorkParent(implicit subProject: SubProject, scalaCompiler: ScalaCompiler): File =
    new File(subProject.crossTarget, "api")*/

  @inline def gitWorkTree(implicit subProject: SubProject, scalaCompiler: ScalaCompiler): String =
    s"--work-tree=$gitWorkPath"

  def tag(cwd: File)
         (implicit subProject: SubProject): Unit = {
    run(s"""git tag -a ${ project.version } -m ${ project.version }""")(LogMessage(INFO, s"Creating git tag for v${ project.version }"))
    run(s"""git push origin --tags""")(emptyMessage)
    ()
  }

  def makeScaladoc(cwd: File)
                  (implicit subProject: SubProject, scalaCompiler: ScalaCompiler): Unit = {
    log.info("Creating Scaladoc")
    setup()

    val classPath: String = run( "sbt", s"; project ${ subProject.name }; export runtime:fullClasspath")(emptyMessage)
    val externalDoc: String = s"https://github.com/${ project.gitHubName }/${ project.name }/tree/master€{FILE_PATH}.scala"
    val outputDirectory: String = ghPages.apiDirFor(subProject).toString
    Scaladoc(
      classPath = classPath,
      externalDoc = externalDoc, // todo is this correct?
      footer = project.copyright,
      outputDirectory = outputDirectory,
      sourcePath = new File(subProject.baseDirectory, "src/main/scala").getAbsolutePath,
      sourceUrl = externalDoc, // todo is this correct?
      title = project.title,
      version = project.version
    ).run(cwd)

    gitPush
    ()
  }

  def gitPush()(implicit subProject: SubProject, scalaCompiler: ScalaCompiler): Unit = {
    run(s"git $gitWorkTree add -a")(LogMessage(INFO, "Uploading Scaladoc to GitHub Pages"))
    run(s"git $gitWorkTree commit -m -")(emptyMessage)
    run(s"git $gitWorkTree push origin gh-pages")(emptyMessage)
  }

  def setup()(implicit project: Project, subProject: SubProject, scalaCompiler: ScalaCompiler): Unit = {
    try {
      val gitGit: File = gitWorkPath.resolve(".git").toFile

      log.debug(s"baseDirectory   = ${ subProject.baseDirectory.getAbsolutePath }")
      log.debug(s"CWD             = ${ sys.props("user.dir") }")
      log.debug(s"ghPages.root    = ${ ghPages.root }")
      log.debug(s"ghPages.apiRoot = ${ ghPages.apiRoot }")
      log.debug(s"gitWorkFile     = ${ gitWorkPath }")
      log.debug(s"gitGit          = $gitGit")

      if (gitGit.exists) {
        run(ghPages.apiRoot, "git checkout gh-pages")(LogMessage(DEBUG, "gitGit exists; about to git checkout gh-pages into gitParent"))
      } else {
        val lm = LogMessage(DEBUG, "gitGit does not exist; about to create it in 2 steps.\n#  1) git clone the gh-pages branch into gitParent")
        run(ghPages.apiDirFor(subProject), s"git clone -b gh-pages ${ subProject.gitHubProjectUrl }.git")(lm)

        LogMessage(DEBUG, s"  2) rename ${ subProject.name } to ${ subProject.baseDirectory.getName }").log()
        file(project.name).renameTo(file(subProject.baseDirectory.getName))
      }
      //Nuke.removeUnder(ghPages.root) // just making sure
      run(ghPages.root, s"git ${ gitWorkTree } add -a")(emptyMessage)
      run(ghPages.root, s"git ${ gitWorkTree } commit -m -")(emptyMessage)
      run(ghPages.root, "git push origin HEAD")(emptyMessage)
    } catch {
      case e: Exception => log.error(e.getMessage)
    }
    ()
  }
}
