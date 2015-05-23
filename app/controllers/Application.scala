package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

object Application extends Controller {

	val canned_response = Json.arr(Json.obj(
		"invitee" -> "John Smith",
		"email" -> "john@smith.mx"
	))

  val readsInvitation = (
  	(__ \ 'invitee).read[String] and
  	(__ \ 'email).read[String]
  ) tupled

  def post_invitation = Action(parse.json) { request =>
  	request.body.validate(readsInvitation) map {
  		case (invitee, email) => Created
  	} recoverTotal {
  		e => BadRequest(JsError.toFlatJson(e))
  	}
  }
  def get_invitation = Action {
  	Ok(canned_response)
  }
}