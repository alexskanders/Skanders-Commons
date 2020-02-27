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

import com.skanders.commons.def.SkandersException;
import com.zaxxer.hikari.HikariConfig;

import java.util.Map;

public class AtSQLFactory
{
    private enum AuSQLType
    {NONE, JDBC_URL, DRIVER}

    private HikariConfig hikariConfig;
    private AuSQLType    auSQLType;

    private AtSQLFactory(
            String username, String password, long maxLifetime, int maxPoolSize)
    {
        this.hikariConfig = new HikariConfig();

        this.hikariConfig.setUsername(username);
        this.hikariConfig.setPassword(password);
        this.hikariConfig.setMaxLifetime(maxLifetime);
        this.hikariConfig.setMaximumPoolSize(maxPoolSize);

        this.auSQLType = AuSQLType.NONE;
    }

    private static AtSQLFactory newInstance(
            String username, String password, long maxLifetime, int maxPoolSize)
    {
        return new AtSQLFactory(username, password, maxLifetime, maxPoolSize);
    }

    public AtSQLFactory withDriver(
            String driver, String hostname, int port, String name)
    {
        setAuSQLType(AuSQLType.DRIVER);

        hikariConfig.setDataSourceClassName(driver);
        hikariConfig.addDataSourceProperty("serverName", hostname);
        hikariConfig.addDataSourceProperty("portNumber", port);
        hikariConfig.addDataSourceProperty("databaseName", name);

        return this;
    }

    public AtSQLFactory withJdbcUrl(String url)
    {
        setAuSQLType(AuSQLType.JDBC_URL);

        hikariConfig.setJdbcUrl(url);

        return this;
    }

    public AtSQLFactory withDataSourceProperties(Map<String, Object> dbProperties)
    {
        if (dbProperties != null)
            for (Object key : dbProperties.keySet())
                hikariConfig.addDataSourceProperty((String) key, dbProperties.get(key));

        return this;
    }

    public AtSQLFactory withMySQLPerformanceSettings()
    {
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", 250);
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        hikariConfig.addDataSourceProperty("cachePrepStmts", true);
        hikariConfig.addDataSourceProperty("useServerPrepStmts", true);
        hikariConfig.addDataSourceProperty("rewriteBatchedStatements", true);

        return this;
    }

    public AtSQL build()
    {
        return new AtSQL(hikariConfig);
    }

    private void setAuSQLType(AuSQLType auSQLType)
    {
        switch (auSQLType) {
            case NONE:
                this.auSQLType = auSQLType;
                return;
            case JDBC_URL:
                if (this.auSQLType != AuSQLType.DRIVER)
                    throw new SkandersException("Cannot set both Driver and JDBC, only one choice is allowed.");
            case DRIVER:
                if (this.auSQLType != AuSQLType.JDBC_URL)
                    throw new SkandersException("Cannot set both Driver and JDBC, only one choice is allowed.");
            default:
                // continue
        }
    }
}