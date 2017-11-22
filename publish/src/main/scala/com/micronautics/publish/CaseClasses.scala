package com.micronautics.publish

import java.io.File

/** @param name GitHub repo name for this SBT project
  * @param gitRemoteOriginUrl taken from `.git/config`
  * @param version Git release version of this SBT project */
case class Project(
  gitRemoteOriginUrl: String,
  override val name: String,
  version: String
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
