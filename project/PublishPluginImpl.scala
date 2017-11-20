package com.micronautics.sbt

import sbt._
import sbt.Keys._
import Settings._
import CommandLine.run

/** To debug, run `bin/demo debug` from the command line and attach from IDEA. */
trait PublishPluginImpl { this: AutoPlugin =>
  import PublishPlugin._
  import autoImport._

  override lazy val projectSettings = Seq(
    /** Include the SBT sub-project name to avoid collisions when merging Scaladoc from all the subprojects */
    apiDir := gitWorkFile.value / "latest/api/",

    commitAndDoc := {
      implicit val log: Logger = streams.value.log
      try {
        scaladocSetup.value // is this already called elsewhere?

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

        log.info("Creating Scaladoc")
        doc.in(Compile).value

        log.info("Uploading Scaladoc to GitHub Pages")
        scaladocPush.value
      } catch {
        case e: Exception => log.error(e.getMessage)
      }
      ()
    },

//    docPublishTag := ???, // todo write me

    gitWorkFile := gitWorkParent.value / baseDirectory.value.name,

    gitWorkParent := crossTarget.value / "api",

    gitWorkTree := s"--work-tree=${ gitWorkFile.value }",

    publishAndTag := {
      implicit val log: Logger = streams.value.log

      commitAndDoc.in(Compile).value
      publish.value

      log.info(s"Creating git tag for v${ version.value }")
      run(s"""git tag -a ${ version.value } -m ${ version.value }""")
      run(s"""git push origin --tags""")
      ()
    },

    scaladoc := {
      implicit val log: Logger = streams.value.log

      log.info("Creating Scaladoc")
      scaladocSetup.value
      doc.in(Compile).value

      log.info("Uploading Scaladoc to GitHub Pages")
      scaladocPush.value
      ()
    },

    scaladocPush := {
      implicit val log: Logger = streams.value.log
      run(s"git ${ gitWorkTree.value } add -a")
      run(s"git ${ gitWorkTree.value } commit -m -")
      run(s"git ${ gitWorkTree.value } push origin gh-pages")
    },

    scaladocSetup := {
      implicit val log: Logger = streams.value.log
      try {
        val gitGit = new File(gitWorkFile.value, ".git")

        val gitParent = gitWorkParent.value.getAbsolutePath

        log.debug(s"baseDirectory = ${ baseDirectory.value.getAbsolutePath }")
        log.debug(s"CWD           = ${ sys.props("user.dir") }")
        log.debug(s"gitWorkParent = ${ gitWorkParent.value }")
        log.debug(s"gitParent     = $gitParent")
        log.debug(s"gitWorkFile   = ${ gitWorkFile.value }")
        log.debug(s"gitGit        = $gitGit")
        log.debug(s"apiDir        = ${ apiDir.value }")

        if (gitGit.exists) {
          log.debug("gitGit exists; about to git checkout gh-pages into gitParent")
          run(s"git checkout gh-pages", gitWorkParent.value)
        } else {
          log.debug("gitGit does not exist; about to create it in 2 steps.\n  1) git clone the gh-pages branch into gitParent")
          IO.createDirectory(gitWorkParent.value) // does not fail if the directories already exist
          IO.delete(gitWorkParent.value.listFiles) // clear out any children left over from before
          run(s"git clone -b gh-pages git@github.com:$gitHubName/${ name.value }.git", gitWorkParent.value)

          val x = s"  2) rename ${ name.value } to ${ baseDirectory.value.name }"
          println(x)
          log.debug(x)
          IO.move(file(name.value), file(baseDirectory.value.name))
        }
        val files: Array[File] = apiDir.value.listFiles
        if (files.nonEmpty) {
          log.debug(s"About to clear the contents of apiDir (${ files.mkString(", ") })")
          IO.delete(files)
        }
        run(s"git ${ gitWorkTree.value } add -a",      gitWorkParent.value)
        run(s"git ${ gitWorkTree.value } commit -m -", gitWorkParent.value)
      } catch {
        case e: Exception => log.error(e.getMessage)
      }
    }
  )

  override def trigger = allRequirements
}
