package com.micronautics.sbt

import sbt._

object PublishPlugin extends AutoPlugin with PublishPluginImpl {
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

//    lazy val docPublishTag = taskKey[Unit]("Does everything necessary to release Scaladoc for this project: initialize, build, upload, tag")

    lazy val gitWorkFile   = settingKey[File]("git work directory for each sbt subproject's scaladoc")

    lazy val gitWorkParent = settingKey[File]("directory to run git from when creating gitWorkFIle")

    lazy val gitWorkTree   = settingKey[String]("git option for applying gitWorkFile")

    /** Publish a new version of this library to BinTray.
      * Be sure to update the version string in build.sbt before running this task. */
    lazy val publishAndTag =
      taskKey[Unit]("Invokes commitAndPublish, then creates a git tag for the version string defined in build.sbt")

    lazy val scaladoc =
      taskKey[Unit]("Rebuilds the Scaladoc and pushes the updated Scaladoc to GitHub pages without committing to the git repository")

    lazy val scaladocPush  = taskKey[Unit]("Upload scaladocPush")

    lazy val scaladocSetup = taskKey[Unit]("Sets up gh-pages branch for receiving scaladoc")
  }
}
