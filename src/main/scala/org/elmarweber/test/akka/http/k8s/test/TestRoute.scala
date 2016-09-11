package org.elmarweber.test.akka.http.k8s.test

import akka.http.scaladsl.server.Directives

import scala.concurrent.{ExecutionContext, Future}

trait TestRoute extends Directives {
  implicit def ec: ExecutionContext

  val testRoute = pathPrefix("test") {
    pathEndOrSingleSlash {
      get {
        complete {
          "OK"
        }
      }
    } ~
    pathPrefix("sleep") {
      pathEndOrSingleSlash {
        get {
          parameter("delay".as[Long]) { delay =>
            complete {
              Future {
                Thread.sleep(delay)
                "OK"
              }
            }
          }
        }
      }
    }
  }
}

