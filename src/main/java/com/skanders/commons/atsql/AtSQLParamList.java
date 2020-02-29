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

import java.util.ArrayList;
import java.util.List;

class AtSQLParamList
{
    private List<AtSQLParam> atSQLParamList;

    AtSQLParamList()
    {
        this.atSQLParamList = new ArrayList<>();
    }

    AtSQLParamList(Object... values)
    {
        this.atSQLParamList = new ArrayList<>();

        for (Object value : values)
            atSQLParamList.add(new AtSQLParam(value));
    }

    public static AtSQLParamList create()
    {
        return new AtSQLParamList();
    }

    public void setPair(int type, Object value)
    {
        atSQLParamList.add(new AtSQLParam(type, value));
    }

    public void set(Object value)
    {
        atSQLParamList.add(new AtSQLParam(value));
    }

    public void setList(Object... values)
    {
        for (Object value : values)
            atSQLParamList.add(new AtSQLParam(value));
    }

    List<AtSQLParam> getList()
    {
        return atSQLParamList;
    }
}
