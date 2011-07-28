package com.readmill.tests;

import static org.junit.Assert.assertEquals;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Lovisa
 *
 */
public class ReadmillBookAssetTests extends ReadmillTestCase{
	
	private String mSampleResponse;

	@Before
	public void loadJSON(){
		//Load the sample JSON string from /com/readmill/tests/resources/sample_reading_response.json"
		mSampleResponse = getResourceContent("sample_book_assets_data.json");
	}
		
	/**
	 * Test that the defaults set when creating an "empty" object contains
	 * expected values. This is to avoid having for example a null value show up
	 * when you are expecting an integer or a String.
	 */
	@Test
	public void testDefault() throws JSONException{
		ReadmillBookAsset assets = new ReadmillBookAsset();
		
		assertEquals("vendor var", null, assets.vendor);
		assertEquals("uri var", null, assets.uri);
		assertEquals("acquisition_type var", 0, assets.acquisition_type);
	}
	
	/**
	 *  Test conversion from a JSON string into a ReadmillBookAsset object.
	 *  Please see the file com/readmill/tests/resources/sample_reading_response.json
	 *  for what values that should appear here.
	 * 	@throws JSONException
	 * 	@throws FileNotFoundException //Change before commit TODO
	 */
	@Test
	public void testInitFromJSON() throws JSONException{
		
		JSONObject json = new JSONObject(mSampleResponse);
		ReadmillBookAsset assets = new ReadmillBookAsset(json);

		assertEquals("vendor var", "feedbooks", assets.vendor);
		assertEquals("uri var", "http://www.feedbooks.com/book/1232.epub", assets.uri);
		assertEquals("acquisition_type var", 0, assets.acquisition_type);
	}
	
	@Test
	public void testConvertToJSON() throws JSONException {
		ReadmillBookAsset assets = new ReadmillBookAsset();
		
		assets.vendor = "feedbooks";
		assets.uri = "http://www.feedbooks.com/book/1232.epub";
		assets.acquisition_type = 0 ;
		
		// Instantly convert the String produced from toJSON() into a JSON object
	    // this is good because A) The JSson has the correct syntax (or an error will be raised)
	    // and B) we can easily use the JSONObject to assert expected values
	    JSONObject json = new JSONObject(assets.toJSON());
		
		assertEquals("vendor var", "feedbooks", json.optString("vendor"));
		assertEquals("uri var", "http://www.feedbooks.com/book/1232.epub", json.optString("uri"));
		assertEquals("acquisition_type var", 0, json.optInt("acquisition_type"));
	}
}
