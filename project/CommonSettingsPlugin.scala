import sbt._
import Keys._
import com.micronautics.sbt.Settings.gitHubName
import bintray.BintrayPlugin.autoImport.{bintrayOrganization, bintrayPackage, bintrayRepository}

/** @see See [[http://blog.jaceklaskowski.pl/2015/04/12/using-autoplugin-in-sbt-for-common-settings-across-projects-in-multi-project-build.html Using AutoPlugin in Sbt for Common Settings Across Projects in Multi-project Build]] */
object CommonSettingsPlugin extends AutoPlugin {
  override lazy val projectSettings = Seq(
    apiURL := Some(url(s"https://$gitHubName.github.io/${ name.value }/${ baseDirectory.value.name }/latest/api")),


    version := "0.2.1"
  )

  override def trigger = allRequirements
}
