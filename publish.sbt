import sbt._
import Keys._

// git settings
val workFile = new File(target.value, "api")
val workTree = s"--work-tree=$workFile"

// doc and package-site settings
val apiDir = target.value / "latest/api/"
siteSourceDirectory := apiDir

scaladocSetup()
doc.value
packageSite.value
scaladocCreate()

def scaladocSetup(): Unit = {
  if (new File(workFile, ".git").exists)
    s"git $workTree checkout gh-pages".!!
  else
    s"git $workTree clone -b gh-pages git@github.com:mslinn/web3j-scala.git".!!
  sbt.IO.delete(apiDir.listFiles)
  s"git $workTree add -a".!!
  s"git $workTree commit -m -".!!
}

def scaladocCreate(): Unit = {
  s"git $workTree add -a".!!
  s"git $workTree commit -m -".!!
  s"git $workTree push origin gh-pages".!!
}


lazy val scaladoc2 =
  taskKey[Unit]("Rebuilds the Scaladoc and pushes the updated Scaladoc to GitHub pages without committing to the git repository")

scaladoc2 := {
  println("Creating Scaladoc")
  doc.in(Compile).value

  println("Uploading Scaladoc to GitHub Pages")
  ghpagesPushSite.value // todo does it push to the proper project directory?
  ()
}

/** Best practice is to comment your commits before invoking this task: `git commit -am "Your comment here"`.
  * This task does the following:
  * 1. `git pull` when it starts.
  * 2. Attempts to build Scaladoc, which fails if there is a compile error
  * 3. Ensures any uncommitted changes are committed before publishing, including new files; it provides the comment as "-".
  * 4. Git pushes all files
  * 5. Uploads new Scaladoc
  * 6. Publishes new version to Bintray */
lazy val commitAndDoc =
  taskKey[Unit]("Rebuilds the docs, commits the git repository, and publishes the updated Scaladoc without publishing a new version")

commitAndDoc := {
  try {
    println("Fetching latest updates for this git repo")
    "git pull".!!

    println("Creating Scaladoc")
    doc.in(Compile).value

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

    println("Uploading Scaladoc to GitHub Pages")
    ghpagesPushSite.value // todo does it push to the proper project directory?
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
  commitAndDoc.in(Compile).value
  publish.value
  println(s"Creating git tag for v${ version.value }")
  s"""git tag -a ${ version.value } -m ${ version.value }""".!!
  s"""git push origin --tags""".!!
  ()
}
