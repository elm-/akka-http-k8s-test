package org.elmarweber.test.akka.http.k8s.test

import akka.http.scaladsl.server.Directives

trait TestRoute extends Directives {
  val testRoute = pathPrefix("test") {
    pathEndOrSingleSlash {
      get {
        complete {
          "OK"
        }
      }
    }
  }
}

