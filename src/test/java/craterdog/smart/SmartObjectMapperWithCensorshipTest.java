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
import java.util.List;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.constraint.MatchPattern;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

    private final XLogger logger = XLoggerFactory.getXLogger(this.getClass());
    private final SmartObjectMapper mapper = new SmartObjectMapper(new CensorshipModule());

    @Test
    public void testMaskCreditCardNumber() throws JsonProcessingException {
        CreditCardNumber data = new CreditCardNumber();
        data.creditCardNumber = "1234-5678-9012-3456";
        String result = mapper.writeValueAsString(data);
        assertTrue(result.contains("1234-XXXX-XXXX-3456"));
    }

    @Test
    public void testMaskSSN() throws JsonProcessingException {
        SSN data = new SSN();
        data.ssn = "123-45-6789";
        String result = mapper.writeValueAsString(data);
        assertTrue(result.contains("XXXXXXX6789"));
    }

    @Test
    public void testMaskEmail() throws JsonProcessingException {
        Email data = new Email();
        data.email = "hello@mail.com";
        String result = mapper.writeValueAsString(data);
        assertTrue(result.contains("XXXX@mail.com"));
    }

    @Test
    public void testMaskCreditNumber2() throws JsonProcessingException {
        CreditCardNumber2 data = new CreditCardNumber2();
        data.creditCardNumber = "1234-5678-9012-3456";
        String result = mapper.writeValueAsString(data);
        assertTrue(result.contains("XXXXXXXXXXXXXXX3456"));
    }

    @Test
    public void testInvalidEmail() {
        Email data = new Email();
        data.email = "hello@";

        Validator validator = new Validator();
        List<ConstraintViolation> violations = validator.validate(data);
        logger.info("size of violations: " + violations.size());
        assertTrue(!violations.isEmpty());
    }

    @Test
    public void testNullProperty() throws JsonProcessingException {
        SSN ssn = null;
        String result = mapper.writeValueAsString(ssn);
        logger.debug(result);
        assertEquals(result, "null");
    }

    @Test
    public void testInvalidMaskPattern() throws JsonProcessingException {
        SSNInvalidMask ssn = new SSNInvalidMask("123-45-6789");
        String result = mapper.writeValueAsString(ssn);
        logger.debug(result);
        assertTrue(result.contains("MASKING_ERROR"));
    }

    @Test
    public void testAlphanumericPinPositiveCases() throws JsonProcessingException {
        //16 numbers
        AlphanumericPin pin = new AlphanumericPin("1234567890123456");
        String result = mapper.writeValueAsString(pin);
        logger.debug(result);
        assertTrue(result.contains("\"XXXXXXXXXXXXXXXX\""));

        pin.alphanumericPin = "12345678901234567890";
        result = mapper.writeValueAsString(pin);
        logger.debug(result);
        assertTrue(result.contains("\"XXXXXXXXXXXXXXXXXXXX\""));

        pin.alphanumericPin = "1234";
        result = mapper.writeValueAsString(pin);
        logger.debug(result);
        assertTrue(result.contains("\"XXXX\""));

        pin.alphanumericPin = "abcdefghijKLmnop";
        result = mapper.writeValueAsString(pin);
        logger.debug(result);
        assertTrue(result.contains("\"XXXXXXXXXXXXXXXX\""));

        pin.alphanumericPin = "abcd";
        result = mapper.writeValueAsString(pin);
        logger.debug(result);
        assertTrue(result.contains("\"XXXX\""));

        pin.alphanumericPin = "12345ABCDE123hij";
        result = mapper.writeValueAsString(pin);
        logger.debug(result);
        assertTrue(result.contains("\"XXXXXXXXXXXXXXXX\""));

        pin.alphanumericPin = "ABCdefghij123456";
        result = mapper.writeValueAsString(pin);
        logger.debug(result);
        assertTrue(result.contains("\"XXXXXXXXXXXXXXXX\""));

    }

    @Test
    public void testAlphanumericPinEmptyString() throws JsonProcessingException {
        AlphanumericPin pin = new AlphanumericPin("");
        String result = mapper.writeValueAsString(pin);
        logger.debug(result);
        assertTrue(result.contains("\"\""));
    }

    @Test
    public void testAlphanumericPinUnderMinLength() throws JsonProcessingException {
        AlphanumericPin pin = new AlphanumericPin("012");
        String result = mapper.writeValueAsString(pin);
        logger.debug(result);
        assertTrue(result.contains("MASKING_ERROR"));
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

        public SSNInvalidMask(String string) {
            ssn = string;
        }

        @Sensitive(type = "social security", mask = "(^\\d{3}-\\d{2}-\\d{4}$")
        public String ssn;
    }

    private class AlphanumericPin {

        @MatchPattern(pattern = Sensitive.MASK_PASSWORD)
        @Sensitive(type = "password", mask = Sensitive.MASK_PASSWORD)
        public String alphanumericPin;

        public AlphanumericPin(String str) {
            this.alphanumericPin = str;
        }
    }

}
