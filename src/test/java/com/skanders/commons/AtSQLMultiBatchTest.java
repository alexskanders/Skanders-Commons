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

import com.skanders.commons.atsql.AtSQLMultiBatch;
import com.skanders.commons.result.Resulted;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Types;

public class AtSQLMultiBatchTest
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

        AtSQLMultiBatch atSQLBatch = Resources.AT_SQL.newMultiBatch()
                .setQuery(query).setList(1, "Student1", 18, "CS", 1)
                .setQuery(query).setList(2, "Student2", 19, "CS", 2)
                .setQuery(query).setList(3, "Student3", 20, "CS", 3)
                .setQuery(query).setList(4, "Student4", 21, "CS", 4)
                .setQuery(query).setList(5, "Student5", 22, "CS", 5);

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

        AtSQLMultiBatch atSQLBatch = Resources.AT_SQL.newMultiBatch();

        for (int i = 6; i <= 10; i++) {
            atSQLBatch
                    .setQuery(query)
                    .set(i)
                    .set("Student" + i)
                    .set(i + 17)
                    .set("CS")
                    .set(i);
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

        AtSQLMultiBatch atSQLBatch = Resources.AT_SQL.newMultiBatch();

        for (int i = 11; i <= 15; i++) {
            atSQLBatch
                    .setQuery(query)
                    .set(Types.INTEGER, i)
                    .set(Types.VARCHAR, "Student" + i)
                    .set(Types.INTEGER, i + 17)
                    .set(Types.VARCHAR, "CS")
                    .set(Types.INTEGER, i);
        }

        Resulted<int[]> results = atSQLBatch.executeBatch();

        System.out.println(results.notValid());
        System.out.println(results.result());

        for (int i : results.value())
            System.out.println(i);
    }
}
