package mesos.dns.client.http.v1.model

/*
Lists in JSON format the Mesos-DNS configuration parameters.
 */
object MesosDnsConfig {
  def empty: MesosDnsConfig = MesosDnsConfig(
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    "",
    Nil,
    Nil,
    "",
    "",
    "",
    "",
    false,
    false,
    false,
    false
  )
}

case class MesosDnsConfig(
  RefreshSeconds: Int,
  Port: Int,
  Timeout: Int,
  StateTimeoutSeconds: Int,
  ZkDetectionTimeout: Int,
  HttpPort: Int,
  TTL: Int,
  SOARetry: Int,
  SOARefresh: Int,
  SOAExpire: Int,
  SOAMinttl: Int,
  Masters: String,
  Resolvers: List[String],
  IPSources: List[String],
  Zk: String,
  Domain: String,
  File: String,
  Listener: String,
  RecurseOn: Boolean,
  DnsOn: Boolean,
  ExternalOn: Boolean,
  EnforeceRFC952: Boolean
)
