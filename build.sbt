
val web3jVersion = "3.1.0"

lazy val demo = project
  .settings(
    name := "demo",
    // define the statements to be evaluated when entering 'console' and 'consoleQuick' but not 'consoleProject'
    initialCommands in console := """import java.math.BigInteger
                                    |import java.util.concurrent.Future
                                    |import org.web3j.protocol._
                                    |import org.web3j.protocol.infura._
                                    |""".stripMargin,
    unmanagedSourceDirectories in Compile += baseDirectory.value / "../abiWrapper"
  )
  .aggregate(root)
  .dependsOn(root)

lazy val root = (project in file("root"))
  .settings(
    name := "root",
    // define the statements initially evaluated when entering 'console', 'console-quick', but not 'console-project'
    initialCommands in console := """
                                    |""".stripMargin,
  )
  .settings(
    libraryDependencies ++= Seq(
      // Dependencies
      library.scalaJava8Compact   % Compile,
      library.logbackClassic      % Compile,
      // See https://docs.web3j.io/modules.html
      library.web3jAbiModule      % Compile withSources(), // Application Binary Interface encoders
      library.web3jCodeGenModule  % Compile withSources(), // Code generators
      library.web3jCmdModule      % Compile withSources(), // Command-line tools
      library.web3jCoreModule     % Compile withSources(),
      library.web3jCryptoModule   % Compile withSources(), // For transaction signing and key/wallet management
      library.web3jGethModule     % Compile withSources(), // Geth-specific JSON-RPC module
      library.web3jInfuraModule   % Compile withSources(), // Infura-specific HTTP header support
      library.web3jParityModule   % Compile withSources(), // Parity-specific JSON-RPC module
      library.web3jRlpModule      % Compile withSources(), // Recursive Length Prefix (RLP) encoders
      library.web3jUtilsModule    % Compile withSources(), // Minimal set of utility classes
      library.web3jMavenPlugin    % Compile withSources(), // Create Java classes from solidity contract files
      // Testing Dependencies
      library.scalaTest           % Test    withSources(),
      library.junitFramework      % Test
    ).map(dependencies =>
      library.exclusions.foldRight(dependencies) { (rule, module) =>
        module.excludeAll(rule)
      })
  )
  .enablePlugins(BuildInfoPlugin)
  .settings(commonSettings)
  .settings(commonLoggingSettings)
  .settings(resolvers           ++= commonResolvers)
  .settings(libraryDependencies ++= commonDependencies)



lazy val library =
  new {
    object Version {
      val commonsIo         = "2.6"
      val scoptV            = "3.7.0"
      val logback           = "1.2.3"
      val junit             = "4.12"
      val pureConfig        = "0.8.0"
      val scalaFmt          = "1.3.0"
      val scalaTest         = "3.0.3"
      val scapeGoat         = "1.3.3"
      val scalaJava8compat  = "0.8.0"
      val web3j             = web3jVersion
      val web3jMaven        = "0.1.2"
    }
    val apacheCommonsIo       = "commons-io"                 %  "commons-io"          % Version.commonsIo
    val scalaJava8Compact     = "org.scala-lang.modules"     %% "scala-java8-compat"  % Version.scalaJava8compat
    val scalaTest             = "org.scalatest"              %% "scalatest"           % Version.scalaTest
    val scopt                 = "com.github.scopt"           %% "scopt"               % Version.scoptV
    val logbackClassic        = "ch.qos.logback"             %  "logback-classic"     % Version.logback
    val junitFramework        = "junit"                      %  "junit"               % Version.junit
    val web3jAbiModule        = "org.web3j"                  %  "abi"                 % Version.web3j
    val web3jCodeGenModule    = "org.web3j"                  %  "codegen"             % Version.web3j
    val web3jCmdModule        = "org.web3j"                  %  "console"             % Version.web3j
    val web3jCoreModule       = "org.web3j"                  %  "core"                % Version.web3j
    val web3jCryptoModule     = "org.web3j"                  %  "crypto"              % Version.web3j
    val web3jGethModule       = "org.web3j"                  %  "geth"                % Version.web3j
    val web3jInfuraModule     = "org.web3j"                  %  "infura"              % Version.web3j
    val web3jParityModule     = "org.web3j"                  %  "parity"              % Version.web3j
    val web3jRlpModule        = "org.web3j"                  %  "rlp"                 % Version.web3j
    val web3jUtilsModule      = "org.web3j"                  %  "utils"               % Version.web3j
    val web3jMavenPlugin      = "org.web3j"                  %  "web3j-maven-plugin"  % Version.web3jMaven

    //  "org.web3j"              %  "tuples"                % web3jVersion withSources(), // See http://www.javatuples.org ... not needed for Scala?
    //  "org.web3j"              %  "quorum"                % "0.7.0"      withSources(), // integration with JP Morgan's Quorum

    // All exclusions that should be applied to every module.
    val exclusions = Seq(
        // Exclusions
    )
  }

lazy val commonSettings = Seq(

  // Version Configuration
  scalaVersion := "2.12.4",
  version      := "0.5.0",

  // Project Meta Information
  organization := "com.micronautics",
  licenses     += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html")),
  developers   := List(
    Developer(
      "mslinn",
      "Mike Slinn",
      "mslinn@micronauticsresearch.com",
      url("https://github.com/mslinn")
    )
  ),
  // http://www.scala-sbt.org/0.12.4/api/sbt/ScmInfo.html
  scmInfo       := Some(
    ScmInfo(
      url("https://github.com/mslinn/web3j-scala"),
      "git@github.com:mslinn/web3j-scala.git"
    )
  ),
  // Buildinfo
  buildInfoKeys ++= Seq[BuildInfoKey](organization, name, version, developers),

  // Set compilation process to be interruptible
  cancelable   := true,
  // Fork all Test-Tasks
  fork         := true,
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
  scalacOptions in (Compile, console) --= Seq("-Ywarn-unused:imports", "-Xfatal-warnings")
)

// Common Dependencies
lazy val commonDependencies    = Seq(
  // Dependencies
  library.apacheCommonsIo   % Compile   withSources(),
  library.logbackClassic    % Compile,
  library.scopt             % Compile   withSources(),
  // Testing Dependencies
  library.scalaTest         % Test      withSources(),
  library.junitFramework    % Test
)

// Common Settings
lazy val commonLoggingSettings = Seq(
  logLevel            :=  Level.Warn,
  // Only show warnings and errors on the screen for compilations.
  // This applies to both test:compile and compile and is Info by default
  logLevel in compile :=  Level.Warn,
  // Level.INFO is needed to see detailed output when running tests
  logLevel in test    :=  Level.Info,
)

// Common Resolvers
lazy val commonResolvers = Seq(
    "Ethereum Maven" at "https://dl.bintray.com/ethereum/maven/"
)
