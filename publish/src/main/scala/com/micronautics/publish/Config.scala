package com.micronautics.publish

object Config {
  val default: Config = Config()
}

/** @param autoCheckIn Stop program if any files need to be committed or pushed
  * @param dryRun Show the commands that would be run */
case class Config(
  dryRun: Boolean = false,
  autoCheckIn: Boolean = true
)
