package com.test.app

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{RejectionHandler, _}
import akka.stream.ActorMaterializer
import org.fusesource.jansi.Ansi._
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success}

object HttpServer extends App {

  val logger = LoggerFactory.getLogger(HttpServer.getClass)

  implicit val system = ActorSystem("server")
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  implicit def myRejectionHandler = RejectionHandler.newBuilder()
    .handleNotFound { complete((NotFound, "Not here!")) }
    .result()

  val binding = Http().bindAndHandle(Routes(), "localhost", 8080)

  binding.onComplete {
    case Success(binding) ⇒
      val localAddress = binding.localAddress

      println(ansi().a(
        """
             ___  _   _  ___   ___
            / __|| | | ||_ _| / __| ___  _ _ __ __ ___  _ _
           | (_ || |_| | | |  \__ \/ -_)| '_|\ V // -_)| '_|
            \___| \___/ |___| |___/\___||_|   \_/ \___||_|

        """
      ).reset())

      logger.info(s"Server is listening on ${localAddress.getHostName}:${localAddress.getPort}")
    //      waitForTermination
    case Failure(e) ⇒
      logger.info(s"Binding failed with ${e.getMessage}")
      system.terminate
  }

  def waitForTermination = {
    print("Please press any key to exit ")
    Console.in.read
    system.terminate
  }
}
