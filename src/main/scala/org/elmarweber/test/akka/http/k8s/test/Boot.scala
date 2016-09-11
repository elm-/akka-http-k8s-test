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
      implicit def ec = ExecutionContext.Implicits.global
      log.info("Received shutdown")
      val f = bindingFuture.flatMap(_.unbind())
      f.onFailure { case ex: Exception =>
        log.error(s"Error during unbind: ${ex.getMessage}", ex)
      }
      Await.ready(f, 10.seconds)
      log.info("Unbound port, giving 5 sec grace period for handling open connections")
      Thread.sleep(5000)
      log.info("Grace period over, terminating system")
      val termination = system.terminate()
      Await.ready(termination, 10.seconds)
    } catch {
      case e: Exception =>
        println(s"Error During shutdown: ${e.getMessage}")
    }
  }
}
