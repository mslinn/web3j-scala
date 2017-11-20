package com.micronautics.publish

import buildInfo.BuildInfo
import java.io.File

/** Utility for creating combined Scaladoc for an SBT multi-project.
  * Must be run from top-level git repo directory */
object Publish extends App {
  val scalaVer = scala.util.Properties.versionNumberString
  implicit val scalaCompiler: ScalaCompiler = ScalaCompiler(scalaVer.split(".").take(2).mkString("."))

  val project = Project(
    gitHubName = "mslinn",
    name       = BuildInfo.name,
    version    = BuildInfo.version,
    copyright  = "Copyright 2017 Micronautics Research Corporation. All rights reserved."
  )
  // subprojects to document; others are ignored (such as this one)
  val subprojects = List("root", "demo").map(x => new SubProject(x, new File(x).getAbsoluteFile))

  val multiScaladoc = new MultiScaladoc(project)
  subprojects.foreach { subProject =>
    multiScaladoc.commitAndDoc(subProject.baseDirectory)(subProject, scalaCompiler)
  }
}
