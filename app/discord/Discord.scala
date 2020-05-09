package discord

import scala.math._

import ackcord._
import ackcord.data._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.Future

import ackcord.requests._
import ackcord.syntax._
import ackcord.CacheSnapshot._
import scala.util.Success
import scala.util.Failure


/**
 * Main class used to interact with the discord api
 * 
  * @author Jeffery Russell 5-7-20
  */
class Discord (token: String, importantChannel: String, notImportantChannel: String)
{
    println("Starting discord bot.")

    var cache = None: Option[CacheState]

    val clientSettings = ClientSettings(token)
    import clientSettings.executionContext
    var client = None: Option[DiscordClient]

    val f : Future[DiscordClient] = clientSettings.createClient()
    f.onComplete
    {
        case Success(cl) => 
        {
            client = Some(cl)
            cl.onEventSideEffectsIgnore {
            case APIMessage.Ready(c:CacheState) => 
            {
                cache = Some(c)
                println("Got new Cache State")
            }}
            cl.login()
        }
        case Failure(exception) =>
        {
            println("Discord client failed to start") 
            println(exception)
        }
    }


    def sendDiscordMessage(message: String, important: Boolean = true): Unit =
    {
        cache match
        {
            case None => 
            {
                println("Discord Bot not started yet, sleeping thread")
                Thread.sleep(5000)
                sendDiscordMessage(message)
            }
            case Some(value) => 
            {
                client.get.requestsHelper.run(CreateMessage(TextChannelId.apply(
                    if (important) importantChannel else notImportantChannel), 
                        CreateMessageData(content = message)))(value.current)
                println("Sending message")
            }
        }
    }
}