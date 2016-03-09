package mesos.dns.client.http.v1.model

object Service {
  def empty: Service = Service("", "", "", "")
}

case class Service(host: String, ip: String, port: String, service: String)
