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

package com.skanders.commons.convert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.skanders.commons.def.SkandersException;
import com.skanders.commons.result.Resulted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class FromNode
{
    private static final Logger LOG = LoggerFactory.getLogger(FromNode.class);

    private static final String PATH_DELIM = "\\.";

    /**
     * Retrieves the optional value as a Boolean
     *
     * @param path dot delimited path to value in yaml
     * @return the value as a Boolean, or null if not found
     */
    public Boolean toBool(JsonNode node, String path)
    {
        JsonNode value = walkNode(node, path);

        return value == null ? null : value.asBoolean();
    }

    /**
     * Retrieves the optional value as a Integer
     *
     * @param path dot delimited path to value in yaml
     * @return the value as a Integer, or null if not found
     */
    public Integer toInt(JsonNode node, String path)
    {
        JsonNode value = walkNode(node, path);

        return value == null ? null : value.asInt();
    }

    /**
     * Retrieves the optional value as a Long
     *
     * @param path dot delimited path to value in yaml
     * @return the value as a Long, or null if not found
     */
    public Long toLong(JsonNode node, String path)
    {
        JsonNode value = walkNode(node, path);

        return value == null ? null : value.asLong();
    }

    /**
     * Retrieves the optional value as a Double
     *
     * @param path dot delimited path to value in yaml
     * @return the value as a Double, or null if not found
     */
    public Double toDouble(JsonNode node, String path)
    {
        JsonNode value = walkNode(node, path);

        return value == null ? null : value.asDouble();
    }

    /**
     * Retrieves the optional value as a String
     *
     * @param path dot delimited path to value in yaml
     * @return the value as a String, or null if not found
     */
    public String toStr(JsonNode node, String path)
    {
        JsonNode value = walkNode(node, path);

        return value == null ? null : value.asText();
    }

    /**
     * Retrieves the optional value as a Boolean
     *
     * @param path dot delimited path to value in yaml
     * @param defaultValue value to be returned if path is not found
     * @return the value as a Boolean, or defaultValue if not found
     */
    public Boolean toBool(JsonNode node, String path, Boolean defaultValue)
    {
        Boolean value = toBool(node, path);

        return value == null ? defaultValue : value;
    }

    /**
     * Retrieves the optional value as a Integer
     *
     * @param path dot delimited path to value in yaml
     * @param defaultValue value to be returned if path is not found
     * @return the value as a Integer, or defaultValue if not found
     */
    public Integer toInt(JsonNode node, String path, Integer defaultValue)
    {
        Integer value = toInt(node, path);

        return value == null ? defaultValue : value;
    }

    /**
     * Retrieves the optional value as a Long
     *
     * @param path dot delimited path to value in yaml
     * @param defaultValue value to be returned if path is not found
     * @return the value as a Long, or defaultValue if not found
     */
    public Long toLong(JsonNode node, String path, Long defaultValue)
    {
        Long value = toLong(node, path);

        return value == null ? defaultValue : value;
    }

    /**
     * Retrieves the optional value as a Double
     *
     * @param path dot delimited path to value in yaml
     * @param defaultValue value to be returned if path is not found
     * @return the value as a Double, or defaultValue if not found
     */
    public Double toDouble(JsonNode node, String path, Double defaultValue)
    {
        Double value = toDouble(node, path);

        return value == null ? defaultValue : value;
    }

    /**
     * Retrieves the optional value as a String
     *
     * @param path dot delimited path to value in yaml
     * @param defaultValue value to be returned if path is not found
     * @return the value as a String, or defaultValue if not found
     */
    public String toStr(JsonNode node, String path, String defaultValue)
    {
        String value = toStr(node, path);

        return value == null ? defaultValue : value;
    }

    /**
     * Retrieves the required value as a Boolean
     *
     * @param path dot delimited path to value in yaml
     * @return the value as a Boolean
     * @throws SkandersException if the path is not found
     */
    public Boolean toReqBool(JsonNode node, String path)
    {
        Boolean value = toBool(node, path);

        requiredValue(path, value);

        return value;
    }

    /**
     * Retrieves the required value as a Integer
     *
     * @param path dot delimited path to value in yaml
     * @return the value as a Integer
     * @throws SkandersException if the path is not found
     */
    public Integer toReqInt(JsonNode node, String path)
    {
        Integer value = toInt(node, path);

        requiredValue(path, value);

        return value;
    }

    /**
     * Retrieves the required value as a Long
     *
     * @param path dot delimited path to value in yaml
     * @return the value as a Long
     * @throws SkandersException if the path is not found
     */
    public Long toReqLong(JsonNode node, String path)
    {
        Long value = toLong(node, path);

        requiredValue(path, value);

        return value;
    }

    /**
     * Retrieves the required value as a Double
     *
     * @param path dot delimited path to value in yaml
     * @return the value as a Double
     * @throws SkandersException if the path is not found
     */
    public Double toReqDouble(JsonNode node, String path)
    {
        Double value = toDouble(node, path);

        requiredValue(path, value);

        return value;
    }

    /**
     * Retrieves the required value as a String
     *
     * @param path dot delimited path to value in yaml
     * @return the value as a String
     * @throws SkandersException if the path is not found
     */
    public String toReqStr(JsonNode node, String path)
    {
        String value = toStr(node, path);

        requiredValue(path, value);

        return value;
    }

    /**
     * Retrieves the optional value as an List
     *
     * @param path dot delimited path to value in yaml
     * @param classType array class type
     * @param <T> List type to be determined by storing value
     * @return the value as an List, or null if not found
     */
    public <T> List<T> toArray(JsonNode node, String path, Class<T> classType)
    {
        JsonNode value = walkNode(node, path);

        if (value == null)
            return null;

        CollectionType type = Mapper.forJson().getTypeFactory().constructCollectionType(List.class, classType);

        return Mapper.forJson().convertValue(value, type);
    }

    /**
     * Retrieves the required value as an List
     *
     * @param path dot delimited path to value in yaml
     * @param classType array class type
     * @param <T> List type to be determined by storing value
     * @return the value as an List
     * @throws SkandersException if the path is not found
     */
    public <T> List<T> toReqArray(JsonNode node, String path, Class<T> classType)
    {
        List<T> value = toArray(node, path, classType);

        requiredValue(path, value);

        return value;
    }

    /**
     * Retrieves the optional value as a Map
     *
     * @param path dot delimited path to value in yaml
     * @param keyClass key class type
     * @param valueClass value class type
     * @param <T> Map key type to be determined by storing value
     * @param <S> Map value type to be determined by storing value
     * @return the value as a Map, or null if not found
     */
    public <T, S> Map<T, S> toMap(JsonNode node, String path, Class<T> keyClass, Class<S> valueClass)
    {
        JsonNode value = walkNode(node, path);

        if (value == null)
            return null;

        MapType type = Mapper.forJson().getTypeFactory().constructMapType(Map.class, keyClass, valueClass);

        return Mapper.forJson().convertValue(value, type);
    }

    /**
     * Retrieves the required value as a Map
     *
     * @param path dot delimited path to value in yaml
     * @param keyClass key class type
     * @param valueClass value class type
     * @param <T> Map key type to be determined by storing value
     * @param <S> Map value type to be determined by storing value
     * @return the value as a Map
     * @throws SkandersException if the path is not found
     */
    public <T, S> Map<T, S> toReqMap(JsonNode node, String path, Class<T> keyClass, Class<S> valueClass)
    {
        Map<T, S> value = toMap(node, path, keyClass, valueClass);

        requiredValue(path, value);

        return value;
    }

    /**
     * Retrieves the optional value as a POJO
     *
     * @param path dot delimited path to value in yaml
     * @param pojoClass pojo class
     * @param <T> pojo class type
     * @return the value as a POJO, or null if not found
     */
    public <T> T toPojo(JsonNode node, String path, Class<T> pojoClass)
    {
        JsonNode value = walkNode(node, path);

        if (value == null)
            return null;

        Resulted<T> pojo = ToPojo.fromNode(value, pojoClass);

        if (pojo.notValid())
            throw new SkandersException("Failed to map pojo " + pojo.result().message());

        return pojo.value();
    }

    /**
     * Retrieves the required value as a POJO
     *
     * @param path dot delimited path to value in yaml
     * @param pojoClass pojo class
     * @param <T> pojo class type
     * @return the value as a POJO
     * @throws SkandersException if the path is not found
     */
    public <T> T toReqPojo(JsonNode node, String path, Class<T> pojoClass)
    {
        T value = toPojo(node, path, pojoClass);

        requiredValue(path, value);

        return value;
    }

    /**
     * Retrieves the path as a JsonNode
     *
     * @param path dot delimited path to value in yaml
     * @return an instance of JsonNode, or null if not found
     */
    private JsonNode walkNode(JsonNode node, String path)
    {
        JsonNode value = node;

        for (String key : path.split(PATH_DELIM)) {
            value = value.get(key);

            if (value == null)
                return null;
        }

        return value;
    }

    /**
     * Checks if a value is missing, if it is a Error is thrown with a message
     * stating which path was not found.
     *
     * @param path dot delimited path to value in yaml
     * @param ob object to check
     * @throws SkandersException if the object is null
     */
    private void requiredValue(String path, Object ob)
    {
        if (ob == null)
            throw new SkandersException("Required path: '" + path + "' not found.");
    }
}
