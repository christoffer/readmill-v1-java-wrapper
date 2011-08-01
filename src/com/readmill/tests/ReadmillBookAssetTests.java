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

  @Test public void testDefault() throws JSONException {
    ReadmillBookAsset assets = new ReadmillBookAsset();

    // TODO change to empty string instead of null for String types
    assertEquals("vendor var", null, assets.getVendor());
    assertEquals("uri var", null, assets.getUri());
    assertEquals("acquisition_type var", 0, assets.getAcquisitionType());
  }

  /**
   * Test conversion from a JSON string into a ReadmillBookAsset object.
   * 
   * Please see the sampled file for values that should appear here.
   * 
   * @throws JSONException
   * @throws FileNotFoundException
   */

  @Test public void testInitFromJSON() throws JSONException {
    mSampleResponse = getResourceContent("sample_book_assets_data.json");

    JSONObject json = new JSONObject(mSampleResponse);
    ReadmillBookAsset assets = new ReadmillBookAsset(json);

    assertEquals("vendor var", "feedbooks", assets.getVendor());
    assertEquals("uri var", "http://www.feedbooks.com/book/1232.epub", assets.getUri());
    assertEquals("acquisition_type var", 0, assets.getAcquisitionType());
  }

  @Test public void testConvertToJSON() throws JSONException,
      DatatypeConfigurationException {
    ReadmillBookAsset assets = new ReadmillBookAsset();

    assets.setVendor("feedbooks");
    assets.setUri("http://www.feedbooks.com/book/1232.epub");
    assets.setAcquisitionType(0);

    JSONObject json = new JSONObject(assets.toJSON());

    assertEquals("vendor var", "feedbooks", json.optString("vendor"));
    assertEquals("uri var", "http://www.feedbooks.com/book/1232.epub", json.optString("uri"));
    assertEquals("acquisition_type var", 0, json.optInt("acquisition_type"));
  }
}
