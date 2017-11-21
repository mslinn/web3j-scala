package com.micronautics.publish

import java.io.File
import buildInfo.BuildInfo
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._
import org.scalatest.Matchers._

@RunWith(classOf[JUnitRunner])
class TestyMcTestFace extends WordSpec with MustMatchers {
  val config: Config = Config.default

  implicit val project: Project = Project(
    gitHubName = "mslinn",
    name       = BuildInfo.name,
    version    = BuildInfo.version,
    copyright  = "Copyright 2017 Micronautics Research Corporation. All rights reserved."
  )

  // subprojects to document; others are ignored (such as this one)
  val subprojects: List[SubProject] =
    List("root", "demo")
      .map(x => new SubProject(x, new File(x).getAbsoluteFile))

  val documenter = new Documenter(config, subprojects)

  "" should {
    "" in {
      "Hello world".length === 11
    }
  }
}
