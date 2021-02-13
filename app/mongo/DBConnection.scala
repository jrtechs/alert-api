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
import java.util.Date

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import scala.collection.immutable.IndexedSeq


class DBConnection(collectionName: String, databaseName: String, host: String, user: String, password: String)
{
    //constructor
    // import Helpers._
    val connectionString: String = "mongodb://%s:%s@%s/?authSource=admin".format(user, password, host)

    val mongoClient: MongoClient = MongoClient(connectionString)

    // get handle to "mydb" database
    val database: MongoDatabase = mongoClient.getDatabase(databaseName)

    val collection: MongoCollection[Document] = database.getCollection(collectionName)
    println("connected to db %s and collection %s".format(databaseName, collectionName))
    //end constructor


    def insertData(jsonString: String): Unit =
    {
        var doc: Document = Document(jsonString)
        doc = doc.+("date" -> new Date())

        print("inserting this boi: ")
        print(doc.toString())

        val observable: Observable[Completed] = collection.insertOne(doc)

        observable.subscribe(new Observer[Completed] {

            override def onNext(result: Completed): Unit = println("Inserted")

            override def onError(e: Throwable): Unit = println("Failed")

            override def onComplete(): Unit = println("Completed")
        })

        println("Inserted document into database")
    }

    def queryMessages(): String = 
    {
        collection.find().printResults()

        // println("Printing collection contents")

        // val observable: Observable[Document] = collection.find().

        // var  returnVar:Option[Document] = None

        // observable.subscribe(new Observer[Document]
        // {
        //     override def onNext(result: Document): Unit = returnVar = Some(result)

        //     override def onError(e: Throwable): Unit = println("Failed")

        //     override def onComplete(): Unit = println("Completed")
        // })
        // returnVar.get.toString()
    }
}

// object Helpers 
// {

//     implicit class DocumentObservable[C](val observable: Observable[Document]) extends ImplicitObservable[Document] 
//     {
//         override val converter: (Document) => String = (doc) => doc.toJson
//     }

//     implicit class GenericObservable[C](val observable: Observable[C]) extends ImplicitObservable[C] 
//     {
//         override val converter: (C) => String = (doc) => doc.toString
//     }

//     trait ImplicitObservable[C] 
//     {
//         val observable: Observable[C]
//         val converter: (C) => String

//         def results(): Seq[C] = Await.result(observable.toFuture(), Duration(10, TimeUnit.SECONDS))
//         def headResult() = Await.result(observable.head(), Duration(10, TimeUnit.SECONDS))
//         def printResults(initial: String = ""): Unit = {
//             if (initial.length > 0) print(initial)
//                 results().foreach(res => println(converter(res)))
//         }
//         def printHeadResult(initial: String = ""): Unit = println(s"${initial}${converter(headResult())}")
//     }

// }

// object starter
// {

//     // test code for the database
//     def main(args: Array[String]): Unit =
//     {
//         import Helpers._

//         //root user
//         //example password
//         val connectionString: String = "mongodb://root:example@127.0.0.1/?authSource=admin"

//         val mongoClient: MongoClient = MongoClient(connectionString)

//         // get handle to "mydb" database
//         val database: MongoDatabase = mongoClient.getDatabase("mydb")

//         val collection: MongoCollection[Document] = database.getCollection("test")
//         println("connected to db")

//         val doc: Document = Document("name" -> "the biggest brain", "type" -> "wozerfda",
//         "count" -> 1, "info" -> Document("x" -> 333, "y" -> 44))

//         collection.insertOne(doc).results()
//     }
// }
