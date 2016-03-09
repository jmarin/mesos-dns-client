package mesos.dns.client.http.v1.model

/*
Lists in JSON format the Mesos-DNS configuration parameters.
 */
object MesosDnsConfig {
  def empty: MesosDnsConfig = {
    MesosDnsConfig(
      List.empty,
      List.empty,
      0,
      0,
      0,
      "",
      List.empty,
      0,
      "",
      "",
      "",
      0,
      false,
      false
    )
  }
}

case class MesosDnsConfig(
  masters: List[String],
  zk: List[String],
  refreshSeconds: Int,
  ttl: Int,
  port: Int,
  domain: String,
  resolvers: List[String],
  timeout: Int,
  email: String,
  mName: String,
  listener: String,
  httpPort: Int,
  dnsOn: Boolean,
  httpOn: Boolean
)
