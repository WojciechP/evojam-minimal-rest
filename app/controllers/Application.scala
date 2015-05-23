package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

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
}