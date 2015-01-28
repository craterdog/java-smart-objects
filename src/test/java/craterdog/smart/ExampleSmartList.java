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
import java.util.ArrayList;
import java.util.List;


/**
 * This class implements a simple example smart list.
 *
 * @author Derk Norton
 */
public class ExampleSmartList extends SmartObject<ExampleSmartList> {

    private final String dummy = "SHOULD NOT GET SERIALIZED";
    private final List<String> list = new ArrayList<>();

    /**
     * This is the default constructor.
     */
    public ExampleSmartList() {
        list.add("alpha");
        list.add("bravo");
        list.add("charlie");
    }

    /**
     * This constructor seeds the list with items.
     *
     * @param items The items to be used to seed the list.
     */
    @JsonCreator
    public ExampleSmartList(List<String> items) {
        list.addAll(items);
    }

    /**
     * This method is used to generate a JSON representation of the list.
     *
     * @return A JSON representation of the list.
     */
    @JsonValue
    public List<String> toList() {
        return list;
    }

}
