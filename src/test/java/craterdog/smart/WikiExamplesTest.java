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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * This class tests the examples posted on the wiki.
 *
 * @author Derk Norton
 */
public class WikiExamplesTest {

    static private final XLogger logger = XLoggerFactory.getXLogger(WikiExamplesTest.class);


    /**
     * Log a message at the beginning of the tests.
     */
    @BeforeClass
    static public void setUpClass() {
        logger.info("Running Wiki Example Unit Tests...\n");
    }


    /**
     * Log a message at the end of the tests.
     */
    @AfterClass
    static public void tearDownClass() {
        logger.info("Completed Wiki Example Unit Tests.\n");
    }


    @Test
    public void testSimpleAttributes() throws JsonProcessingException {
        logger.info("Testing simple attributes...");
        SmartObjectMapper mapper = new SmartObjectMapper();

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("count", 5);
        map.put("pi", Math.PI);
        map.put("name", "Derk Norton");
        map.put("timestamp", new DateTime("2015-08-28T19:59:55.585Z"));
        logger.info("  Simple Attributes: {}", mapper.writeValueAsString(map));

        logger.info("Testing completed.");
    }


    @Test
    public void testList() throws JsonProcessingException {
        logger.info("Testing list...");
        SmartObjectMapper mapper = new SmartObjectMapper();

        List<String> list = new ArrayList<>();
        list.add("alpha");
        list.add("bravo");
        list.add("charlie");
        logger.info("  List: {}", mapper.writeValueAsString(list));

        logger.info("Testing completed.");
    }


    @Test
    public void testMonoList() throws JsonProcessingException {
        logger.info("Testing mono list...");
        SmartObjectMapper mapper = new SmartObjectMapper();

        List<DateTime> list = new ArrayList<>();
        list.add(new DateTime("2015-08-28T19:59:55.585Z"));
        logger.info("  Mono List: {}", mapper.writeValueAsString(list));

        logger.info("Testing completed.");
    }


    @Test
    public void testNestedLists() throws JsonProcessingException {
        logger.info("Testing nested lists...");
        SmartObjectMapper mapper = new SmartObjectMapper();

        List<List<Integer>> list = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            List<Integer> sublist = new ArrayList<>();
            for (int j = 1; j < 4; j++) {
                sublist.add(i * j);
            }
            list.add(sublist);
        }
        logger.info("  Nested Lists: {}", mapper.writeValueAsString(list));

        logger.info("Testing completed.");
    }


    @Test
    public void testNestedObjects() throws JsonProcessingException {
        logger.info("Testing nested objects...");
        SmartObjectMapper mapper = new SmartObjectMapper();

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", "Crater Dog Technologites™");
        map.put("email", "craterdog@gmail.com");
        list.add(map);
        map = new LinkedHashMap<>();
        map.put("name", "Blackhawk Network™");
        map.put("email", "customerservice@bhnetwork.com");
        list.add(map);

        logger.info("  Nested Objects: {}", mapper.writeValueAsString(list));

        logger.info("Testing completed.");
    }


    @Test
    public void testNestedCollections() throws JsonProcessingException {
        logger.info("Testing nested collections...");
        SmartObjectMapper mapper = new SmartObjectMapper();

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", "Crater Dog Technologites™");
        map.put("email", "craterdog@gmail.com");
        List<String> address = new ArrayList<>();
        address.add("597 West Pine Street");
        address.add("Louisville, CO 80027");
        address.add("USA");
        map.put("address", address);
        logger.info("  Nested Collections: {}", mapper.writeValueAsString(map));

        logger.info("Testing completed.");
    }


    @Test
    public void testReals() throws JsonProcessingException {
        logger.info("Testing reals...");
        SmartObjectMapper mapper = new SmartObjectMapper();

        List<Double> list = new ArrayList<>();
        double third = 1.0d / 80000.0d;
        for (int i = 0; i < 14; i++) {
            list.add(third);
            third *= 10.0d;
        }
        logger.info("  Reals: {}", mapper.writeValueAsString(list));

        logger.info("Testing completed.");
    }

}
