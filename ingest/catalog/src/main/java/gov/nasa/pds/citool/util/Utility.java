// Copyright 2009, by the California Institute of Technology.
// ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
// Any commercial use must be negotiated with the Office of Technology Transfer
// at the California Institute of Technology.
//
// This software is subject to U. S. export control laws and regulations
// (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
// is subject to U.S. export control laws and regulations, the recipient has
// the responsibility to obtain export licenses or other export authority as
// may be required before exporting such information to foreign countries or
// providing access to foreign nationals.
//
// $Id$

package gov.nasa.pds.citool.util;

import gov.nasa.pds.citool.diff.DiffRecord;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utility {
    public static String stripWhitespace(String value) {
        return filterString(stripNewLines(value));
    }

    public static String stripNewLines(String value) {
        String filteredValue = value;
        //Perform whitespace stripping
        //First replace all hyphen line.separator with ""
        filteredValue = filteredValue.replaceAll("-" + System.getProperty("line.separator"), "");
        //Next replace all line.separator with " "
        filteredValue = filteredValue.replaceAll(System.getProperty("line.separator"), " ");
        return filteredValue;
    }

    public static String filterString(String value) {
        String filteredValue = value;
        //Replace all '_' with ' '
        filteredValue = filteredValue.replaceAll("_", " ");
        //Replace multiple spaces with a single space
        filteredValue = filteredValue.replaceAll("\\s+", " ");
        //Trim whitespace
        filteredValue = filteredValue.trim();
        return filteredValue;
    }

    public static String trimString(String value, int length) {
        String trimmedString = value;

        if (trimmedString.length() > length*3)
            trimmedString = trimmedString.substring(0, length*3);
        trimmedString = stripNewLines(trimmedString);
        trimmedString = filterString(trimmedString);
        if (trimmedString.length() > length)
            trimmedString = trimmedString.substring(0, length-1);

        return trimmedString;
    }

    /**
     * Strips only newline characters and extra whitespaces.
     *
     * @param value A string value.
     * @return The filtered value.
     */
    public static String stripOnlyWhitespaceAndNewLine(String value) {
        String filteredValue = value;

        //Next replace all line.separator with " "
        filteredValue = filteredValue.replaceAll(System.getProperty("line.separator"), " ");
        //Replace multiple spaces with a single space
        filteredValue = filteredValue.replaceAll("\\s+", " ");
        //Trim whitespace
        filteredValue = filteredValue.trim();

        return filteredValue;
    }

    /**
     * Determines whether a value is null as defined by the PDS.
     *
     * @param value The value in question
     * @return true if the value is null, false otherwise.
     */
    public static boolean isNull(String value) {
        if("N/A".equals(value) ||
           "UNK".equals(value) ||
           "NULL".equals(value)) {
            return true;
        }
        else
            return false;
    }

    public static List<String> removeQuotes(List<String> list) {
        for(int i=0; i < list.size(); i++) {
            list.set(i, list.get(i).toString().replace('"', ' ')
                    .trim());
        }
        return list;
    }

    /**
     * Convert a string to a URL.
     * @param s The string to convert
     * @return A URL of the input string
     */
    public static URL toURL(String s) throws MalformedURLException {
        URL url = null;
        try {
            url = new URL(s);
        } catch (MalformedURLException ex) {
            url = new File(s).toURI().toURL();
        }
        return url;
    }

    /**
     * Get the current date time.
     *
     * @return A date time.
     */
    public static String getDateTime() {
        SimpleDateFormat df = new SimpleDateFormat(
        "EEE, MMM dd yyyy 'at' hh:mm:ss a");
        Date date = Calendar.getInstance().getTime();
        return df.format(date);
    }

    public static String printDiff(String indent, List<DiffRecord> records) {
        String message = "";
        for (DiffRecord dr : records) {
            if (!"".equals(dr.getInfo())) {
                message += indent + dr.getInfo() + "\n";
            }
            for (String msg : dr.getFromSource()) {
                message += indent + msg + "\n";
            }
            for (String msg : dr.getFromTarget()) {
                message += indent + msg + "\n";
            }
            message += "\n";
        }
        return message;
    }
}