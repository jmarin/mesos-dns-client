package mesos.dns.client.http.v1

import mesos.dns.client.http.v1.service.MesosDnsClient
import org.scalatest.{AsyncFlatSpec, FlatSpec, MustMatchers}

import scala.concurrent.{ExecutionContext, Future}

class MesosDnsHttpApiSpec extends AsyncFlatSpec with MustMatchers {

  override implicit val executionContext: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  "Mesos DNS Server" must "return version information" in {
    val maybeVersion = MesosDnsClient.mesosDnsVersion
    maybeVersion.flatMap { version =>
      version match {
        case Right(v) =>
          v.Service mustBe "Mesos-DNS"
          v.URL mustBe "https://github.com/mesosphere/mesos-dns"
          v.Version mustBe "v0.5.0"

        case Left(e) => fail(s"Failed to connect to Mesos-DNS: ${e.desc}")
      }
    }
  }

  //TODO: Review JSON deserialization, it's failing there
  //it must "return configuration information" in {
  //  val maybeConfig = MesosDnsClient.mesosDnsConfig
  //  maybeConfig.flatMap { config =>
  //    config match {
  //      case Right(c) =>
  //        c.DnsOn mustBe true
  //      case Left(e) => fail(s"Failed to connect to Mesos-DNS: ${e.desc}")
  //    }
  //  }
  //}

  it must "return ip addresses that correspond to a hostname" in {
    val maybeService = MesosDnsClient.mesosDnsHost("marathon.mesos")
    maybeService.flatMap { host =>
      host match {
        case Right(h) =>
          h.head.host mustBe "marathon.mesos."
          h.head.ip.isEmpty mustBe false
        case Left(e) => fail(s"Failed to connect to Mesos-DNS: ${e.desc}")
      }
    }
  }

  // Assumes chronos is deployed through Marathon
  it must "return service information for chronos" in {
    val maybeChronos = MesosDnsClient.mesosDnsService("chronos")
    maybeChronos.flatMap { chronos =>
      chronos match {
        case Right(s) =>
          s.head.ip.isEmpty mustBe false
          s.head.port.isEmpty mustBe false
          s.head.service mustBe "_chronos._tcp.marathon.mesos"
         case Left(e) => fail(s"Failed to connect to Mesos-DNS: ${e.desc}")
      }
    }
  }




}
