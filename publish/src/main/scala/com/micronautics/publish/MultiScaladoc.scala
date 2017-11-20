package com.micronautics.publish

import java.io.File
import java.nio.file.Paths
import com.micronautics.publish.CommandLine.run

object MultiScaladoc {
  implicit val log: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(classOf[MultiScaladoc])
}

class MultiScaladoc(project: Project) {
  import MultiScaladoc._

  def apiDir(implicit subProject: SubProject, scalaCompiler: ScalaCompiler) = new File(gitWorkFile, "latest/api/")

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

  @inline def gitWorkFile(implicit subProject: SubProject, scalaCompiler: ScalaCompiler): File =
    new File(gitWorkParent, subProject.baseDirectory.getName)

  @inline def gitWorkParent(implicit subProject: SubProject, scalaCompiler: ScalaCompiler): File =
    new File(subProject.crossTarget, "api")

  @inline def gitWorkTree(implicit subProject: SubProject, scalaCompiler: ScalaCompiler): String =
    s"--work-tree=$gitWorkFile"

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
    val outputDirectory: String = apiDir.getAbsolutePath
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

  def scaladocSetup(implicit subProject: SubProject, scalaCompiler: ScalaCompiler): Unit = {
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
        run(gitWorkParent, "git checkout gh-pages")
      } else {
        log.debug("# gitGit does not exist; about to create it in 2 steps.\n#  1) git clone the gh-pages branch into gitParent")
        log.debug(s"mkdir -p ${ gitWorkParent.getAbsolutePath }")
        gitWorkParent.mkdirs() // does not fail if the directories already exist
        log.debug(s"rm -rf ${ gitWorkParent.listFiles.mkString(" ") }")
        Nuke.removeUnder(gitWorkParent) // clear out any children left over from before
        run(gitWorkParent, s"git clone -b gh-pages git@github.com:${ project.gitHubName }/${ subProject.name }.git")

        log.debug(s"#  2) rename ${ subProject.name } to ${ subProject.baseDirectory.getName }")
        log.debug(s"(cd ${ file(subProject.baseDirectory.getName) }; mv ${ subProject.name } ${ subProject.baseDirectory.getName })")
        file(project.name).renameTo(file(subProject.baseDirectory.getName))
      }
      Nuke.removeUnder(apiDir)
      run(gitWorkParent, s"git ${ gitWorkTree } add -a")
      run(gitWorkParent, s"git ${ gitWorkTree } commit -m -")
    } catch {
      case e: Exception => log.error(e.getMessage)
    }
    ()
  }
}
