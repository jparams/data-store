package com.jparams.util;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * JsonTest utilities.
 */
public final class JsonTestUtils
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static
    {
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JsonTestUtils()
    {
        // utility class
    }

    public static <T> T loadResource(final String resourcePath, final Class<T> valueType)
    {
        try
        {
            return OBJECT_MAPPER.readValue(JsonTestUtils.class.getResourceAsStream(resourcePath), valueType);
        }
        catch (final IOException ioException)
        {
            throw new JsonReadException(ioException);
        }
    }

    private static class JsonReadException extends RuntimeException
    {
        public JsonReadException(final Throwable cause)
        {
            super(cause);
        }
    }
}