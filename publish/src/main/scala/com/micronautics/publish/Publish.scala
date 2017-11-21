package com.micronautics.publish

import buildInfo.BuildInfo
import java.io.File
import org.slf4j.Logger

/** Utility for creating combined Scaladoc for an SBT multi-project.
  * Must be run from top-level git repo directory */
object Publish extends App {
  implicit val logger: Logger = org.slf4j.LoggerFactory.getLogger("pub")
  implicit protected val config: Config = Config.default

  implicit protected [publish] val commandLine: CommandLine = new CommandLine

  implicit val project: Project = Project(
    gitHubName         = "mslinn", // computing this for all cases is tricky
    gitRemoteOriginUrl = gitRemoteOriginUrl,
    name               = BuildInfo.name,
    version            = BuildInfo.version,
    copyright          = "Copyright 2017 Micronautics Research Corporation. All rights reserved."
  )

  // subprojects to document; others are ignored (such as this one)
  val subprojects: List[SubProject] =
    List("root", "demo")
      .map(subProjectName => new SubProject(subProjectName, new File(subProjectName).getAbsoluteFile))

  new Documenter(subprojects).publish()


  protected def gitRemoteOriginUrl: String = commandLine.run("git config --get remote.origin.url")
}
