import sbt.Keys._
import sbt._
import sbtdocker.DockerKeys._
import sbtassembly.AssemblyPlugin.autoImport._

object HttpServiceBuild extends Build {

  import Dependencies._

  lazy val commonSettings = Seq(
    organization := "me.spoolphiz",
    scalaVersion := "2.11.8",
    scalacOptions in(Compile, doc) ++= Seq("-groups", "-implicits"),
    javacOptions in(Compile, doc) ++= Seq("-notimestamp", "-linksource"),
    autoAPIMappings := true,
    parallelExecution in Test := false,
    parallelExecution in IntegrationTest := true,
    publishArtifact in Test := false
  )

  lazy val httpService: Project = Project(
    id = "httpservice",
    base = file("."),
    settings = commonSettings ++ dockerSettings ++
      Seq(name := "httpservice",
        version := "0.0.1",
        libraryDependencies ++= projectDeps)
  )
    .enablePlugins(sbtdocker.DockerPlugin)

  lazy val dockerSettings = Seq(
    // make the docker task depend on the assembly task, which generates a fat JAR file
    docker <<= (docker dependsOn (assembly in httpService)),

    // generate Dockerfile
    dockerfile in docker := {
      val artifact = (assemblyOutputPath in assembly in httpService).value
      val artifactTargetPath = s"/opt/services/${artifact.name}"

      new sbtdocker.mutable.Dockerfile {
        from(s"java:$dockerJavaVer")

        // Dockerfile best practices: https://docs.docker.com/articles/dockerfile_best-practices/
        expose(9004) // post the server listens on
        copy(artifact, artifactTargetPath)
        copy(baseDirectory(_ / "deploy" / "start-service.sh").value, file("/opt/services/bin/start-service.sh"))

        runRaw(s"""sed -i.bak "s/~APP_VER~/${version.value}/" /opt/services/bin/start-service.sh""".stripMargin.trim)
      }
    },

    // tag image
    imageNames in docker := Seq(
      // Sets a name with a tag that contains the project version
      sbtdocker.ImageName(
        namespace = Some("spoolphiz.test"),
        repository = "httpservice",
        tag = Some("latest")
      )
    )
  )
}

