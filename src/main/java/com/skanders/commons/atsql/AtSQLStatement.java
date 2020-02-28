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


import com.skanders.commons.def.Verify;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class AtSQLStatement implements AutoCloseable
{
    private AtSQL             atSQL;
    private Connection        connection;
    private PreparedStatement preparedStatement;

    private boolean closed;

    private AtSQLStatement(
            AtSQL atSQL, Connection connection,
            PreparedStatement preparedStatement)
    {
        this.atSQL             = atSQL;
        this.connection        = connection;
        this.preparedStatement = preparedStatement;
        this.closed            = false;
    }

    static AtSQLStatement newManager(
            @Nonnull AtSQL atSQL, @Nonnull Connection connection,
            @Nonnull PreparedStatement preparedStatement)
    {
        Verify.notNull(atSQL, "poolManager Cannot be Null");
        Verify.notNull(connection, "connection Cannot be Null");

        return new AtSQLStatement(atSQL, connection, preparedStatement);
    }

    void setParams(AtSQLParamList atSQLParamList)
            throws SQLException
    {
        int count = 1;

        for (AtSQLParam atSQLParam : atSQLParamList.getList())
            if (atSQLParam.getType() == null)
                preparedStatement.setObject(count++, atSQLParam.getValue());
            else
                preparedStatement.setObject(count++, atSQLParam.getValue(), atSQLParam.getType());
    }

    void setBatch(AtSQLParamList atSQLParamList)
            throws SQLException
    {
        setParams(atSQLParamList);

        preparedStatement.addBatch();
    }

    int[] executeBatch()
            throws SQLException
    {
        return preparedStatement.executeBatch();
    }

    int executeUpdate()
            throws SQLException
    {
        return preparedStatement.executeUpdate();
    }

    ResultSet executeQuery()
            throws SQLException
    {
        return preparedStatement.executeQuery();
    }

    @Override
    public void close()
    {
        if (!this.closed) {
            atSQL.releaseCon(connection);
            this.closed = true;
        }
    }
}

