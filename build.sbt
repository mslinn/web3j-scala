val web3jVersion = "4.5.17"

cancelable := true

// Travis can be a PITA
crossScalaVersions := { if (new java.io.File("/home/travis").exists) Seq("2.13.1") else Seq("2.12.10", "2.13.1") }

fork in Test := true

// define the statements initially evaluated when entering 'console', 'console-quick', but not 'console-project'
initialCommands in console := """import scala.sys.process._
                                |import java.math.BigInteger
                                |import java.util.concurrent.Future
                                |import org.web3j.protocol._
                                |import org.web3j.protocol.infura._
                                |""".stripMargin

javacOptions ++= Seq(
  "-Xlint:deprecation",
  "-Xlint:unchecked",
  "-source", "1.8",
  "-target", "1.8",
  "-g:vars"
)

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.3" withSources(),
  // See https://docs.web3j.io/modules.html
  "org.web3j"              %  "abi"                     % web3jVersion withSources(), // Application Binary Interface encoders
  "org.web3j"              %  "codegen"                 % web3jVersion withSources(), // Code generators
  //"org.web3j"              %  "console"                 % web3jVersion withSources(), // Command-line tools
  "org.web3j"              %  "core"                    % web3jVersion withSources(),
//  "org.web3j"              %  "crypto"                  % web3jVersion withSources(), // For transaction signing and key/wallet management
  "org.web3j"              %  "geth"                    % web3jVersion withSources(), // Geth-specific JSON-RPC module
  "org.web3j"              %  "infura"                  % "4.2.0"      withSources(), // Infura-specific HTTP header support
  "org.web3j"              %  "parity"                  % web3jVersion withSources(), // Parity-specific JSON-RPC module
//  "org.web3j"              %  "quorum"                  % "0.7.0"      withSources(), // integration with JP Morgan's Quorum
  "org.web3j"              %  "rlp"                     % web3jVersion withSources(), // Recursive Length Prefix (RLP) encoders
  "org.web3j"              %  "utils"                   % web3jVersion withSources(), // Minimal set of utility classes
  "org.web3j"              %  "web3j-maven-plugin"      % "0.3.5"      withSources(), // Create Java classes from solidity contract files
  //
  "org.scala-lang.modules" %% "scala-java8-compat"      % "0.9.0",
  "ch.qos.logback"         %  "logback-classic"         % "1.2.7",
  //
  "org.scalatest"          %% "scalatest"     % "3.0.8" % Test withSources(),
  "junit"                  %  "junit"         % "4.12"  % Test
)

libraryDependencies ++=  { // Newer versions of Java have had the runtime library reduced, so include missing Java dependencies
  sys.props("java.version") match {
    case jv if jv.startsWith("11") =>
	  javacOptions += "-J--add-modules=java.xml.bind"
	  Seq(
        "javax.xml.bind" % "jaxb-api" % "2.3.1"
      )

	case _ => Nil
}}

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

logLevel := Level.Warn

// Only show warnings and errors on the screen for compilations.
// This applies to both test:compile and compile and is Info by default
logLevel in compile := Level.Warn

// Level.INFO is needed to see detailed output when running tests
logLevel in test := Level.Info

name := "web3j-scala"

organization := "com.micronautics"

resolvers ++= Seq(
  "Ethereum Maven" at "https://dl.bintray.com/ethereum/maven/",
  "bintray"        at "https://bintray.com/web3j/maven/org.web3j",
  //"Snapshots"      at "https://oss.sonatype.org/content/repositories/snapshots"
)

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
  "-Xlint:adapted-args",               // Warn if an argument list is modified to match the receiver.
  "-Xlint:constant",                   // Evaluation of a constant arithmetic expression results in an error.
  "-Xlint:delayedinit-select",         // Selecting member of DelayedInit.
  "-Xlint:doc-detached",               // A Scaladoc comment appears to be detached from its element.
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
)

// The REPL can’t cope with -Ywarn-unused:imports or -Xfatal-warnings so turn them off for the console
scalacOptions in (Compile, console) --= Seq("-Ywarn-unused:imports", "-Xfatal-warnings")

scalacOptions in (Compile, doc) ++= baseDirectory.map { bd: File =>
  Seq[String](
     "-sourcepath", bd.getAbsolutePath,
     "-doc-source-url", "https://github.com/mslinn/web3j-scala/tree/master€{FILE_PATH}.scala"
  )
}.value

scalaVersion := "2.13.1"

unmanagedSourceDirectories in Test ++= Seq(
  baseDirectory.value / "abiWrapper",
  baseDirectory.value / "demo"
)

version := web3jVersion
