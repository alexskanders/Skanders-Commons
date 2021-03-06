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

import com.skanders.commons.Resources;
import com.skanders.commons.result.Resulted;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Types;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class AtSQLQueryTest
{
    @BeforeAll
    public static void clearDb()
    {
        String query = "DELETE FROM student WHERE id > 0;";

        Resources.AT_SQL.createQuery(query).executeUpdate();
    }

    @Test
    public void querySetListTest()
    {
        String query = "\n" +
                "INSERT INTO student \n" +
                "     (id, name, age, major, year) \n" +
                "VALUES \n" +
                "     (?,?,?,?,?)";

        AtSQLQuery atSQLQuery = Resources.AT_SQL.createQuery(query)
                .setList(1, "Student1", 18, "CS", 1);

        Resulted<Integer> resulted = atSQLQuery.executeUpdate();

        assertFalse(resulted.notValid());
    }

    @Test
    public void querySetTest()
    {
        String query = "\n" +
                "INSERT INTO student \n" +
                "     (id, name, age, major, year) \n" +
                "VALUES \n" +
                "     (?,?,?,?,?)";

        AtSQLQuery atSQLQuery = Resources.AT_SQL.createQuery(query)
                .set(2)
                .set("Student2")
                .set(19)
                .set("CS")
                .set(2);

        Resulted<Integer> resulted = atSQLQuery.executeUpdate();

        assertFalse(resulted.notValid());
    }

    @Test
    public void querySetTypeTest()
    {
        String query = "\n" +
                "INSERT INTO student \n" +
                "     (id, name, age, major, year) \n" +
                "VALUES \n" +
                "     (?,?,?,?,?)";

        AtSQLQuery atSQLQuery = Resources.AT_SQL.createQuery(query)
                .set(Types.INTEGER, 3)
                .set(Types.VARCHAR, "Student3")
                .set(Types.INTEGER, 20)
                .set(Types.VARCHAR, "CS")
                .set(Types.INTEGER, 3);

        Resulted<Integer> resulted = atSQLQuery.executeUpdate();

        assertFalse(resulted.notValid());
    }
}
