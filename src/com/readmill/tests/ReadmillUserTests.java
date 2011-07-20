package com.readmill.tests;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import com.readmill.dal.ReadmillUser;

/*
TODO
 These are the available fields (w/ example data) that should be 
 avaialable on a user.
 
 Taken from: https://github.com/Readmill/API/wiki/Users

 {
   "id": 2,
   "username": "henrik",
   "firstname": "Henrik",
   "lastname": "Berggren",
   "fullname": "Henrik Berggren",
   "country": "Sweden",
   "city": "Stockholm",
   "website": "http://henrikberggren.com",
   "description": "Passionate reader of internet lit and crime novels",
   "uri": "http://api.readmill.com/users/2",
   "permalink_url":"http://readmill.com/henrik",
   "books_interesting":23,
   "books_open":2,
   "books_finished":3,
   "books_abandoned":0,
   "readings":"http://api.readmill.com/users/2/readings",
   "avatar_url":"http://static.readmill.com/avatars/c4f5.png",
   "followers":125,
   "followings":115
 }
 
*/


public class ReadmillUserTests extends TestCase {

  public void testDefaultValues() {
    ReadmillUser user = new ReadmillUser();
    assertEquals(-1, user.id);
    assertEquals("", user.username);
    assertEquals("", user.fullname);
    assertEquals(null, user.avatarURL);
  }
  
  public void testConvertFromJSON() throws JSONException {
    JSONObject json = new JSONObject(
        "{" +
          "'id': '45', " +
          "'username': 'christoffer', " +
          "'fullname': 'Christoffer Klang', " +
          "'avatar_url': 'http://images.com/christoffer.png'" +
        "}");

    ReadmillUser user = new ReadmillUser(json);
    assertEquals(45, user.id);
    assertEquals("christoffer", user.username);
    assertEquals("Christoffer Klang", user.fullname);
    assertEquals("http://images.com/christoffer.png", user.avatarURL);
  }

  public void testConvertToJSON() throws JSONException {
    ReadmillUser user = new ReadmillUser();
    user.id = 45;
    user.username = "christoffer";
    user.fullname = "Christoffer Klang";
    user.avatarURL = "http://images.com/christoffer.png";

    JSONObject json = new JSONObject(user.toJSON());

    assertEquals(45, json.optInt("id"));
    assertEquals("christoffer", json.optString("username"));
    assertEquals("Christoffer Klang", json.optString("fullname"));
    assertEquals("http://images.com/christoffer.png", json.optString("avatar_url"));
  }
  
}
