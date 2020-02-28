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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.sql.ResultSet;

public class AtSQLResult implements AutoCloseable
{
    private static final Logger LOG = LoggerFactory.getLogger(AtSQLResult.class);

    private AtSQLStatement atSQLStatement;
    private ResultSet      resultSet;

    private AtSQLResult(AtSQLStatement atSQLStatement, ResultSet resultSet)
    {
        this.atSQLStatement = atSQLStatement;
        this.resultSet      = resultSet;
    }

    static AtSQLResult newInstance(@Nonnull AtSQLStatement atSQLStatement, @Nonnull ResultSet resultSet)
    {
        Verify.notNull(atSQLStatement, "queryManager cannot be null");
        Verify.notNull(resultSet, "resultSet cannot be null");

        return new AtSQLResult(atSQLStatement, resultSet);
    }

    public ResultSet getResultSet()
    {
        return resultSet;
    }

    @Override
    public void close()
    {
        atSQLStatement.close();
    }
}

