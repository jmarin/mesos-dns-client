package mesos.dns.client.http.v1.model

object MesosDnsHost {
  def empty: MesosDnsHost = MesosDnsHost("", "")
}

case class MesosDnsHost(host: String, ip: String)
