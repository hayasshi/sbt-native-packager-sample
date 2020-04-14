lazy val akkaHttpVersion = "10.1.11"
lazy val akkaVersion    = "2.6.4"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.github.hayasshi",
      scalaVersion    := "2.13.1"
    )),
    name := "sbt-native-packager-sample",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "ch.qos.logback"    % "logback-classic"           % "1.2.3",

      "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"                % "3.0.8"         % Test
    )
  )
  .settings(
    defaultLinuxInstallLocation in Docker     := "/opt/hayasshi",
    executableScriptName                      := "app",
    dockerBaseImage                           := "adoptopenjdk/openjdk8:x86_64-alpine-jdk8u242-b08-slim",
    dockerRepository                          := Some("hayasshi"),
    packageName in Docker                     := "sbt-native-packager-sample",
    dockerUpdateLatest                        := true,
    dockerExposedPorts                       ++= Seq(8080),
    mainClass in (Compile, bashScriptDefines) := Some("com.github.hayasshi.QuickstartApp"),
    javaOptions in Universal ++= Seq(
      "-server"
    ),
    bashScriptExtraDefines ++= Seq(
      """addJava "-Xms${JVM_HEAP_MIN:-256m}" """,
      """addJava "-Xmx${JVM_HEAP_MAX:-256m}" """,
      """addJava "-XX:MaxMetaspaceSize=${JVM_META_MAX:-128m}" """,
      """addJava "${JVM_GC_OPTIONS:--XX:+UseG1GC}" """
    )
  )
  .enablePlugins(JavaAppPackaging, AshScriptPlugin)
