package com.micronautics.sbt

import sbt.Keys._
import sbt._
import Settings._

trait PublishPluginImpl { this: AutoPlugin =>
  import PublishPlugin._
  import autoImport._

  override lazy val projectSettings = Seq(
    /** Include the SBT sub-project name to avoid collisions when merging Scaladoc from all the subprojects */
    apiDir := gitWorkFile.value / "latest/api/",

    commitAndDoc := {
      val log = streams.value.log
      try {
        scaladocSetup.value // is this already called elsewhere?

        log.info("Fetching latest updates for this git repo")
        "git pull".!!

        val changedFileNames = "git diff --name-only".!!.trim.replace("\n", ", ")
        if (changedFileNames.nonEmpty) {
          log.info(s"About to commit these changed files: $changedFileNames")
          "git add -A".!!
          "git commit -m -".!!
        }

        /*val stagedFileNames = "git diff --cached --name-only".!!.trim.replace("\n", ", ")
        if (stagedFileNames.nonEmpty) {
          println(s"About to push these staged files: $stagedFileNames")
        }*/

        log.info("About to git push to origin")
        "git push origin HEAD".!!  // See https://stackoverflow.com/a/20922141/553865

        log.info("Creating Scaladoc")
        doc.in(Compile).value

        log.info("Uploading Scaladoc to GitHub Pages")
        scaladocPush.value
      } catch {
        case e: Exception => println(e.getMessage)
      }
      ()
    },

//    docPublishTag := ???, // todo write me

    gitWorkFile := crossTarget.value / "api" / baseDirectory.value.name,

    gitWorkTree := s"--work-tree=${ gitWorkFile.value }",

    publishAndTag := {
      val log = streams.value.log

      commitAndDoc.in(Compile).value
      publish.value

      log.info(s"Creating git tag for v${ version.value }")
      s"""git tag -a ${ version.value } -m ${ version.value }""".!!
      s"""git push origin --tags""".!!
      ()
    },

    scaladoc := {
      val log = streams.value.log

      log.info("Creating Scaladoc")
      scaladocSetup.value
      doc.in(Compile).value

      log.info("Uploading Scaladoc to GitHub Pages")
      scaladocPush.value
      ()
    },

    scaladocPush := {
      s"git ${ gitWorkTree.value } add -a".!!
      s"git ${ gitWorkTree.value } commit -m -".!!
      s"git ${ gitWorkTree.value } push origin gh-pages".!!
    },

    scaladocSetup := {
      val log = streams.value.log
      try {
        val cwd: String = sys.props("user.dir") // save directory so it can be restored at the end
        log.debug(s"CWD=${ sys.props("user.dir") }")

        val path = baseDirectory.value.getAbsolutePath
        System.setProperty("user.dir", path)

        if (new File(gitWorkFile.value, ".git").exists) {
          log.debug(s"${ gitWorkFile.value } exists; about to git checkout gh-pages into $path")
          s"git checkout gh-pages".!!
        } else {
          log.debug(s"${ gitWorkFile.value } does not exist; about to create it and git clone the gh-pages branch into $path")
          gitWorkFile.value.mkdirs()
          s"git clone -b gh-pages git@github.com:$gitHubName/${ name.value }.git".!!
        }
        log.debug(s"About to clear the contents of ${ apiDir.value }")
        sbt.IO.delete(apiDir.value.listFiles)
        s"git ${ gitWorkTree.value } add -a".!!
        s"git ${ gitWorkTree.value } commit -m -".!!

        System.setProperty("user.dir", cwd) // restore previous directory
      } catch {
        case e: Exception => println(e.getMessage)
      }
    }
  )

  override def trigger = allRequirements
}
