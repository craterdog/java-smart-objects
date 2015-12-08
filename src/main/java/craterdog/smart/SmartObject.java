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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    /*
    NOTE: the following mappers are defined as static attributes so that they are shared across
    all smart objects of all types.  The ObjectMapper class is thread-safe when used this way
    but it is important to note that all serializable classes or modules that are added for one
    subclass of SmartObject affect all subclasses of SmartObject.  In general this is still worth
    the performance benefits of the shared ObjectMappers.
    */

    // define a safe mapper that censors any sensitive attributes marked with the @Sensitive annotation
    static private final SmartObjectMapper safeMapper = new SmartObjectMapper(new CensorshipModule());

    // define a full mapper that outputs all attributes as stored
    static private final SmartObjectMapper fullMapper = new SmartObjectMapper();


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
     * @param <T> The concrete type of the smart object.
     * @return An exact copy of the smart object.
     */
    public <T extends SmartObject<S>> T copy() {
        try {
            String fullJSON = fullMapper.writeValueAsString(this);
            @SuppressWarnings("unchecked")
            T copy = (T) fullMapper.readValue(fullJSON, getClass());
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
     * This function takes string containing Javascript Object Notation (JSON) and
     * uses it to construct the corresponding smart object.  This function can be used
     * for any non-parameterized type for example:
     * <pre>
     *     Customer customer = SmartObject.fromString(Customer.class, jsonString);
     * </pre>
     *
     * @param <T> The type of object being constructed.
     * @param classType The concrete class type being constructed.
     * @param json The JSON string.
     * @return The corresponding object.
     * @throws IOException The JSON string could not be parsed correctly.
     */
    static public <T> T fromString(Class<T> classType, String json) throws IOException {
        return safeMapper.readerFor(classType).readValue(json);
    }


    /**
     * This function takes string containing Javascript Object Notation (JSON) and
     * uses it to construct the corresponding smart object.  This function can be used
     * for any parameterized type for example:
     * <pre>
     *     List&lt;String&gt; list = SmartObject.fromString(new TypeReference&lt;List&lt;String&gt;&gt;() { }, jsonString);
     * </pre>
     * The anonymous type declaration is required due to Java's erasure of parameterized
     * types at runtime.
     *
     * @param <T> The type of object being constructed.
     * @param classType The parameterized class type being constructed.
     * @param json The JSON string.
     * @return The corresponding object.
     * @throws IOException The JSON string could not be parsed correctly.
     */
    static public <T> T fromString(TypeReference<T> classType, String json) throws IOException {
        return safeMapper.readerFor(classType).readValue(json);
    }


    /**
     * This function generates a Javascript Object Notation (JSON) string from
     * an object.
     *
     * @param object The object to be turned into a JSON string.
     * @return The corresponding JSON string.
     */
    static public String toString(Object object) {
        try {
            return safeMapper.writeValueAsString(object);  // masks any sensitive attributes!
        } catch (JsonProcessingException e) {
            throw new RuntimeException("The attempt to map an object to a string failed", e);
        }
    }


    /**
     * This function generates an indented Javascript Object Notation (JSON) string from
     * an object.
     *
     * @param object The object to be turned into a JSON string.
     * @param indentation The amount of space that should be prepended to each line.
     * @return The corresponding JSON string.
     */
    static public String toString(Object object, String indentation) {
        try {
            return safeMapper.writeValueAsString(object, indentation);  // masks any sensitive attributes!
        } catch (JsonProcessingException e) {
            throw new RuntimeException("The attempt to map an object to a string failed", e);
        }
    }


    /**
     * This function generates a new object mapper with the specified modules.  For example,
     * to create object mapper that masks sensitive attributes do the following:
     * <pre>
     * ObjectMapper mapper = SmartObject.createMapper(new CensorshipModule());
     * </pre>
     *
     * @param modules The list of modules that should be added to the object mapper.
     * @return A new object mapper containing the specified modules.
     */
    static public ObjectMapper createMapper(Module... modules) {
        return new SmartObjectMapper(modules);
    }


    /**
     * This protected method allows a subclass to add to the mappers a class type that can be
     * serialized using its toString().
     *
     * @param serializable The type of class that can be serialized using its toString() method.
     */
    protected void addSerializableClass(Class<?> serializable) {
        safeMapper.addMixIn(serializable, UseToStringAsValueMixIn.class);
        fullMapper.addMixIn(serializable, UseToStringAsValueMixIn.class);
    }


    /**
     * This protected method allows a subclass to add to the mappers a class type that can be
     * serialized using mixin class.
     *
     * @param serializable The type of class that can be serialized using its toString() method.
     * @param mixin The type of class that can be used to serialized the serializable class.
     */
    protected void addSerializableClass(Class<?> serializable, Class<?> mixin) {
        safeMapper.addMixIn(serializable, mixin);
        fullMapper.addMixIn(serializable, mixin);
    }


    /**
     * This protected method allows a subclass to add to the mappers a Jackson module that can
     * be used to serialize and deserialize instances of the subclass.
     *
     * @param module The type of class that can be serialized using its toString() method.
     */
    protected void addSerializableClass(Module module) {
        safeMapper.registerModule(module);
        fullMapper.registerModule(module);
    }

}
