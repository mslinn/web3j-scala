package com.micronautics.publish

import java.io.File
import buildInfo.BuildInfo
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._
import org.scalatest.Matchers._
import org.scalatest.OptionValues._

@RunWith(classOf[JUnitRunner])
class TestyMcTestFace extends WordSpec with MustMatchers {
  val config: Config =
    Config
      .default
      .copy(subProjectNames = List("root", "demo"))
      //.copy(deleteAfterUse = false)

  implicit val project: Project = Project(
    gitHubName = "mslinn",
    name       = BuildInfo.gitRepoName,
    version    = BuildInfo.version,
    copyright  = "Copyright 2017 Micronautics Research Corporation. All rights reserved."
  )

  // subprojects to document; others are ignored (such as this one)
  val subprojects: List[SubProject] =
    List("root", "demo")
      .map(x => new SubProject(x, new File(x).getAbsoluteFile))

  val documenter = new Documenter(config, subprojects)
  val subProjects: List[SubProject] = documenter.subProjects
  val ghPages: GhPages = documenter.ghPages

  "GhPages" should {
    "work" in {
      ghPages.apisRoot mustBe ghPages.root.resolve("latest/api")
      subProjects.find(_.name=="root").map(ghPages.apiRootFor).value mustBe ghPages.root.resolve("latest/api/root")
    }
  }

  "Setup" should {
    "work" in {
      documenter.setup()
      ghPages.root.toFile.listFiles.length mustBe 2
      ghPages.deleteScaladoc()
      ghPages.root.resolve("latest/api/demo").toFile.listFiles.length mustBe 0
      ghPages.root.resolve("latest/api/root").toFile.listFiles.length mustBe 0
    }
  }

  "RunScaladoc" should {
    "work" in {
//      documenter.runScaladoc(subProjects.head)
      subProjects.foreach(documenter.runScaladoc)

      import java.awt.Desktop
      import java.net.URI

      if (Desktop.isDesktopSupported)
        Desktop.getDesktop.browse(new URI(ghPages.root.resolve("latest/api/demo").toString))
    }
  }
}
