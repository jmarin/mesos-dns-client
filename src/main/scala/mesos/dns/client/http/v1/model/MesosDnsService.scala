package mesos.dns.client.http.v1.model

object MesosDnsService {
  def empty: MesosDnsService = MesosDnsService("", "", "", "")
}

case class MesosDnsService(host: String, ip: String, port: String, service: String)
