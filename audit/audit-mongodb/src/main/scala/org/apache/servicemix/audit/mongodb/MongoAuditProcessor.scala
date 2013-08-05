package org.apache.servicemix.audit.mongodb

import scala.beans.BeanProperty
import scala.collection.JavaConversions._

import com.mongodb.DBCollection
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons
import org.apache.camel.{Processor, Message, Exchange}
import org.slf4j.LoggerFactory

/**
 * Camel {@link Processor} that stores every {@link Exchange} into MongoDB.
 *
 * This will be added to the OSGi Service Registry as an intercept processor for {@link ServiceMix} to pick up.
 */
class MongoAuditProcessor extends Processor with MongoAdapter {

  val logger = LoggerFactory.getLogger(classOf[MongoAuditProcessor])

  @BeanProperty
  var host = "localhost"

  @BeanProperty
  var port = 27017

  @BeanProperty
  var database = "servicemix"

  @BeanProperty
  var collection = "camel"

  lazy val mongoCollection: DBCollection = MongoConnection(host, port).getDB(database).getCollection(collection)

  override def process(exchange: Exchange) = {
    try {
      // use Camel's breadcrumb id by default, fall-back to the plain exchange id if that's not available
      val id = Option(exchange.getIn.getHeader(Exchange.BREADCRUMB_ID, classOf[String])).getOrElse(exchange.getExchangeId)
      persist(id, toMongo(exchange))
    } catch {
      case e: Exception => logger.error("Error while saving exchange in MongoDB", e)
    }
  }

  /*
   * Convert a Camel exchange to a MongoDB representation
   */
  def toMongo(exchange: Exchange) : MongoDBObject = {
    val properties = MongoDBObject()
    for (key <- exchange.getProperties.keySet()) {
      properties.put(key, exchange.getProperty(key, classOf[String]))
    }

    val result = MongoDBObject("id" -> exchange.getExchangeId,
      "properties" -> properties,
      "in" -> toMongo(exchange.getIn))

    if (exchange.hasOut) {
      result.put("out", toMongo(exchange.getOut))
    }

    result
  }

  /*
   * Convert a Camel message to a MongoDB representation
   */
  def toMongo(message: Message) : MongoDBObject = {
    val headers = MongoDBObject()
    for (key <- message.getHeaders.keySet()) {
      headers.put(key, message.getHeader(key, classOf[String]))
    }

    MongoDBObject("body" -> Option(message.getBody(classOf[String])).getOrElse(""),
      "headers" -> headers)
  }

  def persist(id: String, value: commons.MongoDBObject) = {
    val query = MongoDBObject("breadcrumb" -> id)
    val update = $set("breadcrumb" -> id) ++ $push("exchanges" -> value)
    mongoCollection.update(query, update, true, false)
  }

}
