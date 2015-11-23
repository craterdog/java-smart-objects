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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * This class tests the canonical methods implemented in the <code>SmartObject</code> class.
 *
 * @author Jeff Webb
 * @author Derk Norton
 */
public class SmartObjectTest {

    static private final XLogger logger = XLoggerFactory.getXLogger(SmartObjectTest.class);

    static private final String expectedJson = "{\n" +
        "  \"bar\" : 0,\n" +
        "  \"pi\" : 3.141592653589793,\n" +
        "  \"timestamp\" : \"2015-08-28T19:59:55.585Z\",\n" +
        "  \"angle\" : 0.0,\n" +
        "  \"binary\" : \"0123456789ABCDEF\",\n" +
        "  \"probability\" : 0.5,\n" +
        "  \"tag\" : \"L97CRGYM17CRGFV2C43FKRJWRYK09WHH\",\n" +
        "  \"text\" : \"This is a test string.\",\n" +
        "  \"uri\" : \"http://google.com\",\n" +
        "  \"foo\" : \"This is another test string.\",\n" +
        "  \"card\" : \"1234-XXXX-XXXX-3456\",\n" +
        "  \"list\" : [\n" +
        "    \"alpha\",\n" +
        "    \"bravo\",\n" +
        "    \"charlie\"\n" +
        "  ],\n" +
        "  \"map\" : {\n" +
        "    \"alpha\" : 1,\n" +
        "    \"bravo\" : 2,\n" +
        "    \"charlie\" : 3\n" +
        "  }\n" +
        "}";

    static private final String indentedJson = "{\n" +
        "      \"bar\" : 0,\n" +
        "      \"pi\" : 3.141592653589793,\n" +
        "      \"timestamp\" : \"2015-08-28T19:59:55.585Z\",\n" +
        "      \"angle\" : 0.0,\n" +
        "      \"binary\" : \"0123456789ABCDEF\",\n" +
        "      \"probability\" : 0.5,\n" +
        "      \"tag\" : \"L97CRGYM17CRGFV2C43FKRJWRYK09WHH\",\n" +
        "      \"text\" : \"This is a test string.\",\n" +
        "      \"uri\" : \"http://google.com\",\n" +
        "      \"foo\" : \"This is another test string.\",\n" +
        "      \"card\" : \"1234-XXXX-XXXX-3456\",\n" +
        "      \"list\" : [\n" +
        "        \"alpha\",\n" +
        "        \"bravo\",\n" +
        "        \"charlie\"\n" +
        "      ],\n" +
        "      \"map\" : {\n" +
        "        \"alpha\" : 1,\n" +
        "        \"bravo\" : 2,\n" +
        "        \"charlie\" : 3\n" +
        "      }\n" +
        "    }";


    /**
     * Log a message at the beginning of the tests.
     */
    @BeforeClass
    static public void setUpClass() {
        logger.info("Running SmartObject Unit Tests...\n");
    }


    /**
     * Log a message at the end of the tests.
     */
    @AfterClass
    static public void tearDownClass() {
        logger.info("Completed SmartObject Unit Tests.\n");
    }


    /**
     * This unit test method tests the toString() method.
     * @throws java.io.IOException
     */
    @Test
    public void testFromString() throws IOException {
        logger.info("Testing the fromString() method for a SmartObject...");

        ExampleSmartObject object = new ExampleSmartObject();
        String actualJson = object.toExposedString();
        ExampleSmartObject copy = SmartObject.fromString(ExampleSmartObject.class, actualJson);
        assertEquals(object, copy);

        logger.info("The fromString() method testing completed.");
    }

    /**
     * This unit test method tests the toString() method.
     * @throws java.io.IOException
     */
    @Test
    public void testFromStringWithParameterizedType() throws IOException {
        logger.info("Testing the fromStringWithParameterizedType() method for a SmartObject...");

        List<String> list = new ArrayList<>();
        list.add("foo");
        list.add("bar");
        String actualJson = SmartObject.toString(list);
        List<String> copy = SmartObject.fromString(new TypeReference<List<String>>() {}, actualJson);
        assertEquals(list, copy);

        logger.info("The fromStringWithParameterizedType() method testing completed.");
    }

    /**
     * This unit test method tests the toString() method.
     */
    @Test
    public void testToString() {
        logger.info("Testing the toString() method for a SmartObject...");

        ExampleSmartObject object = new ExampleSmartObject();
        String actualJson = object.toString();
        logger.info("  The JSON string: {}", actualJson);
        assertEquals(expectedJson, actualJson);

        logger.info("The toString() method testing completed.");
    }

    /**
     * This unit test method tests the toString(String indentation) method.
     */
    @Test
    public void testToStringWithIndent() {
        logger.info("Testing the toString(indentation) method for a SmartObject...");

        ExampleSmartObject object = new ExampleSmartObject();
        String actualJSON = object.toString("    ");
        logger.info("  The indented JSON string: {}", actualJSON);
        assertEquals(indentedJson, actualJSON);

        logger.info("The toString(indentation) method testing completed.");
    }

    /**
     * This unit test method tests the copy() and equals(Object object) methods.
     */
    @Test
    public void testCopyAndEquals() {
        logger.info("Testing the copy() and equals() methods for a SmartObject...");

        ExampleSmartSubclass object = new ExampleSmartSubclass();
        ExampleSmartSubclass copy = object.copy();
        if (!object.equals(copy)) {
            fail("  The the copied object is not equal to the original.");
        }

        logger.info("The copy() and equals() methods testing completed.");
    }

    /**
     * This unit test method tests the compareTo(Object object) method.
     */
    @Test
    public void testCompareTo() {
        logger.info("Testing the compareTo() method for a SmartObject...");

        ExampleSmartSubclass object1 = new ExampleSmartSubclass();
        ExampleSmartSubclass object2 = new ExampleSmartSubclass();
        object2.bar = 4;
        logger.info("  The modified JSON string: {}", object2.toExposedString());
        if (object1.compareTo(object2) > -1) {
            fail("  The The compareTo() method failed to compare smart objects correctly.");
        }

        logger.info("The compareTo() method testing completed.");
    }

}
