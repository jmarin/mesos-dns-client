package mesos.dns.client.http.v1.protocol

import mesos.dns.client.http.v1.model._
import spray.json.DefaultJsonProtocol

trait MesosDnsClientJsonProtocol extends DefaultJsonProtocol {
  implicit val responseFormat = jsonFormat1(ResponseError.apply)
  implicit val hostStatus = jsonFormat2(MesosDnsHost.apply)
  implicit val serviceStatus = jsonFormat4(MesosDnsService.apply)
  implicit val configStatus = jsonFormat22(MesosDnsConfig.apply)
  implicit val versionStatus = jsonFormat3(MesosDnsVersion.apply)
}
