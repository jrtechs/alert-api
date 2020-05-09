package discord

import scala.io.StdIn.{readLine,readInt}
import scala.io.Source
import scala.io.BufferedSource
import play.api.Play
import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config

object DiscordManager
{
    val conf : Tuple3[String, String, String] = getConfig().get
    var discord: Discord = new Discord(conf._1, conf._2, conf._3)

    def getDiscord():Discord = 
    {
        discord
    }

    def getConfig(): Option[Tuple3[String, String, String]] = 
    {
        //todo more error checking to see if value exists
        val conf: Config = ConfigFactory.load()
        Some(conf.getString("discord.token"), 
            conf.getString("discord.importantChannelID"), 
            conf.getString("discord.infoChannelID"))
    }
}