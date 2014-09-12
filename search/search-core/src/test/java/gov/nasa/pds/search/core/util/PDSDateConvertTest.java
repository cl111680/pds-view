package gov.nasa.pds.search.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import gov.nasa.pds.search.core.constants.Constants;
import gov.nasa.pds.search.core.test.SearchCoreTest;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test PDSDateConvert to verify expected output is being produced
 * 
 * @author jpadams
 * 
 */
@RunWith(JUnit4.class)
public class PDSDateConvertTest extends SearchCoreTest {

	/** Maps possible input to expected output */
	private static Map<String, String> timeMap;

	/**
	 * Set up map of possible inputs and correct output for testing
	 */
	@BeforeClass
	public final static void oneTimeSetUp() {
		timeMap = new HashMap<String, String>();
		// timeMap.put("processing__unk", Constants.DEFAULT_STOPTIME);
		// timeMap.put("processing__1988-08-01", "1988-08-01T00:00:00.000Z");
		timeMap.put("1999", "1999-01-01T00:00:00.000Z");
		timeMap.put("2000-03", "2000-03-01T00:00:00.000Z");
		timeMap.put("2000-003", "2000-01-03T00:00:00.000Z");
		timeMap.put("2001-4-30", "2001-04-30T00:00:00.000Z");
		timeMap.put("2001-04-30", "2001-04-30T00:00:00.000Z");
		timeMap.put("2002-05-15t14:20", "2002-05-15T14:20:00.000Z");
		timeMap.put("1994-07-20t20:16:32", "1994-07-20T20:16:32.000Z");
		timeMap.put("1977-09-05t00:14z", "1977-09-05T00:14:00.000Z");
		timeMap.put("1999-354t06:53:12z", "1999-12-20T06:53:12.000Z");
		timeMap.put("1979-02-26T00:00:35.897", "1979-02-26T00:00:35.897Z");
		timeMap.put("19851108070408.649", "1985-11-08T07:04:08.649Z");
		timeMap.put("2009-06-30t00:00:00.00", "2009-06-30T00:00:00.000Z");
		timeMap.put("2004-001t00:00:00.000", "2004-01-01T00:00:00.000Z");
		timeMap.put("2002-02-19t19:00:29.6236", "2002-02-19T19:00:29.623Z");
		timeMap.put("2001-266t10:44:29.81", "2001-09-23T10:44:29.810Z");
		timeMap.put("2008-03-12T24:00:00.000", "2008-03-13T00:00:00.000Z");
		timeMap.put("UNK", Constants.DEFAULT_STOPTIME);
		timeMap.put("N/A", Constants.DEFAULT_STOPTIME);
		timeMap.put("NULL", Constants.DEFAULT_STOPTIME);
		timeMap.put("UNKNOWN", Constants.DEFAULT_STOPTIME);

		// Handle invalid PSA data for now
		timeMap.put("2005-11-27_14:29:04.0", "2005-11-27T14:29:04.000Z");
		timeMap.put("2005-06-27_18:48:06.416666666", "2005-06-27T18:48:06.416Z");

		/** Taken from XML validation test files (see resources/testing/time **/
		// Negative ASCII_Date_YMD: valid values
		timeMap.put("-0123", "-0123-01-01T00:00:00.000Z");
		timeMap.put("-1008", "-1008-01-01T00:00:00.000Z");
		timeMap.put("-2008", "-2008-01-01T00:00:00.000Z");
		timeMap.put("-2008-01", "-2008-01-01T00:00:00.000Z");

		// Positive ASCII_Date_YMD: valid values
		timeMap.put("0123", "0123-01-01T00:00:00.000Z");
		timeMap.put("1008", "1008-01-01T00:00:00.000Z");
		timeMap.put("2008", "2008-01-01T00:00:00.000Z");
		timeMap.put("2008-01", "2008-01-01T00:00:00.000Z");

		// Negative ASCII_Date_Time_YMD: valid values
		timeMap.put("-2008-01-01", "-2008-01-01T00:00:00.000Z");
		timeMap.put("-2008-01-02", "-2008-01-02T00:00:00.000Z");

		// Positive ASCII_Date_Time_YMD: valid values
		timeMap.put("2008-01-01", "2008-01-01T00:00:00.000Z");
		timeMap.put("2008-01-02", "2008-01-02T00:00:00.000Z");

		// Negative ASCII_Date_Time_YMD: valid values
		timeMap.put("-2008-01-01T23:01:01.0000", "-2008-01-01T23:01:01.000Z");
		timeMap.put("-2008-01-01T23:01:01.9999", "-2008-01-01T23:01:01.999Z");

		// ASCII_Date_YMD with Z: valid values
		timeMap.put("-2008-01-01T23:01:01.0000Z", "-2008-01-01T23:01:01.000Z");
		timeMap.put("-2008-01-01T23:01:01.9999Z", "-2008-01-01T23:01:01.999Z");

		// HH:mm:ss valid values
		timeMap.put("12:12:59", "12:12:59");
		timeMap.put("15:02:28", "15:02:28");
	}

	/**
	 * Clear the time map
	 */
	@AfterClass
	public final static void oneTimetearDown() {
		timeMap.clear();
	}

	/**
	 * Loop through the time map and verify code is producing expected values
	 */
	@Test
	public void testPDSDateConvertValid() {

		// Test Valid Values
		try {
			for (String inputDateTime : timeMap.keySet()) {
				String outputDateTime = PDSDateConvert.convert("test",
						inputDateTime);
				System.out.println("\n  Input   - " + inputDateTime
						+ "\n  Output  - " + outputDateTime);
				/*
				 * if (!outputDateTime.equals(timeMap.get(inputDateTime))) {
				 * fail("Invalid conversion." + "\n  Input   - " + inputDateTime
				 * + "\n  Output  - " + outputDateTime + "\n  Expected - " +
				 * timeMap.get(inputDateTime)); }
				 */
				assertEquals(outputDateTime, timeMap.get(inputDateTime));
			}
		} catch (Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}
}