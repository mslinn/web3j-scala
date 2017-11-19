import com.micronautics.sbt.PublishPlugin
import Settings._
import sbt.Keys.apiURL

val web3jVersion = "3.1.0"

/* To preview the Scaladoc, run `previewSite`, which launches a static web server, or run `previewAuto`,
   which launches a dynamic server that updates its content at each modification in your source files.
   Both launch the server on port 4000 and attempt to connect your browser to http://localhost:4000/.
   To change the server port, use the key previewFixedPort: {{{previewFixedPort := Some(9999)}}} */

lazy val demo = project
  .enablePlugins(PublishPlugin)
  .settings(
    apiURL := Some(url(s"https://$gitHubName.github.io/web3j-scala/demo/latest/api")),// todo is the demo directory respected?

    // define the statements initially evaluated when entering 'console', 'console-quick', but not 'console-project'
    initialCommands in console := """import java.math.BigInteger
                                    |import java.util.concurrent.Future
                                    |import org.web3j.protocol._
                                    |import org.web3j.protocol.infura._
                                    |""".stripMargin,

    name := "web3j-scala-demo",

    unmanagedSourceDirectories in Compile += baseDirectory.value / "../abiWrapper"
  ).dependsOn(root)

lazy val root = (project in file("root"))
  .enablePlugins(PublishPlugin)
  .settings(
    apiURL := Some(url(s"https://$gitHubName.github.io/web3j-scala/root/latest/api")),

    // define the statements initially evaluated when entering 'console', 'console-quick', but not 'console-project'
    initialCommands in console := """import java.math.BigInteger
                                    |import java.util.concurrent.Future
                                    |import org.web3j.protocol._
                                    |import org.web3j.protocol.infura._
                                    |""".stripMargin,

    libraryDependencies ++= Seq(
      "ch.qos.logback"         %  "logback-classic"       % "1.2.3",
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

    name := "web3j-scala"
  )
