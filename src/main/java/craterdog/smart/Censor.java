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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * This class performs the masking function on a specified string value using a specified mask.
 *
 * @author Yan Ma
 * @author Derk Norton
 */
public class Censor {

    static private final XLogger logger = XLoggerFactory.getXLogger(Censor.class);

    private final char maskingCharacter;

    /**
     * This constructor creates a new <code>Censor</code> object that uses the default 'X' character
     * for masking.
     */
    public Censor() {
        this.maskingCharacter = 'X';
    }

    /**
     * This constructor creates a new <code>Censor</code> object that uses the specified character
     * for masking.
     *
     * @param maskingCharacter The character to be used for masking the sensitive characters.
     */
    public Censor(char maskingCharacter) {
        this.maskingCharacter = maskingCharacter;
    }

    /**
     * This method masks the given string according to the groups specified in the specified mask.
     * Parentheses are used to group the characters to be masked. You should make sure that no
     * parenthesis are used in the parts that should remain as plain text.
     * <p>For example, a credit card number of 1234-5678-9012-3456 would be masked as follows with these masks:
     * <ul>
     * <li>
     * mask pattern: <code>^\d{4}-(\d{4})-(\d{4})-\d{4}$</code>  yields: <code>1234-XXXX-XXXX-3456</code>
     * </li>
     * <li>
     * mask pattern: <code>^\d{4}(-(\d{4}-){2})\d{4}$</code>  yields: <code>1234XXXXXXXXXXX3456</code>
     * </li>
     * <li>
     * mask pattern: <code>^((\d{4}-){3})\d{4}$</code>  yields: <code>XXXXXXXXXXXXXXX3456</code>
     * </li>
     * </ul>
     * <p> The java.util.regex.Pattern and java.util.regex.Matcher classes are used to locate the
     * groups included in the round brackets. Please note, if the group is included in a
     * repetition such as *, + or {m,n}, only the last appearance of the group would be masked.
     * <p>For example:
     * <ul>
     * <li>
     * mask pattern: <code>^(\d{4}-){3}\d{4}$</code>  yields: <code>1234-5678-XXXXX3456</code>
     * </li>
     * </ul>
     *
     * @param value The value to be masked.
     * @param mask The regular expression used to extract and mask the sensitive information.
     * @return The masked value.
     */
    public String process(String value, String mask) {

        // check for empty value string
        if (value == null || value.isEmpty()) {
            return value;
        }

        // check for empty mask expression
        if (mask == null || mask.isEmpty()) {
            return value;
        }

        // flatten all the nested intervals, the outermost interval for each is all we care about
        ArrayList<Interval> intervals = mergeIntervals(value, mask);
        if (intervals == null) {
            return "MASKING_ERROR";
        }

        StringBuilder stringBuilder = new StringBuilder();
        int index = 0; // the index walks through the value string
        for (Interval interval : intervals) {
            stringBuilder.append(value.substring(index, interval.start));
            for (int i = 0; i < interval.end - interval.start; i++) {
                stringBuilder.append(maskingCharacter);
            }
            index = interval.end;
        }

        // append the rest of value unmasked.
        if (index < value.length()) {
            stringBuilder.append(value.substring(index));
        }
        return stringBuilder.toString();
    }


    /*
     * This method will "flatten" any nested intervals contained in "(" and ")" pairs in the
     * specified string and merge them into a single interval that spans the outer-most pair.
     * This is needed since java.util.Matcher does not guarantee that the sequence of the
     * groups are maintained.
     */
    private static ArrayList<Interval> mergeIntervals(String string, String mask) {
        ArrayList<Interval> result;
        try {

            // generate the matcher
            Pattern pattern = Pattern.compile(mask);
            Matcher matcher = pattern.matcher(string);
            if (!matcher.find()) {
                return null;
            }
            int groupCount = matcher.groupCount();
            if (groupCount == 0) {
                return null;
            }

            // pull out the potentially nested intervals
            ArrayList<Interval> intervals = new ArrayList<>();
            for (int i = 1; i <= groupCount; i++) {
                intervals.add(new Interval(matcher.start(i), matcher.end(i)));
            }

            if (groupCount == 1) {
                result = intervals;
            } else {
                Collections.sort(intervals, new IntervalComparator());
                int start = intervals.get(0).start;
                int end = intervals.get(0).end;
                result = new ArrayList<>();
                for (int i = 1; i < intervals.size(); i++) {
                    if (intervals.get(i).start > end) {
                        // this is a new interval
                        result.add(new Interval(start, end));
                        start = intervals.get(i).start;
                        end = intervals.get(i).end;
                    } else {
                        end = Math.max(end, intervals.get(i).end);
                    }
                }
                // don't forget to add the last group into the final results
                result.add(new Interval(start, end));
            }

        } catch (PatternSyntaxException e) {
            logger.error(e.getLocalizedMessage());
            result = null;
        }

        return result;
    }

    /**
     * This class is used to capture an interval defined by matching parentheses in a masking
     * pattern.
     */
    static private class Interval {

        int start;
        int end;

        Interval() {
            start = 0;
            end = 0;
        }

        Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }

    }

    /**
     * This class is used to compare two intervals.
     */
    static private class IntervalComparator implements Comparator<Interval> {

        @Override
        public int compare(Interval source, Interval target) {
            return source.start - target.start;
        }
    }

}
