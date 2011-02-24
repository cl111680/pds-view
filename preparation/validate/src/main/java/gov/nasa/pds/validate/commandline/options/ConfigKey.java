// Copyright 2006-2010, by the California Institute of Technology.
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
// $Id: ConfigKey.java -1M 2010-11-04 18:13:37Z (local) $
package gov.nasa.pds.validate.commandline.options;

/**
 * An interface that contains the valid property keys for the Validate Tool
 * configuration file.
 *
 * @author mcayanan
 *
 */
public class ConfigKey {
    /** List of file patterns to look for if traversing a target directory. */
    public static final String REGEXP = "validate.regexp";

    /** The report. */
    public static final String REPORT = "validate.report";

    /** A list of targets to validate. */
    public static final String TARGET = "validate.target";

    /** A severity level for the output report. */
    public static final String VERBOSE = "validate.verbose";

    /** A list of schema files to validate against. */
    public static final String SCHEMA = "validate.schema";

    public static final String CATALOG = "validate.catalog";

    /** Enables/disables direcotry recursion. */
    public static final String LOCAL = "validate.local";
}
