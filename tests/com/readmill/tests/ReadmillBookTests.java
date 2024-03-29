package com.readmill.tests;

import com.readmill.dal.ReadmillBook;
import com.readmill.dal.ReadmillBookAsset;
import com.readmill.dal.ReadmillEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class ReadmillBookTests {

  @Test
  public void testDefaults() {

    ReadmillBook book = new ReadmillBook(null);

    // TODO change to empty strings instead of nulls for String values
    assertEquals("id var", -1, book.getId());
    assertEquals("title var", "", book.getTitle());
    assertEquals("author var", "", book.getAuthor());
    assertEquals("coverURL var", "", book.getCoverURL());
    assertEquals("isbn var", "", book.getISBN());
    assertEquals("story var", "", book.getStory());
    assertEquals("publishedAt var", null, book.getPublishedAt());
    assertEquals("language var", "", book.getLanguage());
    assertEquals("permalink var", "", book.getPermalink());
    assertEquals("permalinkURL var", "", book.getPermalinkURL());
    assertEquals("uri var", "", book.getURI());
    assertEquals("assets array", new ArrayList<ReadmillBookAsset>(), book.getAssets());
  }

  @Test
  public void testInitFromJSON() throws JSONException {
    String mSampleResponse = TestUtils.getResourceContent("sample_book_data.json");

    JSONObject json = new JSONObject(mSampleResponse);
    ReadmillBook book = new ReadmillBook(json);

    assertEquals("id var", 9, book.getId());
    assertEquals("title var", "Ulysses", book.getTitle());
    assertEquals("author var", "James Joyce", book.getAuthor());
    assertEquals("coverURL var", "http://static.readmill.com/covers/800-medium.png", book.getCoverURL());
    assertEquals("isbn var", "9780554354088", book.getISBN());
    assertEquals("story var", "Stately- plump Buck Mulligan...", book.getStory());
    assertEquals("publishedAt var", ReadmillEntity.parseUTC("2008-08-18T23:16:43Z"), book.getPublishedAt());
    assertEquals("language var", "en", book.getLanguage());
    assertEquals("permalink var", "ulysses", book.getPermalink());
    assertEquals("permalinkURL var", "http://readmill.com/books/ulysses", book.getPermalinkURL());
    assertEquals("uri var", "http://api.readmill.com/books/9", book.getURI());

    // assets
    assertEquals("assets array: object 0: vendor", "feedbooks", book.getAssets().get(0).getVendor());
    assertEquals("assets array: object 0: uri", "http://www.feedbooks.com/book/1232.epub", book.getAssets().get(0).getUri());
    assertEquals("assets array: object 0: acquisition_type", 0, book.getAssets().get(0).getAcquisitionType());
    assertEquals("assets array: object 1: vendor", "gutenberg", book.getAssets().get(1).getVendor());
    assertEquals("assets array: object 1: uri", "http://www.projectgutenberg.com/5432.epub", book.getAssets().get(1).getUri());
    assertEquals("assets array: object 1: acquisition_type", 1, book.getAssets().get(1).getAcquisitionType());

  }

  @Test
  public void testConvertToJSON() throws JSONException {
    // Set the expected values on the ReadmillBook object
    ReadmillBook book = new ReadmillBook(null);

    // This corresponding to the expected sample_book_data.json
    ReadmillBookAsset first_asset = new ReadmillBookAsset(null);
    ReadmillBookAsset second_asset = new ReadmillBookAsset(null);

    book.setId(9);
    book.setTitle("Ulysses");
    book.setAuthor("James Joyce");
    book.setCoverURL("http://static.readmill.com/covers/800-medium.png");
    book.setISBN("9780554354088");
    book.setStory("Stately- plump Buck Mulligan...");
    book.setPublishedAt(ReadmillEntity.parseUTC("2008-08-18T23:16:43Z"));
    book.setLanguage("en");
    book.setPermalink("ulysses");
    book.setPermalinkURL("http://readmill.com/books/ulysses");
    book.setURI("http://api.readmill.com/books/9");

    book.setAssets(new ArrayList<ReadmillBookAsset>());

    first_asset.setVendor("feedbooks");
    first_asset.setUri("http://www.feedbooks.com/book/1232.epub");
    first_asset.setAcquisitionType(0);

    second_asset.setVendor("gutenberg");
    second_asset.setUri("http://www.projectgutenberg.com/5432.epub");
    second_asset.setAcquisitionType(1);

    book.getAssets().add(first_asset);
    book.getAssets().add(second_asset);

    JSONObject json = new JSONObject(book.toJSON());

    assertEquals("id var", 9, json.optInt("id"));
    assertEquals("title var", "Ulysses", json.optString("title"));
    assertEquals("author var", "James Joyce", json.optString("author"));
    assertEquals("coverURL var", "http://static.readmill.com/covers/800-medium.png", json.optString("cover_url"));
    assertEquals("isbn var", "9780554354088", json.optString("isbn"));
    assertEquals("story var", "Stately- plump Buck Mulligan...", json.optString("story"));
    assertEquals("publishedAt var", "2008-08-18T23:16:43Z", json.optString("published_at"));
    assertEquals("language var", "en", json.optString("language"));
    assertEquals("permalink var", "ulysses", json.optString("permalink"));
    assertEquals("permalinkURL var", "http://readmill.com/books/ulysses", json.optString("permalink_url"));
    assertEquals("uri var", "http://api.readmill.com/books/9", json.optString("uri"));

    // Very long varibles
    String vendor0 = json.optJSONArray("assets").optJSONObject(0).optString("vendor");
    String assets_uri0 = json.optJSONArray("assets").optJSONObject(0).optString("uri");
    int acquisition_type0 = json.optJSONArray("assets").optJSONObject(0).optInt("acquisition_type");

    String vendor1 = json.optJSONArray("assets").optJSONObject(1).optString("vendor");
    String assets_uri1 = json.optJSONArray("assets").optJSONObject(1).optString("uri");
    int acquisition_type1 = json.optJSONArray("assets").optJSONObject(1).optInt("acquisition_type");

    // assets
    assertEquals("assets array: object 0: vendor", "feedbooks", vendor0);
    assertEquals("assets array: object 0: uri", "http://www.feedbooks.com/book/1232.epub", assets_uri0);
    assertEquals("assets array: object 0: acquisition_type", 0, acquisition_type0);

    assertEquals("assets array: object 1: vendor", "gutenberg", vendor1);
    assertEquals("assets array: object 1: uri", "http://www.projectgutenberg.com/5432.epub", assets_uri1);
    assertEquals("assets array: object 1: acquisition_type", 1, acquisition_type1);

  }
}
