package com.readmill.tests;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import com.readmill.dal.ReadmillBook;


/*
 TODO
 The following fields should be accessable on instances of ReadmillBook 

{
  "id": 9,
  "title": "Ulysses",
  "author": "James Joyce",
  "isbn": "9780554354088",
  "story": "Stately- plump Buck Mulligan...",
  "published_at": "2008-08-18",
  "language": "en",
  "permalink": "ulysses",
  "permalink_url": "http://readmill.com/books/ulysses",
  "uri": "http://api.readmill.com/books/9",
  "cover_url": "http://static.readmill.com/covers/800-medium.png",
  "assets": [
    {
      "vendor": "feedbooks",
      "uri": "http://www.feedbooks.com/book/1232.epub",
      "acquisition_type": 0
    }
  ]
}

*/

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
