package com.micronautics.sbt

import sbt.Keys._
import sbt._

object PublishPlugin extends AutoPlugin {
  object autoImport {
    lazy val apiDir = settingKey[File]("File where Scaladoc for a branch is generated into")

    /** Best practice is to comment your commits before invoking this task: `git commit -am "Your comment here"`.
      * This task does the following:
      * 1. `git pull` when it starts.
      * 2. Attempts to build Scaladoc, which fails if there is a compile error; output is to subprojects' target/api directory.
      * 3. Ensures any uncommitted changes are committed before publishing, including new files; it provides the comment as "-".
      * 4. Git pushes all files
      * 5. Uploads new Scaladoc
      * 6. Publishes new version to Bintray */
    lazy val commitAndDoc =
      taskKey[Unit]("Rebuilds the docs, commits the git repository, and publishes the updated Scaladoc without publishing a new version")

    lazy val gitWorkFile = settingKey[File]("Point git to non-standard location")

    lazy val gitWorkTree = settingKey[String]("Point git to non-standard location")

    /** Publish a new version of this library to BinTray.
      * Be sure to update the version string in build.sbt before running this task. */
    lazy val publishAndTag =
      taskKey[Unit]("Invokes commitAndPublish, then creates a git tag for the version string defined in build.sbt")

    lazy val scaladoc2 =
      taskKey[Unit]("Rebuilds the Scaladoc and pushes the updated Scaladoc to GitHub pages without committing to the git repository")

    lazy val scaladocPush = taskKey[Unit]("")

    lazy val scaladocSetup = taskKey[Unit]("Sets up gh-pages branch for receiving scaladoc")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    apiDir := target.value / "latest/api/",

    commitAndDoc := {
      try {
        println("Fetching latest updates for this git repo")
        "git pull".!!

        val changedFileNames = "git diff --name-only".!!.trim.replace("\n", ", ")
        if (changedFileNames.nonEmpty) {
          println(s"About to commit these changed files: $changedFileNames")
          "git add -A".!!
          "git commit -m -".!!
        }

        /*val stagedFileNames = "git diff --cached --name-only".!!.trim.replace("\n", ", ")
        if (stagedFileNames.nonEmpty) {
          println(s"About to push these staged files: $stagedFileNames")
        }*/

        println("About to git push to origin")
        "git push origin HEAD".!!  // See https://stackoverflow.com/a/20922141/553865

        println("Creating Scaladoc")
        //scaladocSetup.value // todo need this here?
        doc.in(Compile).value

        println("Uploading Scaladoc to GitHub Pages")
//        SiteScaladocPlugin.autoImport
//        siteSubdirName in Global := apiDir.value.getAbsolutePath // todo append sbt subproject name
//        siteSourceDirectory in Global := target.value / "latest/api/"

//        makeSite.value
        scaladocPush.value
    //    ghpagesPushSite.value // todo does it push to the proper project directory?
      } catch {
        case e: Exception => println(e.getMessage)
      }
      ()
    },

    gitWorkFile := new File(target.value, "api").getAbsoluteFile,

    gitWorkTree := s"--work-tree=${ gitWorkFile.value }",

    publishAndTag := {
      commitAndDoc.in(Compile).value
      publish.value
      println(s"Creating git tag for v${ version.value }")
      s"""git tag -a ${ version.value } -m ${ version.value }""".!!
      s"""git push origin --tags""".!!
      ()
    },

    scaladoc2 := {
      println("Creating Scaladoc")
      scaladocSetup.value
      doc.in(Compile).value

      println("Uploading Scaladoc to GitHub Pages")
//      siteSubdirName := apiDir.value.getAbsolutePath // todo append sbt subproject name
//      makeSite.value
      scaladocPush.value
      ()
    },

    scaladocPush := {
      s"git ${ gitWorkTree.value } add -a".!!
      s"git ${ gitWorkTree.value } commit -m -".!!
      s"git ${ gitWorkTree.value } push origin gh-pages".!!
    },

    scaladocSetup := {
      try {
        val cwd: String = sys.props("user.dir") // safe default directory
        println(s"CWD 1=${ sys.props("user.dir") }")
        val path = baseDirectory.value.getAbsolutePath
        println(s"path=$path")
        System.setProperty("user.dir", path)
        println(s"CWD 2=${ sys.props("user.dir") }")

        if (new File(gitWorkFile.value, ".git").exists) {
          println(s"${ gitWorkFile.value } exists; about to git checkout using ${ gitWorkTree.value }")
          s"git checkout gh-pages".!!
        } else {
          println(s"${ gitWorkFile.value } does not exist; about to git clone the gh-pages branch using ${ gitWorkTree.value }")
          s"git clone -b gh-pages git@github.com:mslinn/web3j-scala.git".!!
        }
        println(s"About to clear the contents of ${ apiDir.value }")
        sbt.IO.delete(apiDir.value.listFiles)
        s"git ${ gitWorkTree.value } add -a".!!
        s"git ${ gitWorkTree.value } commit -m -".!!

        System.setProperty("user.dir", cwd) // restore default directory
      } catch {
        case e: Exception => println(e.getMessage)
      }
    }
  )

  override def trigger = allRequirements
}
