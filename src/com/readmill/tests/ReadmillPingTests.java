package com.readmill.tests;

import javax.xml.datatype.DatatypeConfigurationException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.readmill.dal.ReadmillPing;

public class ReadmillPingTests extends ReadmillTestCase {

	private String mSampleResponse;

	/**
	 * Test that the defaults set when creating an "empty" object contains
	 * expected values. This is to avoid having for example a null value show up
	 * when you are expecting an integer or a String.
	 */
	@Test
	// @Ignore
	public void testDefaults() {
		ReadmillPing ping = new ReadmillPing();

		assertEquals("readingId: ", 0, ping.getReadingId());
		assertEquals("identifier: ", null, ping.getIdentifier());
		assertEquals("progress: ", 0, ping.getProgress());
		assertEquals("duration: ", 0, ping.getDuration());
		assertEquals("occurredAt: ", null, ping.getOccurredAt());

	}

	/**
	 * Test conversion from a JSON string into a ReadmillBook object. Please see
	 * the file com/readmill/tests/resources/sample_reading_response.json for
	 * what values that should appear here.
	 * 
	 * @throws JSONException
	 * @throws DatatypeConfigurationException
	 */

	@Test
	// @Ignore
	public void testInitFromJSON() throws JSONException,
			DatatypeConfigurationException {
		// Load the sample JSON string from
		// /com/readmill/tests/resources/sample_ping_data.json
		mSampleResponse = getResourceContent("sample_ping_data.json");

		JSONObject json = new JSONObject(mSampleResponse);
		ReadmillPing ping = new ReadmillPing(json);

		assertEquals("readingId: ", 19, ping.getReadingId());
		assertEquals("identifier: ", "identify me!", ping.getIdentifier());
		assertEquals("progress: ", 14, ping.getProgress());
		assertEquals("duration: ", 4, ping.getDuration());
		assertEquals("occurredAt: ", "2011-07-29T23:16:43Z",
				ping.toUTC(ping.getOccurredAt()));

	}

	/**
	 * testConvertToJSON
	 * @throws JSONException
	 * @throws DatatypeConfigurationException
	 */
	@Test
	// @Ignore
	public void testConvertToJSON() throws JSONException,
			DatatypeConfigurationException {
		// Set the expected values on the ReadmillBook object
		ReadmillPing ping = new ReadmillPing();

		ping.setReadingId(19);
		ping.setIdentifier("identify me!");
		ping.setProgress(14);
		ping.setDuration(4);
		ping.setOccurredAt(ping.fromUTC("2011-07-29T23:16:43Z"));

		// Instantly convert the String produced from toJSON() into a JSON
		// object
		// this is good because A) The JSson has the correct syntax (or an error
		// will be raised)
		// and B) we can easily use the JSONObject to assert expected values
		JSONObject json = new JSONObject(ping.toJSON());

		assertEquals("readingId: ", 19, json.optLong("reading_id", 0));
		assertEquals("identifier: ", "identify me!",
				json.optString("identifier", ""));
		assertEquals("progress: ", 14, json.optLong("progress", 0));
		assertEquals("duration: ", 4, json.optLong("duration", 0));
		assertEquals("occurredAt: ", "2011-07-29T23:16:43Z",
				json.optString("occurred_at", ""));
	}

}
