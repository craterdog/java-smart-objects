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

/**
 * This class implements a simple example smart object.
 *
 * @author Derk Norton
 */
public class ExampleSmartObject extends SmartObject<ExampleSmartObject> {

    /**
     * An example attribute.
     */
    public String foo;

    /**
     * An example sensitive attribute.
     */
    @Sensitive(type = "credit card", mask = Sensitive.MASK_CREDIT_CARD_NUMBER)
    public String card;

    /**
     * Another example attribute.
     */
    public int bar;

    /**
     * A slightly more complex attribute.
     */
    public DateTime timestamp;

    /**
     * A nested list attribute.
     */
    public ExampleSmartList list = new ExampleSmartList();

    /**
     * A nested map attribute.
     */
    public ExampleSmartMap map = new ExampleSmartMap();

}
