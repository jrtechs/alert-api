package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.concurrent.Future

import discord.DiscordManager.getDiscord
import apiMessages.MessageData
import mongo.DBManager.getConnection


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController 
{
  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }


  def sendMessage = Action { _ =>
    getDiscord.sendDiscordMessage("hello",true)

    getDiscord.sendDiscordMessage("world",false)
    Ok("Sending Discord Message")
  }

  implicit val messageReads: Reads[MessageData] = (
    (JsPath \ "priority").read[Boolean] and
    (JsPath \ "message").read[String]
    )(MessageData.apply _)


  def sendMessagePost = Action(parse.json) { request =>

    val messageJS:JsValue = request.body
    val message = messageJS.as[MessageData]
    print(message)
    val n: Unit = getDiscord().sendDiscordMessage(message.message, message.priority)
    getConnection().insertData(request.body.toString())
    // println(request.body.toString())
    Ok
  }


  def getMessage = Action { _ =>
    //getConnection().queryMessages().toString()
    Ok(getConnection().queryMessages())
  }
}
