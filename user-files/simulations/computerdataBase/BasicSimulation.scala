package computerdataBase

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class BasicSimulation extends Simulation {

	val httpProtocol = http
		.baseUrl("http://computer-database.gatling.io")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.9,pt;q=0.8")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")

	val headers_6 = Map("Origin" -> "http://computer-database.gatling.io")

	val scn = scenario("BasicSimulation")
		.exec(http("/GET Request to Macbook")
			.get("/computers?f=macbook"))
		.pause(2)
		.exec(http("/GET Request to see Edit computer Mac")
			.get("/computers/6"))
		.pause(4)
		.exec(http("/GET Request to see first computers")
			.get("/"))
		.pause(5)
		.exec(http("/GET Request to see next page")
			.get("/computers?p=1"))
		.pause(2)
		.exec(http("/GET Request to see next to next page")
			.get("/computers?p=2"))
		.pause(2)
		.exec(http("/GET Request to see Amiga Computer")
			.get("/computers/71"))
		.pause(3)
		.exec(http("/POST Request to success edit computer")
			.post("/computers/71")
			.headers(headers_6)
			.formParam("name", "Amiga 1500 Teste")
			.formParam("introduced", "")
			.formParam("discontinued", "")
			.formParam("company", "6"))

	setUp(scn.inject(atOnceUsers(5))).protocols(httpProtocol)
}