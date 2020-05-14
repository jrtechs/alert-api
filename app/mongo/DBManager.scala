package mongo


import org.mongodb.scala._
import org.mongodb.scala.model.Aggregates._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model._

import com.mongodb.MongoCredential._

import java.util.concurrent.TimeUnit

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import scala.collection.immutable.IndexedSeq


class DBManager 
{
  
}

object Helpers {

  implicit class DocumentObservable[C](val observable: Observable[Document]) extends ImplicitObservable[Document] {
    override val converter: (Document) => String = (doc) => doc.toJson
  }

  implicit class GenericObservable[C](val observable: Observable[C]) extends ImplicitObservable[C] {
    override val converter: (C) => String = (doc) => doc.toString
  }

  trait ImplicitObservable[C] {
    val observable: Observable[C]
    val converter: (C) => String

    def results(): Seq[C] = Await.result(observable.toFuture(), Duration(10, TimeUnit.SECONDS))
    def headResult() = Await.result(observable.head(), Duration(10, TimeUnit.SECONDS))
    def printResults(initial: String = ""): Unit = {
      if (initial.length > 0) print(initial)
      results().foreach(res => println(converter(res)))
    }
    def printHeadResult(initial: String = ""): Unit = println(s"${initial}${converter(headResult())}")
  }

}

object starter
{

    // test code for the database
    def main(args: Array[String]): Unit =
    {
        import Helpers._

        //root user
        //example password
        val connectionString: String = "mongodb://root:example@127.0.0.1/?authSource=admin"

        val mongoClient: MongoClient = MongoClient(connectionString)

        // get handle to "mydb" database
        val database: MongoDatabase = mongoClient.getDatabase("mydb")

        val collection: MongoCollection[Document] = database.getCollection("test")
        println("connected to db")

        val doc: Document = Document("name" -> "the biggest brain", "type" -> "wozerfda",
            "count" -> 1, "info" -> Document("x" -> 333, "y" -> 44))

        collection.insertOne(doc).results()
    }
}
