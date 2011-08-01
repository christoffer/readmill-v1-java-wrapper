package com.readmill.tests;

import javax.xml.datatype.DatatypeConfigurationException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.readmill.dal.ReadmillBookAsset;

/**
 * 
 * @author Lovisa
 * 
 */
public class ReadmillBookAssetTests extends ReadmillTestCase {

	private String mSampleResponse;

	/**
	 * Test that the defaults set when creating an "empty" object contains
	 * expected values. This is to avoid having for example a null value show up
	 * when you are expecting an integer or a String.
	 */
	@Test
	public void testDefault() throws JSONException {
		ReadmillBookAsset assets = new ReadmillBookAsset();

		assertEquals("vendor var", null, assets.getVendor());
		assertEquals("uri var", null, assets.getUri());
		assertEquals("acquisition_type var", 0, assets.getAcquisitionType());
	}

	/**
	 * Test conversion from a JSON string into a ReadmillBookAsset object.
	 * Please see the file
	 * com/readmill/tests/resources/sample_reading_response.json for what values
	 * that should appear here.
	 * 
	 * @throws JSONException
	 * @throws FileNotFoundException
	 *             //Change before commit TODO
	 */
	@Test
	public void testInitFromJSON() throws JSONException {
		// Load the sample JSON string from
		// /com/readmill/tests/resources/sample_book_assets_data.json"
		mSampleResponse = getResourceContent("sample_book_assets_data.json");

		JSONObject json = new JSONObject(mSampleResponse);
		ReadmillBookAsset assets = new ReadmillBookAsset(json);

		assertEquals("vendor var", "feedbooks", assets.getVendor());
		assertEquals("uri var", "http://www.feedbooks.com/book/1232.epub",
				assets.getUri());
		assertEquals("acquisition_type var", 0, assets.getAcquisitionType());
	}

	@Test
	public void testConvertToJSON() throws JSONException,
			DatatypeConfigurationException {
		ReadmillBookAsset assets = new ReadmillBookAsset();

		assets.setVendor("feedbooks");
		assets.setUri("http://www.feedbooks.com/book/1232.epub");
		assets.setAcquisitionType(0);

		// Instantly convert the String produced from toJSON() into a JSON
		// object
		// this is good because A) The JSson has the correct syntax (or an error
		// will be raised)
		// and B) we can easily use the JSONObject to assert expected values
		JSONObject json = new JSONObject(assets.toJSON());

		assertEquals("vendor var", "feedbooks", json.optString("vendor"));
		assertEquals("uri var", "http://www.feedbooks.com/book/1232.epub",
				json.optString("uri"));
		assertEquals("acquisition_type var", 0, json.optInt("acquisition_type"));
	}
}
