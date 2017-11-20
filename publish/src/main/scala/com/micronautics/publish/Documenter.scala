package com.micronautics.publish

import java.io.File
import java.nio.file.Path
import com.micronautics.publish.CommandLine.run
import org.slf4j.event.Level._

object Documenter {
  @inline def file(name: String): File = new File(name)

  implicit val log: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger("pub")
}

class Documenter(implicit project: Project) {
  import Documenter._

  lazy val ghPages = GhPages()

  def publishFor(subprojects: List[SubProject]): Unit = {
    try {
      setup()
      run("git pull")(LogMessage(INFO, "Fetching latest updates for this git repo"), log)

      run("git diff --name-only").replace("\n", ", ") foreach { changedFileNames =>
        run("git add -A")(LogMessage(INFO, s"About to commit these changed files: $changedFileNames"), log)
        run("git commit -m -")
      }

      run("git diff --cached --name-only").replace("\n", ", ") foreach { stagedFileNames =>
        log.debug(s"About to push these staged files: $stagedFileNames")
      }

      // See https://stackoverflow.com/a/20922141/553865
      run("git push origin HEAD")(LogMessage(INFO, "About to git push to origin"), log)
      subprojects.foreach(sp => makeScaladoc(sp.baseDirectory))
      gitPush()
    } catch {
      case e: Exception => log.error(e.getMessage)
    }
    ()
  }


  @inline protected def dumpDirs(subProject: SubProject, gitGit: Path): Unit =
    log.debug(s"""baseDirectory    = ${ subProject.baseDirectory.getAbsolutePath }
                 |cwd              = ${ sys.props("user.dir") }
                 |ghPages.root     = ${ ghPages.root }
                 |ghPages.apiRoots = ${ ghPages.apiRoots }
                 |gitWorkPath      = ${ gitWorkPath }
                 |gitGit           = $gitGit
                 |""".stripMargin)

  protected def gitPush()(implicit subProject: SubProject): Unit = {
    val workTree: String = gitWorkTree
    run(s"git $workTree add -a")(LogMessage(INFO, "Uploading Scaladoc to GitHub Pages"), log)
    run(s"git $workTree commit -m -")
    run(s"git $workTree push origin -u gh-pages")
  }

 /* protected @inline def gitWorkParent(implicit subProject: SubProject, scalaCompiler: ScalaCompiler): File =
    new File(subProject.crossTarget, "api")*/

  @inline protected def gitWorkPath(implicit subProject: SubProject): Path =
    ghPages.apiRootFor(subProject)

  @inline protected def gitWorkTree(implicit subProject: SubProject): String =
    s"--work-tree=$gitWorkPath"

  protected def makeScaladoc(cwd: File)
                            (implicit subProject: SubProject): Unit = {
    log.info("Creating Scaladoc")
    setup()

    val classPath: String = run("sbt", s"; project ${ subProject.name }; export runtime:fullClasspath")
    val externalDoc: String =
      s"https://github.com/${ project.gitHubName }/${ project.name }/tree/master€{FILE_PATH}.scala"
    val outputDirectory: Path = ghPages.apiRootFor(subProject)

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

  /** 1) Creates any temporary directories that might be needed
    * 2) Just checks out the top 2 directories
    * 3) Creates new 3rd level directories to hold sbt subproject Scaladoc */
  protected def setup()(implicit project: Project, subProject: SubProject): Unit = {
    try {
      val gitGitPath: Path = gitWorkPath.resolve(".git")
      dumpDirs(subProject, gitGitPath)

      if (gitGitPath.toFile.exists) {
        run(ghPages.apiRoots, "git checkout gh-pages")(LogMessage(DEBUG, "gitGit exists; about to git checkout gh-pages into gitParent"), log)
      } else {
        val lm = LogMessage(DEBUG, "gitGit does not exist; about to create it in 2 steps.\n#  1) git clone the gh-pages branch into gitParent")
        run(ghPages.apiRootFor(subProject), s"git clone -b gh-pages ${ subProject.gitHubProjectUrl }.git")(lm, log)

        LogMessage(DEBUG, s"  2) rename ${ subProject.name } to ${ subProject.baseDirectory.getName }").display()
        file(project.name).renameTo(file(subProject.baseDirectory.getName))
      }

      //Nuke.removeUnder(ghPages.root) // just making sure

      run(ghPages.root, s"git ${ gitWorkTree } add -a")
      run(ghPages.root, s"git ${ gitWorkTree } commit -m -")
      run(ghPages.root, "git push origin HEAD")
    } catch {
      case e: Exception => log.error(e.getMessage)
    }
    ()
  }

  protected def tag(cwd: File)
                   (implicit subProject: SubProject): Unit = {
    run(s"""git tag -a ${ project.version } -m ${ project.version }""")(LogMessage(INFO, s"Creating git tag for v${ project.version }"), log)
    run(s"""git push origin --tags""")
    ()
  }
}
