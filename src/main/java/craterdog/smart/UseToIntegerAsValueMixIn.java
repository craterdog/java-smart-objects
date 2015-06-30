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

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * This mixin interface tells the Jackson parser to use the toInteger() method to generate the value
 * of an attribute.
 *
 * @author Derk Norton
 */
public interface UseToIntegerAsValueMixIn {

    /**
     * This method returns an integer version of the object.
     *
     * @return An integer version of the object.
     */
    @JsonValue
    public double toInteger();

}
