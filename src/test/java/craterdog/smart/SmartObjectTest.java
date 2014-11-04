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

import org.joda.time.DateTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
    static private final String expectedJSON
            = "{\n"
            + "  \"foo\" : \"Foo\",\n"
            + "  \"card\" : \"1234-XXXX-XXXX-3456\",\n"
            + "  \"bar\" : 5,\n"
            + "  \"timestamp\" : \"2014-04-11T04:01:14.139Z\"\n"
            + "}";

    @Test
    public void testToString() {
        logger.info("Testing the toString() method for a SmartObject...");
        ExampleSmartObject object = new ExampleSmartObject();
        object.foo = "Foo";
        object.bar = 5;
        object.timestamp = new DateTime("2014-04-11T04:01:14.139Z");
        object.card = "1234-5678-9012-3456";
        String actualJSON = object.toString();
        assertEquals(expectedJSON, actualJSON);
        logger.info("JSON string: " + actualJSON);
        logger.info("The toString() method testing completed.");
    }

    @Test
    public void testCopyAndEquals() throws Exception {
        logger.info("Testing the copy() and equals() methods for a SmartObject...");
        ExampleSmartObject object = new ExampleSmartObject();
        object.foo = "Foo";
        object.bar = 5;
        object.timestamp = new DateTime();
        object.card = "1234-5678-9012-3456";
        ExampleSmartObject copy = object.copy();
        if (!object.equals(copy)) {
            fail("The copied object is not equal to the original.");
        }
        logger.info("The copy() mnd equals() methods testing completed.");
    }

}
