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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;


/**
 * This class implements a simple example smart map.
 *
 * @author Derk Norton
 */
public class ExampleSmartMap extends SmartObject<ExampleSmartMap> {

    private final String dummy = "SHOULD NOT GET SERIALIZED";
    private final Map<String, Integer> map = new HashMap<>();

    public ExampleSmartMap() {
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
    }

    @JsonCreator
    public ExampleSmartMap(Map<String, Integer> items) {
        map.putAll(items);
    }

    @JsonValue
    public Map<String, Integer> toMap() {
        return map;
    }

}
