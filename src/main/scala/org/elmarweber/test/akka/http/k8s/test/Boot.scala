package org.elmarweber.test.akka.http.k8s.test

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.slf4s.Logging

object Boot extends App with TestRoute with Logging {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  Http().bindAndHandle(testRoute, "0.0.0.0", 8080).transform(
    binding => log.info(s"REST interface bound to ${binding.localAddress} "), { t => log.error(s"Couldn't bind interface: ${t.getMessage}", t); sys.exit(1) }
  )
}
