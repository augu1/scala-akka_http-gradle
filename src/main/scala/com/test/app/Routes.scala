package com.test.app

import akka.actor.ActorSystem
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.Materializer

import scala.concurrent.ExecutionContext

object Routes extends Directives {
  def apply()(implicit s: ActorSystem, m: Materializer, e: ExecutionContext): Route = {
    pathSingleSlash {
      getFromResource("web/index.html")
    }
  }
}