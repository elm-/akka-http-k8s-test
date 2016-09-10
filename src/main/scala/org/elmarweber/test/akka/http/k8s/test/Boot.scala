package org.elmarweber.test.akka.http.k8s.test

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import scala.util._
import org.slf4s.Logging

object Boot extends App with TestRoute with Logging {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val bindingFuture = Http().bindAndHandle(testRoute, "0.0.0.0", 8080)

  bindingFuture.transform(
    binding => log.info(s"REST interface bound to ${binding.localAddress} "), { t => log.error(s"Couldn't bind interface: ${t.getMessage}", t); sys.exit(1) }
  )

  sys.addShutdownHook {
    log.info("Received shutdown")
    bindingFuture.flatMap(_.unbind()).onComplete {
      case Success(_) =>
        log.info("Unbound port, giving 2 sec graceperiod for handling open connections")
        Thread.sleep(2000)
        log.info("Graceperiod over, terminating system")
        system.terminate()
      case Failure(ex) =>
        log.error("Error during shutdown", ex)
        System.exit(1)
    }
  }
}
