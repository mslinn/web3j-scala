/** This task tries to `git push` even if there's nothing to commit, then publishes Scaladoc.
  * Best practice is to comment your commits: git commit -m "Your comment here"
  * However, this task ensures any uncommitted changes are committed before publishing, including new files;
  * it provides the comment as "-". */
val commitAndPublish =
  taskKey[Unit]("Rebuilds the docs, commits the git repository, and pushes to publish the updated gitbook")

commitAndPublish := {
  "git pull".!!
  "git add -A".!!
  "git commit -m -".!!
  "git push origin master".!!
  doc.in(Test).value
  ghpagesPushSite.value
  publish.value
}

/** Be sure to update the version string in build.sbt before running this task */
val publishAndTag =
  taskKey[Unit]("Invokes commitAndPublish, then creates a git tag for the version string defined in build.sbt")

publishAndTag := {
  commitAndPublish.value
  s"""git tag -a "${ version.value }" -m "v${ version.value }"""".!!
  s"""git push origin --tags""".!!
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
