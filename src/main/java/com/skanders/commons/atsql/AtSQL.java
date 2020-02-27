/*
 * Copyright (c) 2020 Alexander Iskander
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.skanders.commons.atsql;


import com.skanders.commons.def.LogPattern;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Autonomous SQL (AtSQL)
 * <p>
 * A SQL manager that manages most of the inner workings of SQL. With reliance
 * on {@link AutoCloseable} and {@link com.zaxxer.hikari.pool.HikariPool} most
 * of the resource management and query creation details are abstracted away.
 */
public class AtSQL
{
    private static final Logger LOG = LoggerFactory.getLogger(AtSQL.class);

    private HikariDataSource hikariDataSource;

    AtSQL(HikariConfig config)
    {
        LOG.trace(LogPattern.ENTER, "Connection Pool Constructor");

        hikariDataSource = new HikariDataSource(config);
    }

    public AtSQLBatch newBatch(@Nonnull String query)
    {
        return new AtSQLBatch(query, this);
    }

    public AtSQLQuery newQuery(@Nonnull String query)
    {
        return new AtSQLQuery(query, this);
    }

    AtSQLStatement createStatement(String query)
            throws SQLException
    {
        return newStatement(query, true);
    }

    AtSQLStatement createBatchStatement(String query)
            throws SQLException
    {
        return newStatement(query, false);
    }

    private AtSQLStatement newStatement(String query, boolean autoCommit)
            throws SQLException
    {
        LOG.trace(LogPattern.ENTER, "Request Connection");

        Connection connection = hikariDataSource.getConnection();

        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement(query);

            if (!autoCommit)
                connection.setAutoCommit(false);

        } catch (SQLException e) {
            hikariDataSource.evictConnection(connection);
            throw e;
        }

        return AtSQLStatement.newManager(this, connection, preparedStatement);
    }

    void releaseCon(Connection connection)
    {
        hikariDataSource.evictConnection(connection);
    }
}