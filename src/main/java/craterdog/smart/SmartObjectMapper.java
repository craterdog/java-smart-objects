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

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

/**
 * This object mapper handles the conversion of a smart object to and from a JSON string. The mapper
 * can be configured to censor sensitive attributes that are marked with the <code>@Sensitive</code>
 * annotation.
 *
 * @author Jeff Webb
 * @author Mukesh Jyothi
 * @author Derk Norton
 */
@SuppressWarnings("serial")
public class SmartObjectMapper extends ObjectMapper {

    /**
     * This constructor creates a new smart object mapper for the specified modules.
     *
     * @param modules The modules to be registered.
     */
    public SmartObjectMapper(Module... modules) {
        super();

        // indent the output
        enable(SerializationFeature.INDENT_OUTPUT);

        // allow empty beans
        disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        // don't serialize nulls
        disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        setSerializationInclusion(Include.NON_NULL);

        // don't fail on unknown properties
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // write dates out as string (not milliseconds since epoch <- timestamp)
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // handle joda types
        registerModule(new JodaModule());

        // add any additional modules passed in
        for (Module module : modules) {
            registerModule(module);
        }

    }

}