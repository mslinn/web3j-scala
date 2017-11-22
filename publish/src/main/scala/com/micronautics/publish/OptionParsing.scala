package com.micronautics.publish

import buildInfo.BuildInfo
import scopt.OptionParser

trait OptionParsing {
  val parser: OptionParser[Config] = new scopt.OptionParser[Config]("bin/publish") {
    head("Publish", BuildInfo.version)

    opt[Boolean]('a', "autoCheckIn").action { (x, c) =>
      c.copy(autoCheckIn = x)
    }.text("Stop program if any files need to be committed or pushed")

    opt[String]('c', "copyright").action { (x, c) =>
      c.copy(copyright = x)
    }.required.text("Scaladoc footer")

    opt[Boolean]('d', "deleteAfterUse").action { (x, c) =>
      c.copy(deleteAfterUse = x)
    }.text("remove the GhPages temporary directory when the program ends")

    opt[String]('n', "gitHubName").action { (x, c) =>
      c.copy(gitHubName = x)
    }.required.text("Github ID for project")

    opt[Boolean]('o', "overWriteIndex").action { (x, c) =>
      c.copy(overWriteIndex = x)
    }.text(s"Do not preserve any pre-existing index.html in the Scaladoc root")

    opt[Boolean]('r', "dryRun").action { (x, c) =>
      c.copy(dryRun = x)
    }.text("Show the commands that would be run")

    opt[String]('s', "subProjectNames").action { (x, c) =>
      c.copy(subProjectNames = x.split(",").toList)
    }.text(s"Comma-delimited names of subprojects to generate Scaladoc for")
  }
}
