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
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * This unit test class runs various tests using the <code>SmartObjectMapper</code> class.
 *
 * @author Jeff Webb
 * @author Derk Norton
 */
public class SmartObjectMapperTest {

    static private final XLogger logger = XLoggerFactory.getXLogger(SmartObjectMapperTest.class);
    static private final SmartObjectMapper mapper = new SmartObjectMapper();


    /**
     * Log a message at the beginning of the tests.
     */
    @BeforeClass
    static public void setUpClass() {
        logger.info("Running SmartObjectMapper Unit Tests...\n");
    }


    /**
     * Log a message at the end of the tests.
     */
    @AfterClass
    static public void tearDownClass() {
        logger.info("Completed SmartObjectMapper Unit Tests.\n");
    }


    @Test
    public void testJavaDateFormatting() throws IOException {
        logger.info("Testing embedded Java Date formatting...");

        // NOTE that this test shows that a java.util.Date gets seralized as though
        // it were a UTC date
        long _now = System.currentTimeMillis();
        Date now = new Date(_now);
        HashMap<String, Date> map = new HashMap<>();
        String nowString = new DateTime(_now, DateTimeZone.UTC).toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        map.put("now", now);

        logger.info("  Converting a map with embedded date to JSON...");
        String jsonRepresentation = mapper.writeValueAsString(map);
        logger.info("  Confirming that {} contains {}", jsonRepresentation, nowString);
        assertThat(jsonRepresentation, containsString("\"now\" : \"" + nowString + "\""));

        logger.info("  Converting the JSON back to the map...");
        HashMap<String, Date> copy = mapper.readValue(jsonRepresentation, new TypeReference<HashMap<String, Date>>() { });
        assertEquals(map, copy);

        logger.info("Embedded Java Date manipulation testing completed.\n");
    }

    @Test
    public void testJodaDateFormatting() throws IOException {
        logger.info("Testing embedded Joda Date formatting...");

        // without the JodaModule joda types such as DateTime aren't properly
        // serialized as dates
        HashMap<String, DateTime> map = new HashMap<>();
        map.put("now", new DateTime(DateTimeZone.UTC));
        String nowString = ISODateTimeFormat.dateTime().print(map.get("now"));

        logger.info("  Converting a map with embedded JODA date to JSON...");
        String jsonRepresentation = mapper.writeValueAsString(map);
        logger.info("  Confirming that {} contains {}", jsonRepresentation, nowString);
        assertThat(jsonRepresentation, containsString("\"now\" : \"" + nowString + "\""));

        logger.info("  Converting the JSON back to the map...");
        HashMap<String, DateTime> copy = mapper.readValue(jsonRepresentation, new TypeReference<HashMap<String, DateTime>>() { });
        assertEquals(map, copy);

        logger.info("Embedded Java Date manipulation testing completed.\n");
    }

    @Test
    public void testNullsSuppressedInMaps() throws IOException {
        logger.info("Testing suppression of nulls in maps...");

        HashMap<String, HashMap<String, String>> map = new HashMap<>();
        HashMap<String, String> innermap = new HashMap<>();
        innermap.put("nonnull", "nonnull");
        innermap.put("null", null);
        map.put("inner", innermap);

        String jsonRepresentation = mapper.writeValueAsString(map);
        assertEquals("{\"inner\":{\"nonnull\":\"nonnull\"}}", jsonRepresentation.replaceAll("\\s", ""));

        logger.info("Suppression of nulls in maps testing completed.\n");
    }

    @Test
    public void testNullPropertiesSuppressed() throws IOException {
        logger.info("Testing suppression of null properties...");

        class TestProperties {
            public String nonnull = "nonnull";
            public String _null = null;
        }

        String jsonRepresentation = mapper.writeValueAsString(new TestProperties());
        assertEquals("{\"nonnull\":\"nonnull\"}", jsonRepresentation.replaceAll("\\s", ""));

        logger.info("Suppression of null properties testing completed.\n");
    }

    @Test
    public void testEmptyObject() throws IOException {
        logger.info("Testing empty objects...");

        class TestObject extends SmartObject<TestObject> {
        }

        String jsonRepresentation = mapper.writeValueAsString(new TestObject());
        assertEquals("{ }", jsonRepresentation);

        logger.info("Empty objects testing completed.\n");
    }

    static class PolymorphicTestObject extends SmartObject<PolymorphicTestObject> {
        public String testAttribute = "test";
    }

    static <E extends SmartObject<E>> E polymorphicReader(SmartObjectMapper mapper) throws IOException {
        return mapper.reader(SmartObject.class).readValue("{\"testAttribute\":\"success\"}");
    }

    @Test
    public void testPolymorphicObject() throws IOException {
        logger.info("Testing polymorphic objects...");

        SimpleModule module = new SimpleModule();
        module.addAbstractTypeMapping(SmartObject.class, PolymorphicTestObject.class);
        mapper.registerModule(module);
        PolymorphicTestObject polyObject = polymorphicReader(mapper);
        assertEquals("success", polyObject.testAttribute);

        logger.info("Polymorphic objects testing completed.\n");
    }

}
