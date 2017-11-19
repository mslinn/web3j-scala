// See https://stackoverflow.com/a/27858890/553865
evictionWarningOptions in update :=
  EvictionWarningOptions
    .default
    .withWarnTransitiveEvictions(false)
    .withWarnDirectEvictions(false)
    .withWarnScalaVersionEviction(false)

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
