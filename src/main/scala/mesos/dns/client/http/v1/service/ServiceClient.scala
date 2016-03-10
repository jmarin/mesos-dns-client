package mesos.dns.client.http.v1.service

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.HttpMethods._
import akka.stream.{ ActorMaterializer, StreamTcpException }
import akka.stream.scaladsl.{ Sink, Source }
import mesos.dns.client.http.v1.model.ResponseError

import scala.concurrent.{ ExecutionContext, Future }

trait ServiceClient {
  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer

  def host: String
  def port: Int

  def sendGetRequest(path: Uri): Future[HttpResponse] = {
    implicit val ec: ExecutionContext = system.dispatcher
    val connectionFlow = Http().outgoingConnection(host, port)
    val request = HttpRequest(GET, path)
    Source.single(request).via(connectionFlow).runWith(Sink.head).recover {
      case e: StreamTcpException =>
        HttpResponse(
          ServiceUnavailable,
          Nil,
          HttpEntity.empty(ContentTypes.NoContentType),
          HttpProtocols.`HTTP/1.1`
        )
    }
  }

  def sendResponseError(response: HttpResponse) = {
    Future.successful(Left(ResponseError(response.status.toString())))
  }

}
