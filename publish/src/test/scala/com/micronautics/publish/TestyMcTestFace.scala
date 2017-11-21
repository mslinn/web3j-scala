package com.micronautics.publish

import java.io.File
import java.nio.file.Path
import buildInfo.BuildInfo
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._
import org.scalatest.Matchers._
import org.scalatest.OptionValues._
import org.slf4j.Logger

@RunWith(classOf[JUnitRunner])
class TestyMcTestFace extends WordSpec with MustMatchers {
  implicit val logger: Logger = org.slf4j.LoggerFactory.getLogger("pub")

  implicit val config: Config =
    Config
      .default
      .copy(subProjectNames = List("root", "demo"))
      //.copy(deleteAfterUse = false)

  implicit protected [publish] val commandLine: CommandLine = new CommandLine

  val gitHubUserUrl: String = commandLine.run("git config --get remote.origin.url")

  implicit val project: Project = Project(
    gitHubName = "mslinn",
    gitRemoteOriginUrl = gitHubUserUrl,
    name       = BuildInfo.gitRepoName,
    version    = BuildInfo.version,
    copyright  = "Copyright 2017 Micronautics Research Corporation. All rights reserved."
  )

  // subprojects to document; others are ignored (such as this one)
  val subprojects: List[SubProject] =
    List("root", "demo")
      .map(x => new SubProject(x, new File(x).getAbsoluteFile))

  val documenter = new Documenter(subprojects)
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
      val rootFileNames: Array[String] = ghPages.root.toFile.listFiles.map(_.getName)
      logger.info(s"ghPages.root (${ ghPages.root }) contains ${ rootFileNames.mkString(", ") }")
      rootFileNames mustBe Array("latest")
      ghPages.deleteScaladoc()
      ghPages.root.resolve("latest/api").toFile.listFiles.length mustBe 0
    }
  }

  "RunScaladoc" should {
    "work" in {
      subProjects.foreach(documenter.runScaladoc)

      ghPages.root.resolve("latest/api/demo").toFile.listFiles.length must be > 0
      ghPages.root.resolve("latest/api/root").toFile.listFiles.length must be > 0

      /*import java.awt.Desktop
      import java.net.URI

      if (Desktop.isDesktopSupported) {
        val demo: Path = ghPages.root.resolve("latest/api/demo")
        val uri: URI = new URI(s"file://$demo")
        Desktop.getDesktop.browse(uri)
      }*/
    }
  }
}
