package me.spoolphiz.service

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import org.apache.log4j.Logger

import scala.concurrent.{ExecutionContext, Future}

object Main extends App {
  val loggerName = this.getClass.getName
  lazy val logger = Logger.getLogger(loggerName)

  implicit val actorSystem = ActorSystem()
  implicit val ec: ExecutionContext = actorSystem.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val config = ConfigFactory.load()
  val httpConfig = config.getConfig("http")
  val httpHost = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")

  val routes = pathPrefix("api" / "v1") {
    pathPrefix("health-check") {
      pathEndOrSingleSlash {
        complete(OK -> "Ok")
      }
    }
  }

  val bindingFuture: Future[ServerBinding] =
    Http().bindAndHandle(routes, httpHost, httpPort)

  bindingFuture.onFailure {
    case ex: Exception =>
      logger.error(ex, "Failed to bind to {}:{}", httpHost, httpPort)
  }

  sys.addShutdownHook({
    logger.info("shutdown")
    actorSystem.terminate()
  })
}
