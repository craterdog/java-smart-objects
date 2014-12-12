/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package craterdog.smart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import static org.hamcrest.CoreMatchers.containsString;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * This unit test class runs various tests using the <code>SmartObjectMapper</code> class.
 *
 * @author Jeff Webb
 */
public class SmartObjectMapperTest {

    private final XLogger logger = XLoggerFactory.getXLogger(SmartObjectMapperTest.class);
    private final SmartObjectMapper mapper = new SmartObjectMapper();

    @Test
    public void testDateConfiguration() throws IOException {
        logger.entry();

        // NOTE that this test shows that a java.util.Date gets seralized as though
        // it were a UTC date
        long _now = System.currentTimeMillis();
        Date now = new Date(_now);
        HashMap<String, Date> map = new HashMap<>();
        String nowString = new DateTime(_now, DateTimeZone.UTC).toString(DateTimeFormat.forPattern("y-MM-dd'T'HH:mm:ss.SSSZ"));
        map.put("now", now);

        String repr = mapper.writeValueAsString(map);
        logger.info(repr);
        logger.info("looking for {}", nowString);
        assertThat(repr, containsString("\"now\" : \"" + nowString + "\""));
        map = mapper.readValue(repr, new TypeReference<HashMap<String, Date>>() {
        });
        assertEquals(now, map.get("now"));

        logger.exit();
    }

    @Test
    public void testJodaDateHandling() throws JsonProcessingException {
        logger.entry();

        // without the JodaModule joda types such as DateTime aren't properly
        // serialized as dates
        HashMap<String, DateTime> map = new HashMap<>();
        map.put("now", new DateTime(DateTimeZone.UTC));
        String nowString = ISODateTimeFormat.dateTime().print(map.get("now"));
        String repr = mapper.writeValueAsString(map);
        logger.info(repr);
        assertThat(repr, containsString("\"now\" : \"" + nowString + "\""));

        logger.exit();
    }

    @Test
    public void testNullsSuppressedInMaps() throws IOException {
        logger.entry();

        HashMap<String, HashMap<String, String>> map = new HashMap<>();
        HashMap<String, String> innermap = new HashMap<>();
        innermap.put("nonnull", "nonnull");
        innermap.put("null", null);
        map.put("inner", innermap);

        String repr = mapper.writeValueAsString(map);
        assertEquals("{\"inner\":{\"nonnull\":\"nonnull\"}}", repr.replaceAll("\\s", ""));

        logger.exit();
    }

    @Test
    public void testNullPropertiesSuppressed() throws IOException {
        logger.entry();

        class TestProperties {

            public String nonnull = "nonnull";
            public String _null = null;
        }

        String repr = mapper.writeValueAsString(new TestProperties());
        assertEquals("{\"nonnull\":\"nonnull\"}", repr.replaceAll("\\s", ""));

        logger.exit();
    }

    @Test
    public void testEmptyObject() throws IOException {
        logger.entry();

        class TestObject extends SmartObject<TestObject> {
        }

        String repr = mapper.writeValueAsString(new TestObject());
        assertEquals("{ }", repr);

        logger.exit();
    }

    static class PolymorphicTestObject extends SmartObject<PolymorphicTestObject> {

        public String testAttribute = "test";
    }

    static <E extends SmartObject<E>> E polymorphicReader(SmartObjectMapper mapper) throws IOException {
        return mapper.reader(SmartObject.class).readValue("{\"testAttribute\":\"success\"}");
    }

    @Test
    public void testPolymorphicObject() throws IOException {
        logger.entry();
        SimpleModule module = new SimpleModule();
        module.addAbstractTypeMapping(SmartObject.class, PolymorphicTestObject.class);
        mapper.registerModule(module);
        PolymorphicTestObject polyObject = polymorphicReader(mapper);
        assertEquals("success", polyObject.testAttribute);
        logger.exit();
    }

}
