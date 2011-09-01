package com.readmill.tests;

import com.readmill.dal.ReadmillUser;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class UserTests {

  @Test
  public void testDefault() throws JSONException {

    ReadmillUser user = new ReadmillUser();

    assertEquals("id: ", -1, user.getId());
    assertEquals("username: ", null, user.getUserName());
    assertEquals("fullname: ", null, user.getFullName());
    assertEquals("avatarUrl: ", null, user.getAvatarUrl());
    assertEquals("firstname: ", null, user.getFirstName());
    assertEquals("lastname: ", null, user.getLastName());
    assertEquals("country: ", null, user.getCountry());
    assertEquals("city: ", null, user.getCity());
    assertEquals("website: ", null, user.getWebsite());
    assertEquals("description: ", null, user.getDescription());
    assertEquals("uri", null, user.getUri());
    assertEquals("permalinkUrl: ", null, user.getPermalinkUrl());
    assertEquals("booksInteresting: ", 0, user.getBooksInteresting());
    assertEquals("booksOpen: ", 0, user.getBooksOpen());
    assertEquals("booksFinished: ", 0, user.getBooksFinished());
    assertEquals("booksAbandoned: ", 0, user.getBooksAbandoned());
    assertEquals("readings: ", null, user.getReadings());
    assertEquals("followings: ", 0, user.getFollowings());
    assertEquals("email: ", null, user.getEmail());

  }

  @Test
  public void testInitFromJSON() throws JSONException {
    // Load the sample JSON string from
    // /com/readmill/tests/resources/sample_user_data.json"
    String mSampleResponse = TestUtils.getResourceContent("sample_user_data.json");

    JSONObject json = new JSONObject(mSampleResponse);
    ReadmillUser user = new ReadmillUser(json);

    assertEquals("id: ", 101, user.getId());
    assertEquals("username: ", "jensnockert", user.getUserName());
    assertEquals("fullname: ", "Jens Nockert", user.getFullName());
    assertEquals("firstname: ", "Jens", user.getFirstName());
    assertEquals("lastname: ", "Nockert", user.getLastName());
    assertEquals("country: ", "Sweden", user.getCountry());
    assertEquals("city: ", "Lund", user.getCity());
    assertEquals("website: ", "", user.getWebsite());
    assertEquals("description: ", "", user.getDescription());
    assertEquals("uri", "http://api.readmill.com/users/101", user.getUri());
    assertEquals("permalinkUrl: ", "http://readmill.com/jensnockert",
        user.getPermalinkUrl());
    assertEquals("avatarUrl: ", "http://static.readmill.com/avatars/" +
        "d281aa934d75016e9f60c2db7e4a55e4-medium.png?1308255920",
        user.getAvatarUrl());
    assertEquals("booksInteresting: ", 1, user.getBooksInteresting());
    assertEquals("booksOpen: ", 1, user.getBooksOpen());
    assertEquals("booksFinished: ", 0, user.getBooksFinished());
    assertEquals("booksAbandoned: ", 0, user.getBooksAbandoned());
    assertEquals("readings: ",
        "http://api.readmill.com/users/101/readings",
        user.getReadings());
    assertEquals("followers: ", 2, user.getFollowers());
    assertEquals("followings: ", 2, user.getFollowings());
    assertEquals("email: ", "no@spam.plz", user.getEmail());
  }

  @Test
  public void testToConvertToJSON() throws JSONException {
    ReadmillUser user = new ReadmillUser();

    user.setId(101);
    user.setUserName("jensnockert");
    user.setFullName("Jens Nockert");
    user.setAvatarUrl("http://static.readmill.com/avatars/" +
        "d281aa934d75016e9f60c2db7e4a55e4-medium.png?1308255920");
    user.setFirstName("Jens");
    user.setLastName("Nockert");
    user.setCountry("Sweden");
    user.setCity("Lund");
    user.setWebsite("");
    user.setDescription("");
    user.setUri("http://api.readmill.com/users/101");
    user.setPermalinkUrl("http://readmill.com/jensnockert");
    user.setBooksInteresting(1);
    user.setBooksOpen(1);
    user.setBooksFinished(0);
    user.setBooksAbandoned(0);
    user.setReadings("http://api.readmill.com/users/101/readings");
    user.setFollowers(2);
    user.setFollowings(2);
    user.setEmail("no@spam.plz");

    JSONObject json = new JSONObject(user.toJSON());

    assertEquals("id: ", 101, json.optLong("id"));
    assertEquals("username: ", "jensnockert", json.optString("username"));
    assertEquals("fullname: ", "Jens Nockert", json.optString("fullname"));
    assertEquals("firstname: ", "Jens", json.optString("firstname"));
    assertEquals("lastname: ", "Nockert", json.optString("lastname"));
    assertEquals("country: ", "Sweden", json.optString("country"));
    assertEquals("city: ", "Lund", json.optString("city"));
    assertEquals("website: ", "", json.optString("website"));
    assertEquals("description: ", "", json.optString("description"));
    assertEquals("uri", "http://api.readmill.com/users/101",
        json.optString("uri"));
    assertEquals("permalinkUrl: ", "http://readmill.com/jensnockert",
        json.optString("permalink_url"));
    assertEquals("avatarUrl: ", "http://static.readmill.com/avatars/" +
        "d281aa934d75016e9f60c2db7e4a55e4-medium.png?1308255920",
        json.optString("avatar_url"));
    assertEquals("booksInteresting: ", 1, json.optLong("books_interesting"));
    assertEquals("booksOpen: ", 1, json.optLong("books_open"));
    assertEquals("booksFinished: ", 0, json.optLong("books_finished"));
    assertEquals("booksAbandoned: ", 0, json.optLong("books_abandoned"));
    assertEquals("readings: ",
        "http://api.readmill.com/users/101/readings",
        json.optString("readings"));
    assertEquals("followers: ", 2, json.optLong("followers"));
    assertEquals("followings: ", 2, json.optLong("followings"));
    assertEquals("email: ", "no@spam.plz", json.optString("email"));
  }
}
