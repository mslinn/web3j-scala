package com.micronautics.publish

import java.io.File

/** @param gitHubName GitHub account id for the git repo for this SBT project
  * @param name GitHub repo name for this SBT project
  * @param version Git release version of this SBT project
  * @param title Full name of this project; this value is used to create the Scaladoc title
  * @param copyright Scaladoc copyright info for this project */
case class Project(
  gitHubName: String,
  gitRemoteOriginUrl: String,
  override val name: String,
  version: String,
  title: String = "",
  copyright: String = "&nbsp;"
) extends SubProject(name, new File(sys.props("user.dir")).getAbsoluteFile) {
  //require(io.Source.fromURL(gitRemoteOriginUrl).mkString.trim.nonEmpty, s"$gitRemoteOriginUrl does not exist")
}

object ScalaCompiler {
  lazy val fullVersion: String = scala.util.Properties.versionNumberString
  lazy val majorMinorVersion: String = fullVersion.split(".").take(2).mkString(".")
}

class SubProject(val name: String, val baseDirectory: File) {
//  lazy val crossTarget: File = new File(s"target/scala-${ ScalaCompiler.majorMinorVersion }")
}
