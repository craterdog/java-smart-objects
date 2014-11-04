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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark string attributes that contain sensitive information and should
 * be masked when output to a log file, and encrypted when stored in a database.
 *
 * @author Yan Ma
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Sensitive {

    /**
     * The type of sensitive attribute. This may be needed to determine how to encrypt/decrypt the
     * sensitive information.
     *
     * @return The attribute type
     */
    public String type();

    /**
     * The masking pattern that should be applied to the value of the annotated attribute. The
     * pattern is a regular expression that contains "(" and ")" surrounding the parts of the string
     * that should be masked with "X"s.
     *
     * @return The masking pattern.
     */
    public String mask();

    /**
     * The character that should be used to mask the value of the annotated attribute. The default
     * character is 'X'.
     *
     * @return The masking character.
     */
    public char character() default 'X';

    //Defining some constants for frequently used mask annotations
    /**
     * Mask for a password or alphanumeric pin with various lengths (min length = 4, max length =
     * 100): mask all characters
     */
    static public final String MASK_PASSWORD = "(^\\w{4,100}$)";

    /**
     * Mask for a email address. Masks every character before @
     */
    static public final String MASK_EMAIL_ADDRESS = "(^[^@]+)@[^@]+$";

    /**
     * Mask for a phone number: XXX-XXX-dddd leave the last four digits unmasked
     */
    static public final String MASK_PHONE_NUMBER = "(^\\d{3})-(\\d{3})-\\d{4}$";

    /**
     * Mask for a credit card number: dddd-XXXX-XXXX-dddd
     */
    static public final String MASK_CREDIT_CARD_NUMBER = "^\\d{4}-(\\d{4})-(\\d{4})-\\d{4}$";

    /**
     * MASK for a SSN: XXX-XX-dddd leave the last four digits unmasked
     */
    static public final String MASK_SSN = "(^\\d{3}-\\d{2}-)\\d{4}$";

}
