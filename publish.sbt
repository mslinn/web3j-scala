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
