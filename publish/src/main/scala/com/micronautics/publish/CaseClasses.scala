package com.micronautics.publish

import java.io.File

case class Project(
  gitHubName: String ,
  override val name: String,
  version: String,
  title: String = "",
  copyright: String = "&nbsp;"
) extends SubProject(name, new File(".").getAbsoluteFile) {
  val gitHub = s"https://github.com/$gitHubName/$name"
  assert(io.Source.fromURL(gitHub).mkString.nonEmpty, s"$gitHub does not exist")
}

case class ScalaCompiler(version: String) extends AnyVal {
  override def toString: String = version
}

class SubProject(val name: String, val baseDirectory: File) {
  def crossTarget(implicit scalaCompilerVersion: ScalaCompiler): File =
    new File(s"root/target/scala-$scalaCompilerVersion/")
}
