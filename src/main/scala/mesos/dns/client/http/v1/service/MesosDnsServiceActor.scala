package mesos.dns.client.http.v1.service

import scala.concurrent.duration._
import akka.actor.{ Actor, ActorLogging, Props }
import akka.pattern.pipe
import com.typesafe.config.ConfigFactory
import mesos.dns.client.http.v1.model.MesosDnsService
import mesos.dns.client.http.v1.service.MesosDnsServiceActor.{ GetServiceConfig, RefreshServiceConfig }

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Random

object MesosDnsServiceActor {
  case object RefreshServiceConfig
  case class GetServiceConfig(serviceName: String)
  def props: Props = Props(new MesosDnsServiceActor)
}

class MesosDnsServiceActor extends Actor with ActorLogging {

  var services = scala.collection.mutable.Map[String, List[MesosDnsService]]()

  val config = ConfigFactory.load()
  val refreshInterval = config.getInt("mesos.dns.client.refreshInterval")

  val mesosDnsClient = new MesosDnsClient(context.system)

  implicit val ec: ExecutionContext = context.dispatcher

  override def preStart: Unit = {
    val scheduler =
      context.system.scheduler.schedule(
        500.milliseconds, refreshInterval.seconds, self, RefreshServiceConfig
      )
  }

  override def receive: Receive = {
    case RefreshServiceConfig =>
      log.debug(s"Refreshing service configuration")
      services.foreach { service =>
        services.remove(service._1)
      }

    case GetServiceConfig(name) =>
      val fList =
        if (services.nonEmpty && services.head._1.nonEmpty) {
          Future(random(services.getOrElse(name, Nil)))
        } else {
          mesosDnsClient.mesosDnsService(name).map { e =>
            e match {
              case Right(xs) =>
                if (xs.nonEmpty) {
                  services.update(name, xs)
                } else {
                  services.remove(name)
                }
                random(services.getOrElse(name, Nil))

              case Left(e) =>
                services.remove(name)
                Nil
            }
          }
        }
      fList pipeTo sender()
  }

  def random(xs: List[MesosDnsService]): MesosDnsService = {
    val rnd = new Random
    xs.toVector(rnd.nextInt(xs.size))
  }

}
