package com.micronautics.publish

import java.io.File
import java.nio.file.Path
import com.micronautics.publish.CommandLine.run

object Documenter {
  implicit val log: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger("pub")
}

class Documenter(implicit project: Project) {
  import Documenter._

  lazy val ghPages = GhPages()

  def commitAndDoc(cwd: File)
                  (implicit subProject: SubProject, scalaCompiler: ScalaCompiler): Unit = {
    try {
      scaladocSetup // is this already called elsewhere?

      log.info("Fetching latest updates for this git repo")
      run("git pull")

      val changedFileNames: String = run("git diff --name-only").replace("\n", ", ")
      if (changedFileNames.nonEmpty) {
        log.info(s"About to commit these changed files: $changedFileNames")
        run("git add -A")
        run("git commit -m -")
      }

      /*val stagedFileNames = "git diff --cached --name-only".!!.trim.replace("\n", ", ")
      if (stagedFileNames.nonEmpty) {
        log.info(s"About to push these staged files: $stagedFileNames")
      }*/

      log.info("About to git push to origin")
      run("git push origin HEAD")  // See https://stackoverflow.com/a/20922141/553865

      makeScaladoc(cwd)

      pushScaladoc
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

  def publishAndTag(cwd: File)
                   (implicit subProject: SubProject, scalaCompiler: ScalaCompiler): Unit = {
    commitAndDoc(cwd)

    log.info(s"Creating git tag for v${ project.version }")
    run(s"""git tag -a ${ project.version } -m ${ project.version }""")
    run(s"""git push origin --tags""")
    ()
  }

  def makeScaladoc(cwd: File)
                  (implicit subProject: SubProject, scalaCompiler: ScalaCompiler): Unit = {
    log.info("Creating Scaladoc")
    scaladocSetup

    val classPath: String = run( "sbt", s"; project ${ subProject.name }; export runtime:fullClasspath")
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

    log.info("Uploading Scaladoc to GitHub Pages")
    pushScaladoc
    ()
  }

  def pushScaladoc(implicit subProject: SubProject, scalaCompiler: ScalaCompiler): Unit = {
    log.info("Uploading Scaladoc to GitHub Pages")
    run(s"git $gitWorkTree add -a")
    run(s"git $gitWorkTree commit -m -")
    run(s"git $gitWorkTree push origin gh-pages")
  }

  def scaladocSetup(implicit project: Project, subProject: SubProject, scalaCompiler: ScalaCompiler): Unit = {
    try {
      val gitGit: File = gitWorkPath.resolve(".git").toFile

      log.debug(s"baseDirectory   = ${ subProject.baseDirectory.getAbsolutePath }")
      log.debug(s"CWD             = ${ sys.props("user.dir") }")
      log.debug(s"ghPages.root    = ${ ghPages.root }")
      log.debug(s"ghPages.apiRoot = ${ ghPages.apiRoot }")
      log.debug(s"gitWorkFile     = ${ gitWorkPath }")
      log.debug(s"gitGit          = $gitGit")

      if (gitGit.exists) {
        log.debug("# gitGit exists; about to git checkout gh-pages into gitParent")
        run(ghPages.apiRoot, "git checkout gh-pages")
      } else {
        log.debug("# gitGit does not exist; about to create it in 2 steps.\n#  1) git clone the gh-pages branch into gitParent")
        log.debug(s"mkdir -p ${ ghPages.apiRoot }")
        run(ghPages.apiDirFor(subProject), s"git clone -b gh-pages ${ subProject.gitHubProjectUrl }.git")

        log.debug(s"#  2) rename ${ subProject.name } to ${ subProject.baseDirectory.getName }")
        log.debug(s"(cd ${ file(subProject.baseDirectory.getName) }; mv ${ subProject.name } ${ subProject.baseDirectory.getName })")
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
}
