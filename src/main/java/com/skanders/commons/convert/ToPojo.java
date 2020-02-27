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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.*;
import com.skanders.commons.def.LogPattern;
import com.skanders.commons.def.SkandersResult;
import com.skanders.commons.result.Result;
import com.skanders.commons.result.Resulted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

class ToPojo
{
    private static final Logger LOG = LoggerFactory.getLogger(ToPojo.class);

    public static <T> Resulted<T> fromNode(
            JsonNode jsonNode, Class<T> className, DeserializationFeature... feature)
    {
        return fromNode(Mapper.forJson(), jsonNode, className, feature);
    }

    public static <T> Resulted<T> fromJsonStream(
            InputStream jsonText, Class<T> className, DeserializationFeature... feature)
    {
        return fromInputStream(Mapper.forJson(), jsonText, className, feature);
    }

    public static <T> Resulted<T> fromJsonFile(
            File jsonFile, Class<T> className, DeserializationFeature... feature)
    {
        return fromFile(Mapper.forJson(), jsonFile, className, feature);
    }

    public static <T> Resulted<T> fromJsonString(
            String jsonText, Class<T> className, DeserializationFeature... feature)
    {
        return fromString(Mapper.forJson(), jsonText, className, feature);
    }

    public static <T> Resulted<T> fromJsonBytes(
            byte[] jsonBytes, Class<T> className, DeserializationFeature... feature)
    {
        return fromBytes(Mapper.forJson(), jsonBytes, className, feature);
    }

    public static <T> Resulted<T> fromXmlStream(
            InputStream xmlText, Class<T> className, DeserializationFeature... feature)
    {
        return fromInputStream(Mapper.forXml(), xmlText, className, feature);
    }

    public static <T> Resulted<T> fromXmlFile(
            File xmlFile, Class<T> className, DeserializationFeature... feature)
    {
        return fromFile(Mapper.forXml(), xmlFile, className, feature);
    }

    public static <T> Resulted<T> fromXmlString(
            String xmlText, Class<T> className, DeserializationFeature... feature)
    {
        return fromString(Mapper.forXml(), xmlText, className, feature);
    }

    public static <T> Resulted<T> fromXmlBytes(
            byte[] xmlBytes, Class<T> className, DeserializationFeature... feature)
    {
        return fromBytes(Mapper.forXml(), xmlBytes, className, feature);
    }

    public static <T> Resulted<T> fromYamlStream(
            InputStream yamlText, Class<T> className, DeserializationFeature... feature)
    {
        return fromInputStream(Mapper.forYaml(), yamlText, className, feature);
    }

    public static <T> Resulted<T> fromYamlFile(
            File yamlFile, Class<T> className, DeserializationFeature... feature)
    {
        return fromFile(Mapper.forYaml(), yamlFile, className, feature);
    }

    public static <T> Resulted<T> fromYamlString(
            String yamlText, Class<T> className, DeserializationFeature... feature)
    {
        return fromString(Mapper.forYaml(), yamlText, className, feature);
    }

    public static <T> Resulted<T> fromYamlBytes(
            byte[] yamlBytes, Class<T> className, DeserializationFeature... feature)
    {
        return fromBytes(Mapper.forYaml(), yamlBytes, className, feature);
    }

    private static <T> Resulted<T> fromInputStream(
            ObjectMapper mapper, InputStream inputStream, Class<T> className, DeserializationFeature... feature)
    {
        try {
            ObjectReader reader = (feature == null || feature.length == 0) ?
                                  mapper.readerFor(className) :
                                  mapper.readerFor(className).withFeatures(feature);

            return Resulted.inValue(reader.readValue(inputStream));

        } catch (IOException e) {
            LOG.error(LogPattern.ERROR, e.getClass(), e.getMessage());
            return Resulted.inResult(convert(e));

        }
    }

    private static <T> Resulted<T> fromFile(
            ObjectMapper mapper, File fileName, Class<T> className, DeserializationFeature... feature)
    {
        try {
            ObjectReader reader = createReader(mapper, className, feature);

            return Resulted.inValue(reader.readValue(fileName));

        } catch (IOException e) {
            LOG.error(LogPattern.ERROR, e.getClass(), e.getMessage());
            return Resulted.inResult(convert(e));

        }
    }

    private static <T> Resulted<T> fromString(
            ObjectMapper mapper, String text, Class<T> className, DeserializationFeature... feature)
    {
        try {
            ObjectReader reader = createReader(mapper, className, feature);

            return Resulted.inValue(reader.readValue(text));

        } catch (IOException e) {
            LOG.error(LogPattern.ERROR, e.getClass(), e.getMessage());
            return Resulted.inResult(convert(e));

        }
    }

    private static <T> Resulted<T> fromNode(
            ObjectMapper mapper, JsonNode node, Class<T> className, DeserializationFeature... feature)
    {
        try {
            ObjectReader reader = createReader(mapper, className, feature);

            return Resulted.inValue(reader.treeToValue(node, className));

        } catch (IOException e) {
            LOG.error(LogPattern.ERROR, e.getClass(), e.getMessage());
            return Resulted.inResult(convert(e));

        }
    }

    private static <T> Resulted<T> fromBytes(
            ObjectMapper mapper, byte[] bytes, Class<T> className, DeserializationFeature... feature)
    {
        try {
            ObjectReader reader = createReader(mapper, className, feature);

            return Resulted.inValue(reader.readValue(bytes));

        } catch (IOException e) {
            LOG.error(LogPattern.ERROR, e.getClass(), e.getMessage());
            return Resulted.inResult(convert(e));

        }
    }

    private static <T> ObjectReader createReader(
            ObjectMapper mapper, Class<T> className, DeserializationFeature... feature)
    {
        return (feature == null || feature.length == 0) ?
               mapper.readerFor(className) :
               mapper.readerFor(className).withFeatures(feature);
    }

    private static Result convert(Exception e)
    {
        if (e instanceof JsonMappingException) {
            return SkandersResult.JSON_MAPPING_EXCEPT;

        } else if (e instanceof JsonParseException) {
            return SkandersResult.JSON_PARSE_EXCEPT;

        } else {
            return Result.exception(e);

        }
    }
}
