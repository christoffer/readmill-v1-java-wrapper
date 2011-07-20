package com.readmill.tests;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import com.readmill.dal.ReadmillBook;

public class ReadmillBookTests extends TestCase {

  public void testDefaults() {
    ReadmillBook book = new ReadmillBook();
    assertEquals(-1, book.id);
    assertEquals("", book.title);
    assertEquals("", book.author);
    assertEquals(null, book.coverURL);
  }

  public void testInitFromJSON() throws JSONException {
    JSONObject json = new JSONObject(
        "{" +
          "'id': '45', " +
          "'title': 'Metamorphosis', " +
          "'author': 'Franz Kafka', " +
          "'cover_url': 'http://images.com/cover.png'" +
        "}");
    ReadmillBook book = new ReadmillBook(json);
    assertEquals(45, book.id);
    assertEquals("Metamorphosis", book.title);
    assertEquals("Franz Kafka", book.author);
    assertEquals("http://images.com/cover.png", book.coverURL);
  }

  public void testConvertToJSON() throws JSONException {
    ReadmillBook book = new ReadmillBook();
    book.id = 45;
    book.title = "Metamorphosis";
    book.author = "Franz Kafka";
    book.coverURL = "http://images.com/cover.png";
    JSONObject json = new JSONObject(book.toJSON());

    assertEquals(45, json.optInt("id"));
    assertEquals("Metamorphosis", json.optString("title"));
    assertEquals("Franz Kafka", json.optString("author"));
    assertEquals("http://images.com/cover.png", json.optString("cover_url"));
  }
}
