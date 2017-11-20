package com.micronautics.publish

import java.io.File
import org.slf4j.Logger
import CommandLine._

/** Utility for creating combined Scaladoc for an SBT multi-project.
  * Must be run from top-level git repo directory */
object Main extends App {
  val scalaVer = scala.util.Properties.versionNumberString
  implicit val scalaCompiler: ScalaCompiler = ScalaCompiler(scalaVer.split(".").take(2).mkString("."))

  val project = Project("mslinn", "web3j-scala", "0.1.0")
  val subprojects = List("root", "demo").map(x => new SubProject(x, new File(x).getAbsoluteFile))

  val multiScaladoc = new MultiScaladoc(project)
  subprojects.foreach { multiScaladoc.commitAndDoc(_, scalaCompiler) }
}

case class ScalaCompiler(version: String) extends AnyVal {
  override def toString: String = version
}

case class Project(gitHubName: String, override val name: String, version: String)
  extends SubProject(name, new File(".").getAbsoluteFile) {
  val gitHub = s"https://github.com/$gitHubName/$name"
  assert(io.Source.fromURL(gitHub).mkString.nonEmpty, s"$gitHub does not exist")
}

class SubProject(val name: String, val baseDirectory: File) {
  def crossTarget(implicit scalaCompilerVersion: ScalaCompiler): File =
    new File(s"root/target/scala-$scalaCompilerVersion/")
}

class MultiScaladoc(project: Project) {
  implicit val log: Logger = org.slf4j.LoggerFactory.getLogger(classOf[MultiScaladoc])

  def apiDir(implicit subProject: SubProject) = new File(gitWorkFile, "latest/api/")

  def commitAndDoc(implicit subProject: SubProject, scalaCompiler: ScalaCompiler): Unit = {
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

      makeScaladoc

      pushScaladoc
    } catch {
      case e: Exception => log.error(e.getMessage)
    }
    ()
  }

  @inline def file(name: String): File = new File(name)

  @inline def gitWorkFile(implicit subProject: SubProject): File = new File(gitWorkParent, subProject.baseDirectory.getName)

  @inline def gitWorkParent(implicit subProject: SubProject): File = new File(subProject.crossTarget, "api")

  @inline def gitWorkTree(implicit subProject: SubProject): String = s"--work-tree=$gitWorkFile"

  def publishAndTag(implicit subProject: SubProject): Unit = {
    commitAndDoc
    makeScaladoc

    log.info(s"Creating git tag for v${ project.version }")
    run(s"""git tag -a ${ project.version } -m ${ project.version }""")
    run(s"""git push origin --tags""")
    ()
  }

  def makeScaladoc(implicit subProject: SubProject): Unit = {
    log.info("Creating Scaladoc")
    scaladocSetup
    makeScaladoc

    log.info("Uploading Scaladoc to GitHub Pages")
    pushScaladoc
    ()
  }

  def pushScaladoc(implicit subProject: SubProject): Unit = {
    log.info("Uploading Scaladoc to GitHub Pages")
    run(s"git $gitWorkTree add -a")
    run(s"git $gitWorkTree commit -m -")
    run(s"git $gitWorkTree push origin gh-pages")
  }

  def scaladocSetup(implicit subProject: SubProject): Unit = {
    try {
      val gitGit: File = new File(gitWorkFile, ".git")

      val gitParent: String = gitWorkParent.getAbsolutePath

      log.debug(s"# baseDirectory = ${ subProject.baseDirectory.getAbsolutePath }")
      log.debug(s"# CWD           = ${ sys.props("user.dir") }")
      log.debug(s"# gitWorkParent = ${ gitWorkParent }")
      log.debug(s"# gitParent     = $gitParent")
      log.debug(s"# gitWorkFile   = ${ gitWorkFile }")
      log.debug(s"# gitGit        = $gitGit")
      log.debug(s"# apiDir        = ${ apiDir }")

      if (gitGit.exists) {
        log.debug("# gitGit exists; about to git checkout gh-pages into gitParent")
        run(s"git checkout gh-pages", gitWorkParent)
      } else {
        log.debug("# gitGit does not exist; about to create it in 2 steps.\n#  1) git clone the gh-pages branch into gitParent")
        log.debug(s"mkdir -p ${ gitWorkParent.getAbsolutePath }")
        gitWorkParent.mkdirs() // does not fail if the directories already exist
        log.debug(s"rm -rf ${ gitWorkParent.listFiles.mkString(" ") }")
        Nuke.removeUnder(gitWorkParent) // clear out any children left over from before
        run(s"git clone -b gh-pages git@github.com:${ project.gitHubName }/${ subProject.name }.git", gitWorkParent)

        log.debug(s"#  2) rename ${ subProject.name } to ${ subProject.baseDirectory.getName }")
        log.debug(s"(cd ${ file(subProject.baseDirectory.getName) }; mv ${ subProject.name } ${ subProject.baseDirectory.getName })")
        file(project.name).renameTo(file(subProject.baseDirectory.getName))
      }
      Nuke.removeUnder(apiDir)
      run(s"git ${ gitWorkTree } add -a",      gitWorkParent)
      run(s"git ${ gitWorkTree } commit -m -", gitWorkParent)
    } catch {
      case e: Exception => log.error(e.getMessage)
    }
    ()
  }
}
