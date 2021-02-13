package mongo


import scala.io.StdIn.{readLine,readInt}
import scala.io.Source
import scala.io.BufferedSource
import play.api.Play
import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config


/**
 * Object used to manage the singleton database object
 * 
  * @author Jeffery Russell 5-14-20
  */
object DBManager
{
    val conf: Config = ConfigFactory.load()

    val connection: DBConnection = new DBConnection(
            conf.getString("mongo.collection"),
            conf.getString("mongo.db"),
            conf.getString("mongo.host"),
            conf.getString("mongo.user"),
            conf.getString("mongo.password"),
        );
    
    def getConnection():DBConnection = 
    {
        connection
    }
}