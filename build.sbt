val web3jVersion = "3.1.0"

lazy val commonSettings = Seq(
  cancelable := true,

  buildInfoKeys ++= Seq[BuildInfoKey]( // assumes that the git repo directory has not been renamed
    "gitRepoName" -> new File(sys.props("user.dir")).getName
  ),

  developers := List(
    Developer("mslinn",
              "Mike Slinn",
              "mslinn@micronauticsresearch.com",
              url("https://github.com/mslinn")
    )
  ),

  // define the statements initially evaluated when entering 'console', 'console-quick', but not 'console-project'
  initialCommands in console := """
                                  |""".stripMargin,
  javacOptions ++= Seq(
    "-Xlint:deprecation",
    "-Xlint:unchecked",
    "-source", "1.8",
    "-target", "1.8",
    "-g:vars"
  ),

  libraryDependencies ++= Seq(
    "commons-io"              %  "commons-io"      % "2.6"    withSources(),
    "ch.qos.logback"          %  "logback-classic" % "1.2.3",
    "com.github.scopt"        %% "scopt"           % "3.7.0"  withSources(),
    //
     "org.scalatest"          %% "scalatest"       % "3.0.3" % Test withSources(),
     "junit"                  %  "junit"           % "4.12"  % Test
  ),

  licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html")),

  logLevel := Level.Warn,

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

  scmInfo := Some(
    ScmInfo(
      url("https://github.com/mslinn/web3j-scala"),
      "git@github.com:mslinn/web3j-scala.git"
    )
  ),

  version := "0.5.0"
)

lazy val demo = project
  .aggregate(root)
  .settings(
    // define the statements to be evaluated when entering 'console' and 'consoleQuick' but not 'consoleProject'
    initialCommands in console := """import java.math.BigInteger
                                    |import java.util.concurrent.Future
                                    |import org.web3j.protocol._
                                    |import org.web3j.protocol.infura._
                                    |""".stripMargin,

    name := "demo",

    unmanagedSourceDirectories in Compile += baseDirectory.value / "../abiWrapper"
  ).dependsOn(root)

lazy val root = (project in file("root"))
  .settings(
    // define the statements initially evaluated when entering 'console', 'console-quick', but not 'console-project'
    initialCommands in console := """
                                    |""".stripMargin,

    libraryDependencies ++= Seq(
      // See https://docs.web3j.io/modules.html
      "org.web3j"              %  "abi"                   % web3jVersion withSources(), // Application Binary Interface encoders
      "org.web3j"              %  "codegen"               % web3jVersion withSources(), // Code generators
      "org.web3j"              %  "console"               % web3jVersion withSources(), // Command-line tools
      "org.web3j"              %  "core"                  % web3jVersion withSources(),
      "org.web3j"              %  "crypto"                % web3jVersion withSources(), // For transaction signing and key/wallet management
      "org.web3j"              %  "geth"                  % web3jVersion withSources(), // Geth-specific JSON-RPC module
      "org.web3j"              %  "infura"                % web3jVersion withSources(), // Infura-specific HTTP header support
      "org.web3j"              %  "parity"                % web3jVersion withSources(), // Parity-specific JSON-RPC module
    //  "org.web3j"              %  "quorum"                % "0.7.0"      withSources(), // integration with JP Morgan's Quorum
      "org.web3j"              %  "rlp"                   % web3jVersion withSources(), // Recursive Length Prefix (RLP) encoders
    //  "org.web3j"              %  "tuples"                % web3jVersion withSources(), // See http://www.javatuples.org ... not needed for Scala?
      "org.web3j"              %  "utils"                 % web3jVersion withSources(), // Minimal set of utility classes
      "org.web3j"              %  "web3j-maven-plugin"    % "0.1.2"      withSources(), // Create Java classes from solidity contract files
      //
      "org.scala-lang.modules" %% "scala-java8-compat"    % "0.8.0",
      "ch.qos.logback"         %  "logback-classic"       % "1.2.3",
      //
      "org.scalatest"          %% "scalatest"   % "3.0.3" % Test withSources(),
      "junit"                  %  "junit"       % "4.12"  % Test
    ),

    name := "root"
  )
