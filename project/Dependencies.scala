import sbt._

object Dependencies {
  // versions
  lazy val dockerJavaVer = sys.env.getOrElse("JAVA_VERSION", "8-jdk")
  lazy val akkaVer = "2.4.7"
  lazy val akkaHttpVer = "10.0.0"
  lazy val slf4jVer = "1.7.5"

  // libs
  val config = "com.typesafe" % "config" % "1.3.0"
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaVer
  val akkaHttpCore = "com.typesafe.akka" %% "akka-http-core" % akkaHttpVer
  val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVer
  val log4j = "log4j" % "log4j" % "1.2.17"

  // project dependencies
  val projectDeps =
  Seq(
    akkaActor,
    akkaHttpCore,
    akkaHttp,
    log4j,
    config
  )
}
