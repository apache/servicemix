/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.servicemix.nmr.examples.interceptors.exchange;

import org.apache.servicemix.nmr.api.Exchange;
import org.apache.servicemix.nmr.api.Role;
import org.apache.servicemix.nmr.api.Status;
import org.apache.servicemix.nmr.api.event.ExchangeListener;
import org.apache.servicemix.nmr.api.internal.InternalExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This listener will be called each time an exchange is sent
 * or delivered to an endpoint on NMR.
 */
public class CustomExchangeListener implements ExchangeListener {

    private final Logger logger = LoggerFactory.getLogger(CustomExchangeListener.class);

    /**
     * Method called each time an exchange is sent
     *
     * @param exchange the exchange sent
     */
    public void exchangeSent(Exchange exchange) {
         try {
             logger.info("Sending exchange: {}", exchange);
             // Intercept exchanges
             if (exchange instanceof InternalExchange &&
                 exchange.getStatus() == Status.Active &&
                 exchange.getRole() == Role.Consumer &&
                 exchange.getOut(false) == null &&
                 exchange.getFault(false) == null) {
                 String id = ((InternalExchange) exchange).getSource().getId();
                 logger.info("Source endpoint: {}", id);
             }
         } catch (Throwable t) {
             logger.warn("Caught exception while processing exchange: {}", t, t);
         }
    }

    /**
     * Method called each time an exchange is delivered
     *
     * @param exchange the delivered exchange
     */
    public void exchangeDelivered(Exchange exchange) {
        try {
            logger.info("Receiving exchange: {}", exchange);
            if (exchange.getStatus() == Status.Active &&
                exchange.getRole() == Role.Provider &&
                exchange.getOut(false) == null &&
                exchange.getFault(false) == null &&
                exchange instanceof InternalExchange) {
                String id = ((InternalExchange) exchange).getDestination().getId();
                logger.info("Dest endpoint: {}", id);
            }
        } catch (Throwable t) {
            logger.warn("Caught exception while processing exchange: {}", t, t);
        }
    }

    /**
     * Method called when an exchange resulted in an exception to be
     * thrown and the exchange not delivered.  This can happen if no
     * endpoint can be found for the target or if something else bad
     * happened.
     *
     * @param exchange the exchange that failed
     */
    public void exchangeFailed(Exchange exchange) {
        logger.info("Exchange Failed: {}", exchange);
    }

}
