name := "akka-http-k8s-test"

organization := "com.elmarweber"

version := "1.0"

scalaVersion := "2.11.8"

enablePlugins(DockerPlugin)

resolvers += Resolver.jcenterRepo

libraryDependencies ++= {
  val akkaV            = "2.4.7"
  val ficusV           = "1.2.4"
  val scalaTestV       = "3.0.0-M15"
  val slf4sV           = "1.7.10"
  val logbackV         = "1.1.3"
  Seq(
    "com.typesafe.akka" %% "akka-http-core"                    % akkaV,
    "com.typesafe.akka" %% "akka-http-experimental"            % akkaV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-slf4j"                        % akkaV,
    "org.slf4s"         %% "slf4s-api"                         % slf4sV,
    "ch.qos.logback"    % "logback-classic"                    % logbackV,
    "org.scalatest"     %% "scalatest"                         % scalaTestV       % Test,
    "com.typesafe.akka" %% "akka-http-testkit"                 % akkaV            % Test
  )
}



updateOptions := updateOptions.value.withCachedResolution(true)
