import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "accept POST invitations" in new WithApplication{
      val json: JsValue = Json.parse("""{"invitee":"John Smith", "email":"john@smith.mx"}""")
      val invitation = route(FakeRequest(POST, "/invitation").withJsonBody(json)).get
      status(invitation) must equalTo(201)
    }

    "discard POST invitations with missing data" in new WithApplication {
      val json = Json.parse("""{"invitee":"John Smith", "useless_field":"value"}""")
      val invitation = route(FakeRequest(POST, "/invitation").withJsonBody(json)).get
      status(invitation) must equalTo(400)
    }

    "return the invitee on GET request" in new WithApplication {
      val invitation = route(FakeRequest(GET, "/invitation")).get

      status(invitation) must equalTo(OK)
      val json = Json.parse(contentAsString(invitation))
      (json(0) \ "invitee").as[String] must equalTo("John Smith")
      (json(0) \ "email").as[String] must equalTo("john@smith.mx")

    }
  }
}
