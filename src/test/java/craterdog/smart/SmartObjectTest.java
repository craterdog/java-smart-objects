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

import craterdog.primitives.Angle;
import craterdog.primitives.BinaryString;
import craterdog.primitives.Probability;
import craterdog.primitives.Tag;
import craterdog.primitives.TextString;
import java.net.URI;
import java.net.URISyntaxException;
import org.joda.time.DateTime;
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

    static private final byte[] bytes = { 81 };

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


    static private final String expectedJSON
            = "{\n"
            + "  \"foo\" : \"Foo\",\n"
            + "  \"card\" : \"1234-XXXX-XXXX-3456\",\n"
            + "  \"bar\" : 5,\n"
            + "  \"timestamp\" : \"2014-04-11T04:01:14.139Z\",\n"
            + "  \"angle\" : 2.5,\n"
            + "  \"binary\" : \"UQ==\",\n"
            + "  \"probability\" : 0.5,\n"
            + "  \"tag\" : \"RHX5NN621RS8JWZCHH4W23YHZL0TDCFL\",\n"
            + "  \"text\" : \"This is a text string.\",\n"
            + "  \"uri\" : \"https://foo.com/bar?baz=0\",\n"
            + "  \"list\" : [\n"
            + "    \"alpha\",\n"
            + "    \"bravo\",\n"
            + "    \"charlie\"\n"
            + "  ],\n"
            + "  \"map\" : {\n"
            + "    \"alpha\" : 1,\n"
            + "    \"bravo\" : 2,\n"
            + "    \"charlie\" : 3\n"
            + "  }\n"
            + "}";

    static private final String indentedJSON
            = "{\n"
            + "      \"foo\" : \"Foo\",\n"
            + "      \"card\" : \"1234-XXXX-XXXX-3456\",\n"
            + "      \"bar\" : 5,\n"
            + "      \"timestamp\" : \"2014-04-11T04:01:14.139Z\",\n"
            + "      \"angle\" : 2.5,\n"
            + "      \"binary\" : \"UQ==\",\n"
            + "      \"probability\" : 0.5,\n"
            + "      \"tag\" : \"RHX5NN621RS8JWZCHH4W23YHZL0TDCFL\",\n"
            + "      \"text\" : \"This is a text string.\",\n"
            + "      \"uri\" : \"https://foo.com/bar?baz=0\",\n"
            + "      \"list\" : [\n"
            + "        \"alpha\",\n"
            + "        \"bravo\",\n"
            + "        \"charlie\"\n"
            + "      ],\n"
            + "      \"map\" : {\n"
            + "        \"alpha\" : 1,\n"
            + "        \"bravo\" : 2,\n"
            + "        \"charlie\" : 3\n"
            + "      }\n"
            + "    }";

    /**
     * This unit test method tests the toString() method.
     * @throws java.net.URISyntaxException
     */
    @Test
    public void testToString() throws URISyntaxException {
        logger.info("Testing the toString() method for a SmartObject...");

        ExampleSmartObject object = new ExampleSmartObject();
        object.foo = "Foo";
        object.bar = 5;
        object.timestamp = new DateTime("2014-04-11T04:01:14.139Z");
        object.angle = new Angle(2.5);
        object.binary = new BinaryString(bytes);
        object.probability = new Probability(0.5);
        object.tag = new Tag("RHX5NN621RS8JWZCHH4W23YHZL0TDCFL");
        object.text = new TextString("This is a text string.");
        object.uri = new URI("https://foo.com/bar?baz=0");
        object.card = "1234-5678-9012-3456";
        String actualJSON = object.toString();
        assertEquals(expectedJSON, actualJSON);
        logger.info("  The JSON string: " + actualJSON);

        logger.info("The toString() method testing completed.");
    }

    /**
     * This unit test method tests the toString(String indentation) method.
     * @throws java.net.URISyntaxException
     */
    @Test
    public void testToStringWithIndent() throws URISyntaxException {
        logger.info("Testing the toString(indentation) method for a SmartObject...");

        ExampleSmartObject object = new ExampleSmartObject();
        object.foo = "Foo";
        object.bar = 5;
        object.timestamp = new DateTime("2014-04-11T04:01:14.139Z");
        object.angle = new Angle(2.5);
        object.binary = new BinaryString(bytes);
        object.probability = new Probability(0.5);
        object.tag = new Tag("RHX5NN621RS8JWZCHH4W23YHZL0TDCFL");
        object.text = new TextString("This is a text string.");
        object.uri = new URI("https://foo.com/bar?baz=0");
        object.card = "1234-5678-9012-3456";
        String actualJSON = object.toString("    ");
        assertEquals(indentedJSON, actualJSON);
        logger.info("  The indented JSON string: " + actualJSON);

        logger.info("The toString(indentation) method testing completed.");
    }

    /**
     * This unit test method tests the copy() and equals(Object object) methods.
     * @throws java.net.URISyntaxException
     */
    @Test
    public void testCopyAndEquals() throws URISyntaxException {
        logger.info("Testing the copy() and equals() methods for a SmartObject...");

        ExampleSmartSubclass object = new ExampleSmartSubclass();
        object.foo = "Foo";
        object.bar = 5;
        object.baz = "Baz";
        object.timestamp = new DateTime();
        object.angle = new Angle(2.5);
        object.binary = new BinaryString(bytes);
        object.probability = new Probability(0.5);
        object.tag = new Tag("RHX5NN621RS8JWZCHH4W23YHZL0TDCFL");
        object.text = new TextString("This is a text string.");
        object.uri = new URI("https://foo.com/bar?baz=0");
        object.card = "1234-5678-9012-3456";
        ExampleSmartSubclass copy = object.copy();
        if (!object.equals(copy)) {
            fail("  The ihe copied object is not equal to the original.");
        }

        logger.info("The copy() mnd equals() methods testing completed.");
    }

    /**
     * This unit test method tests the compareTo(Object object) method.
     * @throws java.net.URISyntaxException
     */
    @Test
    public void testCompareTo() throws URISyntaxException {
        logger.info("Testing the compareTo() method for a SmartObject...");

        DateTime now = new DateTime();
        ExampleSmartSubclass object1 = new ExampleSmartSubclass();
        object1.foo = "Foo";
        object1.bar = 5;
        object1.baz = "Baz";
        object1.timestamp = now;
        object1.angle = new Angle(2.5);
        object1.binary = new BinaryString(bytes);
        object1.probability = new Probability(0.5);
        object1.tag = new Tag("RHX5NN621RS8JWZCHH4W23YHZL0TDCFL");
        object1.text = new TextString("This is a text string.");
        object1.uri = new URI("https://foo.com/bar?baz=0");
        object1.card = "1234-5678-9012-3456";
        ExampleSmartSubclass object2 = new ExampleSmartSubclass();
        object2.foo = "Foo";
        object2.bar = 4;
        object2.baz = "Baz";
        object2.timestamp = now;
        object2.angle = new Angle(2.5);
        object2.binary = new BinaryString(bytes);
        object2.probability = new Probability(0.5);
        object2.tag = new Tag("RHX5NN621RS8JWZCHH4W23YHZL0TDCFL");
        object2.text = new TextString("This is a text string.");
        object2.uri = new URI("https://foo.com/bar?baz=0");
        object2.card = "1234-5678-9012-3456";
        if (object1.compareTo(object2) < 1) {
            fail("  The The compareTo() method failed to compare smart objects correctly.");
        }

        logger.info("The compareTo() method testing completed.");
    }

}
