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

package com.skanders.commons.config;


import com.skanders.commons.Resources;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigTest
{
    private static final String   ALGO   = "PBEWITHHMACSHA512ANDAES_256";
    private static final String   PASS   = "PASSWORD";
    private static final String[] NUMBER = new String[]{"One", "Two", "Three", "Four", "Five", "Six"};

    @Test
    public void testEncryptedFileArray()
    {
        final String file = Resources.getFilePath("test-encrypted-config.yaml");

        Config prop = Config.fromEncrypted(file, ALGO, PASS);

        List<String> array       = prop.getReqArray("Array", String.class);
        List<String> hybridArray = prop.getReqArray("Hybrid.Array", String.class);


        for (int i = 0; i < array.size(); i++)
             assertEquals("Array" + NUMBER[i], array.get(i));

        for (int i = 0; i < hybridArray.size(); i++)
             assertEquals("HybridArray" + NUMBER[i], hybridArray.get(i));

    }

    @Test
    public void testEncryptedFileMap()
    {
        final String file = Resources.getFilePath("test-encrypted-config.yaml");

        Config prop = Config.fromEncrypted(file, ALGO, PASS);

        Map<String, String> map            = prop.getReqMap("Map", String.class, String.class);
        List<Map>           hybridMapArray = prop.getReqArray("Hybrid.Map", Map.class);

        Map<String, String> hybridMap = (Map<String, String>) hybridMapArray.get(0).get("Map");

        for (String s : NUMBER)
            assertEquals("Map" + s, map.get(s));

        for (String s : NUMBER)
            assertEquals("HybridMap" + s, hybridMap.get(s));
    }
}
