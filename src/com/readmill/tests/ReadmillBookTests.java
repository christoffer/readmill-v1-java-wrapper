package com.readmill.tests;

import static org.junit.Assert.*;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.readmill.dal.ReadmillBook;

public class ReadmillBookTests extends ReadmillTestCase{
	
	private String mSampleResponse;

	@Before
	public void loadJSON(){
		//Load the sample JSON string from /com/readmill/tests/resources/sample_reading_response.json"
		mSampleResponse = getResourceContent("sample_book_data.json");
	}
	
	/**
	 * Test that the defaults set when creating an "empty" object contains
	 * expected values. This is to avoid having for example a null value show up
	 * when you are expecting an integer or a String.
	 */
	@Test  
	//@Ignore
	public void testDefaults() {

		ReadmillBook book = new ReadmillBook();
		
		assertEquals("id var", -1, book.id);
		
		assertNull("title var", book.title);
		assertNull("author var", book.author);
		assertNull("coverURL var", book.coverURL);
		assertNull("isbn var", book.isbn);
		assertNull("story var", book.story);
		assertNull("publishedAT var", book.publishedAT);
		assertNull("language var", book.language);
		assertNull("permalink var", book.permalink);
		assertNull("permalinkURL var", book.permalinkURL);
		assertNull("uri var", book.uri);
		assertEquals("assets array", new ArrayList<ReadmillBookAsset>(), book.assets);

	  }
	 
	/**
	 *  Test conversion from a JSON string into a ReadmillBook object.
	 *  Please see the file com/readmill/tests/resources/sample_reading_response.json
	 *  for what values that should appear here.
	 * 	@throws JSONException
	 */

	@Test  
	public void testInitFromJSON() throws JSONException {
		
		JSONObject json = new JSONObject(mSampleResponse);
	    ReadmillBook book = new ReadmillBook(json);
	    
	    assertEquals("id var", 9, book.id);
	    
		assertEquals("title var", "Ulysses", book.title);
		assertEquals("author var", "James Joyce", book.author);
		assertEquals("coverURL var", "http://static.readmill.com/covers/800-medium.png", book.coverURL);
		assertEquals("isbn var", "9780554354088", book.isbn);
		assertEquals("story var", "Stately- plump Buck Mulligan...", book.story);
		assertEquals("publishedAT var", "2008-08-18", book.publishedAT);
		assertEquals("language var", "en", book.language);
		assertEquals("permalink var", "ulysses", book.permalink);
		assertEquals("permalinkURL var", "http://readmill.com/books/ulysses", book.permalinkURL);
		assertEquals("uri var", "http://api.readmill.com/books/9", book.uri);
		
		assertEquals("assets array: object 0: vendor", "feedbooks", book.assets.get(0).vendor);
		assertEquals("assets array: object 0: uri", "http://www.feedbooks.com/book/1232.epub", book.assets.get(0).uri);
		assertEquals("assets array: object 0: acquisition_type", 0, book.assets.get(0).acquisition_type);
		
		assertEquals("assets array: object 1: vendor", "gutenberg", book.assets.get(1).vendor);
		assertEquals("assets array: object 1: uri", "http://www.projectgutenberg.com/5432.epub", book.assets.get(1).uri);
		assertEquals("assets array: object 1: acquisition_type", 1, book.assets.get(1).acquisition_type);

	}
	 
	@Test
	public void testConvertToJSON() throws JSONException {
		// Set the expected values on the ReadmillBook object
		ReadmillBook book = new ReadmillBook();
	    
		//This corresponding to the expected sample_book_data.json
		//might do a more general solution?? TODO discuss
	    ReadmillBookAsset asset1 = new ReadmillBookAsset();
	    ReadmillBookAsset asset2 = new ReadmillBookAsset();
	    
	    book.id = 9;
	    book.title = "Ulysses";
	    book.author = "James Joyce";
	    book.coverURL = "http://static.readmill.com/covers/800-medium.png";
		book.isbn = "9780554354088";
		book.story = "Stately- plump Buck Mulligan...";
		book.publishedAT = "2008-08-18";
		book.language = "en";
		book.permalink = "ulysses";
		book.permalinkURL = "http://readmill.com/books/ulysses";
		book.uri = "http://api.readmill.com/books/9";
		
		book.assets = new ArrayList<ReadmillBookAsset>();

		asset1.vendor = "feedbooks";
		asset1.uri = "http://www.feedbooks.com/book/1232.epub";
		asset1.acquisition_type = 0;
		
		asset2.vendor = "gutenberg";
		asset2.uri = "http://www.projectgutenberg.com/5432.epub";
		asset2.acquisition_type = 1;
		
		book.assets.add(asset1);
		book.assets.add(asset2);
	    
		// Instantly convert the String produced from toJSON() into a JSON object
	    // this is good because A) The JSson has the correct syntax (or an error will be raised)
	    // and B) we can easily use the JSONObject to assert expected values
		JSONObject json = new JSONObject(book.toJSON());
	
	    assertEquals("id var", 9, json.optInt("id"));
		assertEquals("title var", "Ulysses", json.optString("title"));
		assertEquals("author var", "James Joyce", json.optString("author"));
		assertEquals("coverURL var", "http://static.readmill.com/covers/800-medium.png", json.optString("cover_url"));
		assertEquals("isbn var", "9780554354088", json.optString("isbn"));
		assertEquals("story var", "Stately- plump Buck Mulligan...", json.optString("story"));
		assertEquals("publishedAT var", "2008-08-18", json.optString("published_at"));
		assertEquals("language var", "en", json.optString("language"));
		assertEquals("permalink var", "ulysses", json.optString("permalink"));
		assertEquals("permalinkURL var", "http://readmill.com/books/ulysses", json.optString("permalink_url"));
		assertEquals("uri var", "http://api.readmill.com/books/9", json.optString("uri"));
		
		//Very long varibles
		String vendor0 = json.optJSONArray("assets").optJSONObject(0).optString("vendor");
	    String assets_uri0 = json.optJSONArray("assets").optJSONObject(0).optString("uri");
	    int acquisition_type0 = json.optJSONArray("assets").optJSONObject(0).optInt("acquisition_type");
	    
	    String vendor1 = json.optJSONArray("assets").optJSONObject(1).optString("vendor");
	    String assets_uri1 = json.optJSONArray("assets").optJSONObject(1).optString("uri");
	    int acquisition_type1 = json.optJSONArray("assets").optJSONObject(1).optInt("acquisition_type");
		
		assertEquals("assets array: object 0: vendor", "feedbooks", vendor0);
		assertEquals("assets array: object 0: uri", "http://www.feedbooks.com/book/1232.epub", assets_uri0);
		assertEquals("assets array: object 0: acquisition_type", 0, acquisition_type0);
		
		assertEquals("assets array: object 1: vendor", "gutenberg", vendor1);
		assertEquals("assets array: object 1: uri", "http://www.projectgutenberg.com/5432.epub", assets_uri1);
		assertEquals("assets array: object 1: acquisition_type", 1, acquisition_type1);

	  }

}
