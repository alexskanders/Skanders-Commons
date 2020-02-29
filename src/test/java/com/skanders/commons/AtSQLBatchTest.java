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

package com.skanders.commons;

import com.skanders.commons.atsql.AtSQLBatch;
import com.skanders.commons.result.Resulted;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Types;

public class AtSQLBatchTest
{
    @BeforeAll
    public static void clearDb()
    {
        String query = "DELETE FROM student WHERE id > 0;";

        Resources.AT_SQL.newQuery(query).executeUpdate();
    }

    @Test
    public void batchInsert()
    {
        String query = "" +
                "INSERT INTO student " +
                "     (id, name, age, major, year)" +
                "VALUES" +
                "     (?,?,?,?,?)";

        AtSQLBatch atSQLBatch = Resources.AT_SQL.newBatch(query)
                .setBatchList(1, "Student1", 18, "CS", 1)
                .setBatchList(2, "Student2", 19, "CS", 2)
                .setBatchList(3, "Student3", 20, "CS", 3)
                .setBatchList(4, "Student4", 21, "CS", 4)
                .setBatchList(5, "Student5", 22, "CS", 5);

        Resulted<int[]> results = atSQLBatch.executeBatch();

        System.out.println(results.notValid());
        System.out.println(results.result());

        for (int i : results.value())
            System.out.println(i);
    }

    @Test
    public void batchInsertAdd()
    {
        String query = "" +
                "INSERT INTO student " +
                "     (id, name, age, major, year)" +
                "VALUES" +
                "     (?,?,?,?,?)";

        AtSQLBatch atSQLBatch = Resources.AT_SQL.newBatch(query);

        for (int i = 6; i <= 10; i++) {
            atSQLBatch
                    .add(i)
                    .add("Student" + i)
                    .add(i + 17)
                    .add("CS")
                    .add(i)
                    .addBatchList();
        }


        Resulted<int[]> results = atSQLBatch.executeBatch();

        System.out.println(results.notValid());
        System.out.println(results.result());

        for (int i : results.value())
            System.out.println(i);
    }

    @Test
    public void batchInsertAddPair()
    {
        String query = "" +
                "INSERT INTO student " +
                "     (id, name, age, major, year)" +
                "VALUES" +
                "     (?,?,?,?,?)";

        AtSQLBatch atSQLBatch = Resources.AT_SQL.newBatch(query);

        for (int i = 11; i <= 15; i++) {
            atSQLBatch
                    .add(Types.INTEGER, i)
                    .add(Types.VARCHAR, "Student" + i)
                    .add(Types.INTEGER, i + 17)
                    .add(Types.VARCHAR, "CS")
                    .add(Types.INTEGER, i)
                    .addBatchList();
        }

        Resulted<int[]> results = atSQLBatch.executeBatch();

        System.out.println(results.notValid());
        System.out.println(results.result());

        for (int i : results.value())
            System.out.println(i);
    }
}
