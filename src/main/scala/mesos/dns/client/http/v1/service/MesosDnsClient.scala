package mesos.dns.client.http.v1.service

import akka.actor.ActorSystem
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.model.StatusCodes._
import akka.stream.ActorMaterializer
import mesos.dns.client.http.v1.model._
import mesos.dns.client.http.v1.protocol.MesosDnsClientJsonProtocol

import scala.concurrent.{ ExecutionContext, Future }
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import com.typesafe.config.ConfigFactory

class MesosDnsClient(sys: ActorSystem) extends ServiceClient with MesosDnsClientJsonProtocol {

  override implicit val system: ActorSystem = sys
  override implicit val materializer: ActorMaterializer = ActorMaterializer()

  implicit val ec: ExecutionContext = system.dispatcher

  val config = ConfigFactory.load()

  override def host: String = config.getString("mesos.dns.client.host")

  override def port: Int = config.getInt("mesos.dns.client.port")

  def mesosDnsVersion: Future[Either[ResponseError, MesosDnsVersion]] = {
    sendGetRequest("/v1/version").flatMap { response =>
      response.status match {
        case OK => Unmarshal(response.entity).to[MesosDnsVersion].map(Right(_))
        case _ => sendResponseError(response)
      }
    }
  }

  def mesosDnsConfig: Future[Either[ResponseError, MesosDnsConfig]] = {
    sendGetRequest("/v1/config").flatMap { response =>
      println(response)
      response.status match {
        case OK => Unmarshal(response.entity).to[MesosDnsConfig].map(Right(_))
        case _ => sendResponseError(response)
      }
    }
  }
  def mesosDnsHost(host: String): Future[Either[ResponseError, List[MesosDnsHost]]] = {
    sendGetRequest(s"/v1/hosts/$host").flatMap { response =>
      response.status match {
        case OK => Unmarshal(response.entity).to[List[MesosDnsHost]].map(Right(_))
        case _ => sendResponseError(response)
      }
    }
  }
  def mesosDnsService(service: String): Future[Either[ResponseError, List[MesosDnsService]]] = {
    val serviceUrl = s"_$service._tcp.marathon.mesos"
    sendGetRequest(s"/v1/services/$serviceUrl").flatMap { response =>
      response.status match {
        case OK => Unmarshal(response.entity).to[List[MesosDnsService]].map(Right(_))
        case _ => sendResponseError(response)
      }
    }
  }

}
