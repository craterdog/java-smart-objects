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

import craterdog.primitives.Angle;
import craterdog.primitives.BinaryString;
import craterdog.primitives.Probability;
import craterdog.primitives.Tag;
import craterdog.primitives.TextString;
import java.net.URI;
import org.joda.time.DateTime;

/**
 * This class implements a simple example smart object.
 *
 * @author Derk Norton
 */
public class ExampleSmartObject extends SmartObject<ExampleSmartObject> {

    public int bar;
    public double pi = Math.PI;
    public DateTime timestamp = new DateTime("2015-08-28T19:59:55.585Z");
    public Angle angle = new Angle(2 * pi);
    public BinaryString binary = new BinaryString("0123456789ABCDEF");
    public Probability probability = new Probability(0.5);
    public Tag tag = new Tag("L97CRGYM17CRGFV2C43FKRJWRYK09WHH");
    public TextString text = new TextString("This is a test string.");
    public URI uri = URI.create("http://google.com");
    public String foo = "This is another test string.";

    @Sensitive(type = "credit card", mask = Sensitive.MASK_CREDIT_CARD_NUMBER)
    public String card = "1234-5678-9012-3456";

    public ExampleSmartList list = new ExampleSmartList();
    public ExampleSmartMap map = new ExampleSmartMap();

}
