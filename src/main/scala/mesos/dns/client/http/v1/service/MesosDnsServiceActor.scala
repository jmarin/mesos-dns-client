package mesos.dns.client.http.v1.service

import scala.concurrent.duration._
import akka.actor.{ Actor, ActorLogging, Props }
import akka.pattern.pipe
import com.typesafe.config.ConfigFactory
import mesos.dns.client.http.v1.model.MesosDnsService
import mesos.dns.client.http.v1.service.MesosDnsServiceActor.{ GetServiceConfig, RefreshServiceConfig }

import scala.concurrent.ExecutionContext

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
      context.system.scheduler.schedule(500.milliseconds, refreshInterval.seconds, self, RefreshServiceConfig)
  }

  override def receive: Receive = {
    case RefreshServiceConfig =>
      log.info(s"Refreshing service configuration")
      services.foreach { service =>
        self ! GetServiceConfig(service._1)
        println(services)
      }

    case GetServiceConfig(name) =>
      val fList = mesosDnsClient.mesosDnsService(name).map { e =>
        e match {
          case Right(xs) =>
            if (xs.nonEmpty) {
              services.update(name, xs)
              services.get(name)
            } else {
              services.remove(name)
            }
            services.get(name)

          case Left(e) =>
            services.remove(name)
            Nil
        }
      }
      fList pipeTo sender()
  }
}
