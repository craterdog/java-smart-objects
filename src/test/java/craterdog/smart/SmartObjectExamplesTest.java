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
import java.util.HashMap;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * This class tests the canonical methods implemented in the <code>SmartObject</code> class.
 *
 * @author Jeff Webb
 * @author Derk Norton
 */
public class SmartObjectExamplesTest {

    static private final XLogger logger = XLoggerFactory.getXLogger(SmartObjectExamplesTest.class);

    public class Customer extends SmartObject<Customer> {
        public String name;
        @Sensitive(type = "credit card", mask = Sensitive.MASK_CREDIT_CARD_NUMBER)
        public String cardNumber;
        @Sensitive(type = "email address", mask = Sensitive.MASK_EMAIL_ADDRESS)
        public String emailAddress;
        public DateTime lastPurchase;
    }

    @Test
    public void testCodeExamples() throws JsonProcessingException {
        logger.info("Testing the code examples for SmartObject...");

        Censor censor = new Censor('*');  // use a star to mask the characters
        String ssNumber = "123-45-6789";
        String mask = "^\\d{1}(\\d{2})-(\\d{2})-(\\d{2})\\d{2}$";
        String maskedNumber = censor.process(ssNumber, mask);
        logger.info("The masked SS number is {}", maskedNumber);

        Customer customer = new Customer();
        customer.name = "Derk Norton";
        customer.cardNumber = "1234-5678-9012-3456";
        customer.emailAddress = "craterdog@gmail.com";
        customer.lastPurchase = new DateTime();
        logger.info("The customer information is {}", customer);

        SmartObjectMapper mapper = new SmartObjectMapper();
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Sheldon Cooper");
        String[] values = { "rock", "paper", "scissors", "lizard", "spock" };
        map.put("values", values);
        String json = mapper.writeValueAsString(map);
        logger.info("The JSON string is {}", json);

        mapper = new SmartObjectMapper(new CensorshipModule());
        json = mapper.writeValueAsString(customer);
        logger.info("The JSON string is {}", json);

        logger.info("The code examples testing completed.");
    }

}
