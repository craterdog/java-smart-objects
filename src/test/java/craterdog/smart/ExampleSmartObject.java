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
public class ExampleSmartObject extends SmartObject {

    public String foo;
    @Sensitive(type = "credit card", mask = Sensitive.MASK_CREDIT_CARD_NUMBER)
    public String card;
    public int bar;
    public DateTime timestamp;

}
