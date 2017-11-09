lazy val scaladoc =
  taskKey[Unit]("Rebuilds the Scaladoc and pushes the updated Scaladoc to GitHub pages without committing to the git repository")

scaladoc := {
  doc.in(Test).value
  ghpagesPushSite.value
}

/** Best practice is to comment your commits before invoking this task: `git commit -am "Your comment here"`.
  * This task does the following:
  * 1. `git pull` when it starts.
  * 2. Attempts to build Scaladoc, which fails if there is a compile error
  * 3. Ensures any uncommitted changes are committed before publishing, including new files; it provides the comment as "-".
  * 4. Pushes all files in the git cache
  * 5. Uploads new Scaladoc
  * 6. Publishes new version to Bintray */
lazy val commitAndPublish =
  taskKey[Unit]("Rebuilds the docs, commits the git repository, and publishes the updated Scaladoc without publishing a new version")

commitAndPublish := {
  try {
    "git pull".!!
    doc.in(Test).value

    val changedFileNames = "git diff --name-only".!!.trim.replace("\n", ", ")
    if (changedFileNames.nonEmpty) {
      println(s"About to commit these changed files: $changedFileNames")
      "git add -A".!!
      "git commit -m -".!!
    }

    val stagedFileNames = "git diff --cached --name-only".!!.trim.replace("\n", ", ")
    if (stagedFileNames.nonEmpty) {
      println(s"About to push these staged files: $stagedFileNames")
      "git push origin master".!!
    }

    ghpagesPushSite.value
    publish.value
  } catch {
    case e: Exception => println(e.getMessage)
  }
  ()
}

/** Publish a new version of this library to BinTray.
  * Be sure to update the version string in build.sbt before running this task. */
lazy val publishAndTag =
  taskKey[Unit]("Invokes commitAndPublish, then creates a git tag for the version string defined in build.sbt")

publishAndTag := {
  commitAndPublish.in(Compile).value
  s"""git tag -a ${ version.value } -m ${ version.value }""".!!
  s"""git push origin --tags""".!!
  ()
}

// See http://www.scala-sbt.org/1.0/docs/Howto-Scaladoc.html
autoAPIMappings := true
apiURL := Some(url("https://mslinn.github.io/web3j-scala/latest/api"))

bintrayOrganization := Some("micronautics")
bintrayRepository := "scala"
bintrayPackage := "web3j-scala"

// sbt-site settings
enablePlugins(SiteScaladocPlugin)
siteSourceDirectory := target.value / "api"
publishSite

// sbt-ghpages settings
enablePlugins(GhpagesPlugin)
git.remoteRepo := s"git@github.com:mslinn/${ name.value }.git"
