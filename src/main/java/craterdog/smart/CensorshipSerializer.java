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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import java.io.IOException;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * This class handles the masking of sensitive attributes during string serialization. Any string
 * attributes that are annotated with the <code>@Sensitive(mask="some pattern")</code> annotation
 * will be masked accordingly.
 *
 * @author Yan Ma
 */
public class CensorshipSerializer extends JsonSerializer<String> implements ContextualSerializer {

    static private final XLogger logger = XLoggerFactory.getXLogger(CensorshipSerializer.class);

    private final String mask;
    private final char maskingCharacter;

    /**
     * The default constructor creates a serializer with no mask. None of the characters of the
     * string will be masked.
     */
    public CensorshipSerializer() {
        this.mask = null;
        this.maskingCharacter = 'X';
    }

    /**
     * This constructor creates a serializer with the specified mask and masking character. The
     * characters of a string serialized using this serializer will be censored using the specified
     * mask.
     *
     * @param mask The pattern to be used to mask the sensitive attribute.
     * @param maskingCharacter The character to be used for masking.
     */
    public CensorshipSerializer(String mask, char maskingCharacter) {
        this.mask = mask;
        this.maskingCharacter = maskingCharacter;
    }


    /*
     This is a strange instance method that really probably should be a static method but isn't
     due to the ContextualSerializer framework structure.  The important thing to realize is that
     a default CensorshipSerializer will be created by the framework with the default constructor
     and then that instance will be used to create the *real* one with this method below.
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
            throws JsonMappingException {
        Sensitive sensitive = null;
        if (property != null) {
            logger.debug("Checking property for @Sensitive annotation.");
            sensitive = property.getAnnotation(Sensitive.class);
            if (sensitive == null) {
                logger.debug("Checking property for @Sensitive context annotation.");
                sensitive = property.getContextAnnotation(Sensitive.class);
            }
        }
        if (sensitive != null) {
            String sensitiveMask = sensitive.mask();
            char sensitiveCharacter = sensitive.character();
            logger.debug("Found an annotation with mask {} and masking character {}.",
                    sensitiveMask, sensitiveCharacter);
            return new CensorshipSerializer(sensitiveMask, sensitiveCharacter);
        }
        // the default serializer has no mask so use it
        return this;
    }

    @Override
    public void serialize(String value, JsonGenerator generator, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        if (mask != null) {
            Censor censor = new Censor(maskingCharacter);
            value = censor.process(value, mask);
        }
        generator.writeString(value);
    }

}
