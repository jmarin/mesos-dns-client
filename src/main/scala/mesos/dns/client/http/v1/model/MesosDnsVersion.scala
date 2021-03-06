package mesos.dns.client.http.v1.model

/*
Lists in JSON format the Mesos-DNS version and source code URL.
*/
object MesosDnsVersion {
  def empty: MesosDnsVersion = MesosDnsVersion("", "", "")
}

case class MesosDnsVersion(Service: String, URL: String, Version: String)
