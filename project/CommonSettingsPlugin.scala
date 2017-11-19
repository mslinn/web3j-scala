import sbt._
import Keys._
import com.micronautics.sbt.Settings.gitHubName
//import bintray.BintrayPlugin.autoImport.{bintrayOrganization, bintrayPackage, bintrayRepository}
import com.micronautics.sbt.PublishPlugin.autoImport.apiDir

/** @see See [[http://blog.jaceklaskowski.pl/2015/04/12/using-autoplugin-in-sbt-for-common-settings-across-projects-in-multi-project-build.html Using AutoPlugin in Sbt for Common Settings Across Projects in Multi-project Build]] */
object CommonSettingsPlugin extends AutoPlugin {
  override lazy val projectSettings = Seq(
    apiURL := Some(url(s"https://$gitHubName.github.io/${ name.value }/${ baseDirectory.value.name }/latest/api")),

    // See http://www.scala-sbt.org/1.0/docs/Howto-Scaladoc.html
    autoAPIMappings := true,

//    bintrayOrganization := Some("micronautics"),
//    bintrayRepository := "scala",
//    bintrayPackage := name.value,

    cancelable := true,

    // See https://stackoverflow.com/a/27858890/553865
    evictionWarningOptions in update :=
      EvictionWarningOptions
        .default
        .withWarnTransitiveEvictions(false)
        .withWarnDirectEvictions(false)
        .withWarnScalaVersionEviction(false),

    javacOptions ++= Seq(
      "-Xlint:deprecation",
      "-Xlint:unchecked",
      "-source", "1.8",
      "-target", "1.8",
      "-g:vars"
    ),

    licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html")),

//    logBuffered in Global := false,

//    logLevel in Global := Level.Warn,

    // Only show warnings and errors on the screen for compilations.
    // This applies to both test:compile and compile and is Info by default
    logLevel in compile := Level.Warn,

    // Level.INFO is needed to see detailed output when running tests
    logLevel in test := Level.Info,

    organization := "com.micronautics",

    resolvers ++= Seq(
      "Ethereum Maven" at "https://dl.bintray.com/ethereum/maven/"
    ),

    scalacOptions ++= Seq( // From https://tpolecat.github.io/2017/04/25/scalac-flags.html
      "-deprecation",                      // Emit warning and location for usages of deprecated APIs.
      "-encoding", "utf-8",                // Specify character encoding used by source files.
      "-explaintypes",                     // Explain type errors in more detail.
      "-feature",                          // Emit warning and location for usages of features that should be imported explicitly.
      "-language:existentials",            // Existential types (besides wildcard types) can be written and inferred
      "-language:experimental.macros",     // Allow macro definition (besides implementation and application)
      "-language:higherKinds",             // Allow higher-kinded types
      "-language:implicitConversions",     // Allow definition of implicit functions called views
      "-unchecked",                        // Enable additional warnings where generated code depends on assumptions.
      "-Xcheckinit",                       // Wrap field accessors to throw an exception on uninitialized access.
      //"-Xfatal-warnings",                  // Fail the compilation if there are any warnings.
      "-Xfuture",                          // Turn on future language features.
      "-Xlint:adapted-args",               // Warn if an argument list is modified to match the receiver.
      "-Xlint:by-name-right-associative",  // By-name parameter of right associative operator.
      "-Xlint:constant",                   // Evaluation of a constant arithmetic expression results in an error.
      "-Xlint:delayedinit-select",         // Selecting member of DelayedInit.
      "-Xlint:doc-detached",               // A Scaladoc comment appears to be detached from its element.
      "-Xlint:inaccessible",               // Warn about inaccessible types in method signatures.
      "-Xlint:infer-any",                  // Warn when a type argument is inferred to be `Any`.
      "-Xlint:missing-interpolator",       // A string literal appears to be missing an interpolator id.
      "-Xlint:nullary-override",           // Warn when non-nullary `def f()' overrides nullary `def f'.
      "-Xlint:nullary-unit",               // Warn when nullary methods return Unit.
      "-Xlint:option-implicit",            // Option.apply used implicit view.
      "-Xlint:package-object-classes",     // Class or object defined in package object.
      "-Xlint:poly-implicit-overload",     // Parameterized overloaded implicit methods are not visible as view bounds.
      "-Xlint:private-shadow",             // A private field (or class parameter) shadows a superclass field.
      "-Xlint:stars-align",                // Pattern sequence wildcard must align with sequence component.
      "-Xlint:type-parameter-shadow",      // A local type parameter shadows a type already in scope.
      "-Xlint:unsound-match",              // Pattern match may not be typesafe.
      "-Yno-adapted-args",                 // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
      "-Ypartial-unification",             // Enable partial unification in type constructor inference
      //"-Ywarn-dead-code",                  // Warn when dead code is identified.
      "-Ywarn-extra-implicit",             // Warn when more than one implicit parameter section is defined.
      "-Ywarn-inaccessible",               // Warn about inaccessible types in method signatures.
      "-Ywarn-infer-any",                  // Warn when a type argument is inferred to be `Any`.
      "-Ywarn-nullary-override",           // Warn when non-nullary `def f()' overrides nullary `def f'.
      "-Ywarn-nullary-unit",               // Warn when nullary methods return Unit.
      "-Ywarn-numeric-widen"               // Warn when numerics are widened.
      //"-Ywarn-unused:implicits",           // Warn if an implicit parameter is unused.
      //"-Ywarn-unused:imports",             // Warn if an import selector is not referenced.
      //"-Ywarn-unused:locals",              // Warn if a local definition is unused.
      //"-Ywarn-unused:params",              // Warn if a value parameter is unused.
      //"-Ywarn-unused:patvars",             // Warn if a variable bound in a pattern is unused.
      //"-Ywarn-unused:privates",            // Warn if a private member is unused.
      //"-Ywarn-value-discard"               // Warn when non-Unit expression results are unused.
    ),

    // The REPL can’t cope with -Ywarn-unused:imports or -Xfatal-warnings so turn them off for the console
    scalacOptions in (Compile, console) --= Seq("-Ywarn-unused:imports", "-Xfatal-warnings"),

    scalacOptions in (Compile, doc) ++= baseDirectory.map { bd: File =>
      Seq(
        "-sourcepath",     bd.getAbsolutePath,
        "-doc-source-url", "https://github.com/mslinn/web3j-scala/tree/master€{FILE_PATH}.scala" // todo how to compute the url?
      )
    }.value,

    // See https://stackoverflow.com/a/22926413/553865 and "man scaladoc"
    scalacOptions in (Compile, doc) ++= Seq(
      "-d",         apiDir.value.toString,
      "-doc-title", name.value
    ),

    scalaVersion := "2.12.4",

    version := "0.2.1"
  )

  override def trigger = allRequirements
}
