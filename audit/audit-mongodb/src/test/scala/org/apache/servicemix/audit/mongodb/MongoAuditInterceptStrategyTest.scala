package org.apache.servicemix.audit.mongodb

import org.apache.camel.test.junit4.CamelTestSupport
import org.apache.camel.{Processor, Exchange, CamelContext}
import com.mongodb.DBCollection
import com.mongodb.casbah.commons.MongoDBObject
import scala.collection.mutable.ListBuffer
import org.apache.camel.builder.RouteBuilder
import org.junit.Test
import org.junit.Assert._
import java.io.{ObjectOutputStream, ByteArrayOutputStream}
import org.apache.servicemix.core.camel.ServiceMix

/**
 * Basic test for the {@link MongoAuditProcessor}
 */
class MongoAuditInterceptStrategyTest extends CamelTestSupport {

  val servicemix = ServiceMix()

  val persisted = scala.collection.mutable.Map.empty[String, Seq[MongoDBObject]].withDefaultValue(Seq.empty[MongoDBObject])

  servicemix.addIntercept(new MongoAuditProcessor() {
    override def persist(id: String, value: MongoDBObject) : Unit = persisted.put(id, value +: persisted(id))
  })

  @Test
  def testSimplePersistence() {
    val mock = getMockEndpoint("mock:test")
    mock.expectedMessageCount(1)

    val exchange = template.send("direct:test", new Processor() {
      override def process(exchange: Exchange) = exchange.getIn.setBody("Initial body text")
    })

    val breadcrumb = exchange.getIn.getHeader(Exchange.BREADCRUMB_ID, classOf[String])

    assertMockEndpointsSatisfied()

    assertEquals("Exchange has been audited twice for the same breadcrumb id", 2, persisted(breadcrumb).size)

    for (dbo <- persisted(breadcrumb)) {
      assertEquals(exchange.getExchangeId, dbo.get("id").get)
      assertSome("Properties should be in the persistent object", dbo.get("properties"))
      assertSome("In message should be in the persistent object", dbo.get("in"))
    }

  }

  def assertSome(message: String, option: Option[AnyRef]) = option match {
    case None => fail(message)
    case _    =>
  }

  override def createRouteBuilder(): RouteBuilder = new RouteBuilder() {
    def configure() {
      from("direct:test").transform().simple("Transformed body text at ${date:now:yyyyMMdd-hhMMssSSS}").to("mock:test")
    }
  }
}
