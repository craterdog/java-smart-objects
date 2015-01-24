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
import net.sf.oval.constraint.MatchPattern;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * This class defines unit tests that exercise a SmartObjectMapper that is configured with a
 * CensorshipModule. It verifies that the masking of sensitive data is handled correctly.
 *
 * @author Yan Ma
 */
public class SmartObjectMapperWithCensorshipTest {

    static private final XLogger logger = XLoggerFactory.getXLogger(SmartObjectMapperWithCensorshipTest.class);
    static private final SmartObjectMapper mapper = new SmartObjectMapper(new CensorshipModule());


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
    public void testMaskSSN() throws JsonProcessingException {
        logger.info("Testing masked social security number...");

        SSN data = new SSN();
        data.ssn = "123-45-6789";
        String result = mapper.writeValueAsString(data);
        assertTrue(result.contains("XXXXXXX6789"));
        logger.info("  Masked value: {}", result);

        logger.info("Masked social security number testing completed.\n");
    }


    @Test
    public void testMaskEmail() throws JsonProcessingException {
        logger.info("Testing masked email address...");

        Email data = new Email();
        data.email = "hello@mail.com";
        String result = mapper.writeValueAsString(data);
        assertTrue(result.contains("XXXX@mail.com"));
        logger.info("  Masked value: {}", result);

        logger.info("Masked email address testing completed.\n");
    }


    @Test
    public void testMaskInvalidEmail() throws JsonProcessingException {
        logger.info("Testing masked invalid email address...");

        Email data = new Email();
        data.email = "hello@";
        String result = mapper.writeValueAsString(data);
        assertTrue(result.contains("MASKING_ERROR"));
        logger.info("  Masked value: {}", result);

        logger.info("Masked invalid email address testing completed.\n");
    }


    @Test
    public void testMaskCreditCardNumber() throws JsonProcessingException {
        logger.info("Testing masked credit card number...");

        CreditCardNumber data = new CreditCardNumber();
        data.creditCardNumber = "1234-5678-9012-3456";
        String result = mapper.writeValueAsString(data);
        assertTrue(result.contains("1234-XXXX-XXXX-3456"));
        logger.info("  Masked value: {}", result);

        logger.info("Masked credit card number testing completed.\n");
    }


    @Test
    public void testMaskCreditCardNumber2() throws JsonProcessingException {
        logger.info("Testing another masked credit card number...");

        CreditCardNumber2 data = new CreditCardNumber2();
        data.creditCardNumber = "1234-5678-9012-3456";
        String result = mapper.writeValueAsString(data);
        assertTrue(result.contains("XXXXXXXXXXXXXXX3456"));
        logger.info("  Masked value: {}", result);

        logger.info("Another masked credit card number testing completed.\n");
    }


    @Test
    public void testNullProperty() throws JsonProcessingException {
        logger.info("Testing masked null social security number...");

        SSN ssn = null;
        String result = mapper.writeValueAsString(ssn);
        assertEquals(result, "null");
        logger.info("  Masked value: {}", result);

        logger.info("Masked null social security number testing completed.\n");
    }


    @Test
    public void testInvalidMaskPattern() throws JsonProcessingException {
        logger.info("Testing invalid mask pattern...");

        SSNInvalidMask ssn = new SSNInvalidMask("123-45-6789");
        String result = mapper.writeValueAsString(ssn);
        assertTrue(result.contains("MASKING_ERROR"));
        logger.info("  Masked value: {}", result);

        logger.info("Invalid mask pattern testing completed.\n");
    }


    @Test
    public void testAlphanumericPins() throws JsonProcessingException {
        logger.info("Testing masked alphanumeric PINs...");

        AlphanumericPin pin = new AlphanumericPin("1234567890123456");
        String result = mapper.writeValueAsString(pin);
        assertTrue(result.contains("\"XXXXXXXXXXXXXXXX\""));
        logger.info("  Masked value: {}", result);

        pin.alphanumericPin = "12345678901234567890";
        result = mapper.writeValueAsString(pin);
        assertTrue(result.contains("\"XXXXXXXXXXXXXXXXXXXX\""));
        logger.info("  Masked value: {}", result);

        pin.alphanumericPin = "1234";
        result = mapper.writeValueAsString(pin);
        assertTrue(result.contains("\"XXXX\""));
        logger.info("  Masked value: {}", result);

        pin.alphanumericPin = "abcdefghijKLmnop";
        result = mapper.writeValueAsString(pin);
        assertTrue(result.contains("\"XXXXXXXXXXXXXXXX\""));
        logger.info("  Masked value: {}", result);

        pin.alphanumericPin = "abcd";
        result = mapper.writeValueAsString(pin);
        assertTrue(result.contains("\"XXXX\""));
        logger.info("  Masked value: {}", result);

        pin.alphanumericPin = "12345ABCDE123hij";
        result = mapper.writeValueAsString(pin);
        assertTrue(result.contains("\"XXXXXXXXXXXXXXXX\""));
        logger.info("  Masked value: {}", result);

        pin.alphanumericPin = "ABCdefghij123456";
        result = mapper.writeValueAsString(pin);
        assertTrue(result.contains("\"XXXXXXXXXXXXXXXX\""));
        logger.info("  Masked value: {}", result);

        logger.info("Masked alphanumeric PINs testing completed.\n");
    }


    @Test
    public void testEmptyAlphanumericPin() throws JsonProcessingException {
        logger.info("Testing masked empty alphanumeric PIN...");

        AlphanumericPin pin = new AlphanumericPin("");
        String result = mapper.writeValueAsString(pin);
        assertTrue(result.contains("\"\""));
        logger.info("  Masked value: {}", result);

        logger.info("Masked empty alphanumeric PIN testing completed.\n");
    }


    @Test
    public void testAlphanumericPinUnderMinimumLength() throws JsonProcessingException {
        logger.info("Testing masked alphanumeric PIN that is too small...");

        AlphanumericPin pin = new AlphanumericPin("012");
        String result = mapper.writeValueAsString(pin);
        assertTrue(result.contains("MASKING_ERROR"));
        logger.info("  Masked value: {}", result);

        logger.info("Masked alphanumeric PIN that is too small testing completed.\n");
    }


    private class CreditCardNumber2 {

        @MatchPattern(pattern = "^((\\d{4}-){3})\\d{4}$")
        @Sensitive(type = "credit card", mask = "^((\\d{4}-){3})\\d{4}$")
        public String creditCardNumber;
    }


    private class CreditCardNumber {

        @MatchPattern(pattern = "^((\\d{4}-){3})\\d{4}$")
        @Sensitive(type = "credit card", mask = Sensitive.MASK_CREDIT_CARD_NUMBER)
        public String creditCardNumber;
    }


    private class SSN {

        @MatchPattern(pattern = Sensitive.MASK_SSN)
        @Sensitive(type = "social security", mask = "(^\\d{3}-\\d{2}-)\\d{4}$")
        public String ssn;
    }


    private class Email {

        @MatchPattern(pattern = "['_A-Za-z0-9-+&]+(\\.['_A-Za-z0-9-+&]+)*[.]{0,1}@([A-Za-z0-9-_])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))")
        @Sensitive(type = "email", mask = Sensitive.MASK_EMAIL_ADDRESS)
        public String email;
    }


    private class SSNInvalidMask {

        SSNInvalidMask(String string) {
            ssn = string;
        }

        @Sensitive(type = "social security", mask = "(^\\d{3}-\\d{2}-\\d{4}$")
        public String ssn;
    }


    private class AlphanumericPin {

        @MatchPattern(pattern = Sensitive.MASK_PASSWORD)
        @Sensitive(type = "password", mask = Sensitive.MASK_PASSWORD)
        public String alphanumericPin;

        AlphanumericPin(String str) {
            this.alphanumericPin = str;
        }
    }

}
