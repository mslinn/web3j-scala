// Settings for sbt assembly

test in assembly := {}

mainClass in assembly := Some("org.web3j.console.Runner")

assemblyMergeStrategy in assembly := {
 case PathList("META-INF", _ @ _*) => MergeStrategy.discard
 case _ => MergeStrategy.first
}

//fullClasspath in assembly := (fullClasspath in Compile).value

//exportJars := true
