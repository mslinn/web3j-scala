package com.micronautics.publish

object Config {
  val default: Config = Config()
}

/** @param autoCheckIn Stop program if any files need to be committed or pushed
  * @param deleteAfterUse remove the GhPages temporary directory when the program ends
  * @param dryRun Show the commands that would be run */
case class Config(
  autoCheckIn: Boolean = true,
  deleteAfterUse: Boolean = true,
  dryRun: Boolean = false,
  overWriteIndex: Boolean = false,
  subProjectNames: List[String] = List("root", "demo")
)
