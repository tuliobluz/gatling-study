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

	object Browse {
		val browse = exec(http("/GET Request to see first computers")
			.get("/"))
			.pause(5)
	}

	object Edit {
		val edit = exec(http("/GET Request to see Amiga Computer")
			.get("/computers/71"))
			.pause(3)
			.exec(http("/POST Request to success edit computer")
				.post("/computers/71")
				.headers(headers_6)
				.formParam("name", "Amiga 1500 Teste")
				.formParam("introduced", "")
				.formParam("discontinued", "")
				.formParam("company", "6"))
	}

	object Search {

		val feeder = csv("./data/search.csv").random // 1, 2

		val search = exec(http("Home")
			.get("/"))
			.pause(1)
			.feed(feeder) // 3
			.exec(http("Search")
			.get("/computers?f=${searchCriterion}") // 4
			.check(css("a:contains('${searchComputerName}')", "href").saveAs("computerURL"))) // 5
			.pause(1)
			.exec(http("Select")
				.get("${computerURL}")) // 6
			.pause(1)
	}

//	val scn = scenario("Scenario Name").exec(Search.search, Browse.browse, Edit.edit)

	val users = scenario("Users").exec(Search.search, Browse.browse)
	val admins = scenario("Admins").exec(Search.search, Browse.browse, Edit.edit)

	setUp(
		users.inject(rampUsers(10) during (10 seconds)),
		admins.inject(rampUsers(2) during (10 seconds))
	).protocols(httpProtocol)
}