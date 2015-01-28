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
import com.fasterxml.jackson.databind.Module;
import craterdog.core.Composite;
import java.io.IOException;

/**
 * This abstract class provides implementations for the standard methods defined in the
 * <code>Object</code> class. It also adds a generics based <code>copy()</code> method that is
 * superior to the <code>clone()</code> method in many ways.
 *
 * @author Derk Norton
 * @author Jeff Webb
 * @author Mukesh Jyothi
 * @author Yan Ma
 *
 * @param <S> The concrete type of the smart object.
 */
public abstract class SmartObject<S extends SmartObject<S>> implements Comparable<S>, Composite {

    // define a safe mapper that censors any sensitive attributes marked with the @Sensitive annotation
    private static final SmartObjectMapper safeMapper = new SmartObjectMapper(new CensorshipModule());

    // define a full mapper that outputs all attributes as stored
    private static final SmartObjectMapper fullMapper = new SmartObjectMapper();


    /**
     * This method returns a string containing a structured, human readable version of the object.
     * The string is formatted in Javascript Object Notation (JSON).
     *
     * @return The formatted JSON string.
     */
    @Override
    public String toString() {
        try {
            return safeMapper.writeValueAsString(this);  // masks any sensitive attributes!
        } catch (JsonProcessingException e) {
            throw new RuntimeException("The attempt to map an object to a string failed", e);
        }
    }


    @Override
    public String toString(String indentation) {
        try {
            return safeMapper.writeValueAsString(this, indentation);  // masks any sensitive attributes!
        } catch (JsonProcessingException e) {
            throw new RuntimeException("The attempt to map an object to a string failed", e);
        }
    }


    /**
     * This method behaves similarly to the <code>toString()</code> method except that it
     * does not perform any censorship of sensitive attributes. It should only be used when
     * the resulting output will not be stored or seen by anyone.
     *
     * @return The formatted JSON string.
     */
    protected String toExposedString() {
        try {
            return fullMapper.writeValueAsString(this);  // exposes any sensitive attributes!
        } catch (JsonProcessingException e) {
            throw new RuntimeException("The attempt to map an object to a string failed", e);
        }
    }


    /**
     * This method determines whether or not two objects are equal. Two objects are equal if they
     * have the same class type and all their attributes and sub-components are equal.
     *
     * @param object The object to be compared with this object.
     * @return Whether or not the two objects are equal.
     */
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!this.getClass().equals(object.getClass())) {
            return false;
        }
        @SuppressWarnings("unchecked")
        S that = (S) object;
        // NOTE: we must use the "exposed" version so that masking doesn't hide any differences!
        return this.toExposedString().equals(that.toExposedString());
    }


    @Override
    public int compareTo(S object) {
        if (object == null) return 1;  // everything is greater than null
        if (this == object) {
            return 0;
        }
        // NOTE: we must use the "exposed" version so that masking doesn't hide any differences!
        return this.toExposedString().compareTo(object.toExposedString());
    }


    /**
     * This method should work for all objects. However, it is very inefficient and should only be
     * used sparingly and for unit testing.
     *
     * @return An exact copy of the smart object.
     */
    public S copy() {
        try {
            String fullJSON = fullMapper.writeValueAsString(this);
            @SuppressWarnings("unchecked")
            S copy = (S) fullMapper.readValue(fullJSON, getClass());
            return copy;
        } catch (IOException e) {
            throw new RuntimeException("The attempted copy of an object failed.", e);
        }
    }


    /**
     * This method returns a hash code for the object based on its string form. This is not a very
     * efficient method.
     *
     * @return A hash code for the object.
     */
    @Override
    public int hashCode() {
        return toExposedString().hashCode();
    }


    /**
     * This protected method allows a subclass to add a class type that can be serialized using its
     * toString() method to the mappers.
     *
     * @param serializable The type of class that can be serialized using its toString() method.
     */
    protected void addSerializableClass(Class<?> serializable) {
        safeMapper.addMixIn(serializable, UseToStringAsValueMixIn.class);
        fullMapper.addMixIn(serializable, UseToStringAsValueMixIn.class);
    }


    /**
     * This protected method allows a subclass to add a Jackson module to the set of modules used by
     * the mappers.
     *
     * @param module The type of class that can be serialized using its toString() method.
     */
    protected void addSerializableClass(Module module) {
        safeMapper.registerModule(module);
        fullMapper.registerModule(module);
    }

}
