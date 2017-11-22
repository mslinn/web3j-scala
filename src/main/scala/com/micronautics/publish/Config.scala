package com.micronautics.publish

object Config {
  val default: Config = Config()
}

/** @param autoCheckIn Stop program if any files need to be committed or pushed
  * @param copyright Scaladoc footer
  * @param deleteAfterUse remove the GhPages temporary directory when the program ends
  * @param dryRun Show the commands that would be run
  * @param gitHubName Github ID for project
  * @param overWriteIndex Do not preserve any pre-existing index.html in the Scaladoc root
  * @param subProjectNames Names of subprojects to generate Scaladoc for; delimited by commas */
case class Config(
  autoCheckIn: Boolean = true,
  copyright: String = "&nbsp;",
  deleteAfterUse: Boolean = true,
  dryRun: Boolean = false,
  gitHubName: String = "mslinn", // Not a generally useful default value, but config requires a value must be specified, so we are good
  overWriteIndex: Boolean = false,
  subProjectNames: List[String] = List("root", "demo")
)
