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
package org.apache.servicemix.examples.drools.camel.blueprint.osgi;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Function Test datasource
 *
 */
public class DataSourceTest {
    /**
     * LOGGER
     */
    private static final Logger log = LoggerFactory
            .getLogger(DataSourceTest.class);

    /**
     * DataSource
     */
    private DataSource datasource;

    /**
     * Set Data source
     * 
     * @param datasource
     */
    public void setDatasource(DataSource datasource) {
        this.datasource = datasource;
    }

    /**
     * Method invoke query with neutral SQL
     * 
     * @throws SQLException
     */
    public void test() throws SQLException {
        log.info("Connection test begin");
        try {
            Connection conn = datasource.getConnection();
            Statement statm = conn.createStatement();
            statm.execute("select 1 from INFORMATION_SCHEMA.SYSTEM_USERS");
            statm.close();
            conn.close();
            log.info("Ypi Connection is OK!");
        } catch (SQLException e) {
            log.error("Ups", e);
            throw e;
        }
    }
}
