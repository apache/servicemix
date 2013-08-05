package org.apache.servicemix.audit.mongodb

import com.mongodb.casbah.commons.MongoDBObject

/**
 * Adapter trait to decouple the connection from the actual interceptor for easier testing
 */
trait MongoAdapter {

  /**
   * Persist new exchange information for a given breadcrumb id
   *
   * @param id the breadcrumb id
   * @param value the extra exchange information
   */
  def persist(id: String, value: MongoDBObject)

}
