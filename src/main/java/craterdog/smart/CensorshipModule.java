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

import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * This class is a Jackson module that can be added to an object mapper to handle the masking of
 * sensitive attributes during string serialization. Any string attributes that are annotated with
 * the <code>@Sensitive(mask="some pattern")</code> annotation will be masked accordingly.
 *
 * @author Yan Ma
 */
public class CensorshipModule extends SimpleModule {

    /**
     * The default constructor adds a censorship serializer that has no mask defined. However, the
     * actual decision about whether or not to censor an attribute string is based on a mask that is
     * specified with an <code>@Sensitive(mask="some mask")</code> annotation on the attribute.
     */
    public CensorshipModule() {
        super("CensorshipModule");
        addSerializer(String.class, new CensorshipSerializer());
    }

}
