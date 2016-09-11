package org.elmarweber.test.akka.http.k8s.test

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import scala.concurrent._
import scala.concurrent.duration._

import scala.util._
import org.slf4s.Logging

object Boot extends App with TestRoute with Logging {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val bindingFuture = Http().bindAndHandle(testRoute, "0.0.0.0", 9091)

  bindingFuture.transform(
    binding => log.info(s"REST interface bound to ${binding.localAddress} "), { t => log.error(s"Couldn't bind interface: ${t.getMessage}", t); sys.exit(1) }
  )

  sys.addShutdownHook {
    try {
      log.info("Received shutdown")
      bindingFuture.flatMap(_.unbind()).onComplete {
        case Success(_) =>
          log.info("Unbound port, giving 2 sec grace period for handling open connections")
          Thread.sleep(2000)
          log.info("Grace period over, terminating system")
          val termination = system.terminate()
          implicit def ec = ExecutionContext.Implicits.global
          Await.ready(termination, 10.seconds)
        case Failure(ex) =>
          log.error("Error during undind", ex)
      }
    } catch {
      case e: Exception =>
        println(s"Error During shutdown: ${e.getMessage}")
    }
  }
}
