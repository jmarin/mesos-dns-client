package mesos.dns.client.http.v1.model

object Host {
  def empty: Host = Host("", "")
}

case class Host(host: String, ip: String)
