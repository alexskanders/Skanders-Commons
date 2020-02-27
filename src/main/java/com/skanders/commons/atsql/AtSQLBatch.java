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
import com.skanders.commons.def.SkandersException;
import com.skanders.commons.def.SkandersVerify;
import com.skanders.commons.result.Resulted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AtSQLBatch
{
    private static final Logger LOG = LoggerFactory.getLogger(AtSQLBatch.class);

    private enum BatchType
    {NONE, SINGLE, LIST}

    private final String               query;
    private final AtSQL                atSQL;
    private final List<AtSQLParamList> atSQLParamList;

    private AtSQLParamList singleList;

    private boolean   closed;
    private BatchType batchType;

    AtSQLBatch(String query, @Nonnull AtSQL atSQL)
    {
        SkandersVerify.checkNull(atSQL, "poolManager cannot be null.");

        this.query          = query;
        this.atSQL          = atSQL;
        this.atSQLParamList = new ArrayList<>();
        this.closed         = false;

        this.batchType = BatchType.NONE;
    }

    public AtSQLBatch setBatchList(Object... params)
    {
        setBatchType(BatchType.LIST);

        atSQLParamList.add(new AtSQLParamList(params));

        return this;
    }

    public AtSQLBatch add(int type, Object value)
    {
        setBatchType(BatchType.SINGLE);

        if (singleList == null)
            singleList = new AtSQLParamList();

        singleList.setPair(type, value);

        return this;
    }


    public AtSQLBatch add(Object value)
    {
        setBatchType(BatchType.SINGLE);

        if (singleList == null)
            singleList = new AtSQLParamList();

        singleList.set(value);

        return this;
    }

    public AtSQLBatch setBatchList()
    {
        atSQLParamList.add(singleList);
        singleList = null;

        return this;
    }

    private void setBatchType(BatchType batchType)
    {
        switch (batchType) {
            case NONE:
                this.batchType = batchType;
                return;
            case LIST:
                if (this.batchType != BatchType.LIST)
                    throw new SkandersException("Cannot switch from using add() to setBatch()");
            case SINGLE:
                if (this.batchType != BatchType.SINGLE)
                    throw new SkandersException("Cannot switch from using setBatch() to add()");
            default:
                // continue
        }
    }

    public Resulted<int[]> executeBatch()
    {
        SkandersVerify.argument(closed, "SQLQuery cannot be called after closed");
        SkandersVerify.argument(singleList != null, "using add() requires the use of setBatchList() between set lists");

        this.closed = true;

        LOG.debug(LogPattern.ENTER, "Database Execute Update");

        try (AtSQLStatement atSQLStatement = atSQL.createBatchStatement(query)) {

            for (AtSQLParamList params : atSQLParamList)
                atSQLStatement.setBatch(params);

            int[] rowUpdates = atSQLStatement.executeBatch();

            return Resulted.inValue(rowUpdates);

        } catch (SQLException e) {
            LOG.error(LogPattern.EXIT_FAIL, "Prepare Database Update Execution", e.getClass(), e.getMessage());

            return Resulted.inException(e);

        }
    }
}
