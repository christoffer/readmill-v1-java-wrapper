package com.readmill.tests;

import com.readmill.dal.ReadmillBook;
import com.readmill.dal.ReadmillReading;
import com.readmill.dal.ReadmillUser;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;

public class ReadingTests {

  @Test
  public void testDefaults() {
    ReadmillReading reading = new ReadmillReading();

    assertEquals("id: ", -1, reading.getId());
    assertEquals("state: ", 0, reading.getState());
    assertEquals("estimated time left: ", 0, reading.getEstimatedTimeLeft());
    assertEquals("progress ", 0.0, reading.getProgress(), 0.0001);
    assertEquals("recommended: ", false, reading.isRecommended());
    assertEquals("is private: ", false, reading.isPrivate());
    assertEquals("closing remark: ", null, reading.getClosingRemark());
    assertEquals("abandonedAt: ", null, reading.getAbandonedAt());
    assertEquals("finishedAt: ", null, reading.getFinishedAt());
    assertEquals("createdAt: ", null, reading.getCreatedAt());
    assertEquals("touchedAt; ", null, reading.getTouchedAt());
    assertEquals("startedAt: ", null, reading.getStartedAt());
    assertEquals("duration: ", null, reading.getDuration());
    assertEquals("locations: ", null, reading.getLocations());
    assertEquals("permalinkUrl; ", null, reading.getPermalinkUrl());
    assertEquals("uri: ", null, reading.getUri());
    assertEquals("periods: ", null, reading.getPeriods());
    assertEquals("highlights: ", null, reading.getHighlights());
    assertEquals("average period time: ", null, reading.getAveragePeriodTime());

    // TODO Imeplement actual tests for the objects below
    // book
    // assertEquals("author of book: ", null, reading.getBook());
    // user
    // assertEquals("id of user: ", null, reading.getUser());
  }

  @Test
  public void testInitFromJSON() throws JSONException {
    String mSampleResponse = TestUtils.getResourceContent("sample_reading_response.json");

    JSONObject json = new JSONObject(mSampleResponse);
    ReadmillReading reading = new ReadmillReading(json);

    assertEquals(21, reading.getId());
    assertEquals("state: ", 3, reading.getState());
    assertEquals("estimated time left: ", 0, reading.getEstimatedTimeLeft());
    assertEquals("progress ", 0.0, reading.getProgress(), 0.0001);
    assertEquals("recommended: ", false, reading.isRecommended());
    assertEquals("is private: ", false, reading.isPrivate());
    assertEquals("The stories about the early days of Thefacebook was really great, hearing about how they were hacking like crazy from that house in Palo Alto. Although, like all tech-books it slowed down towards the end. The last 100 pages was a fight.", reading.getClosingRemark());
    assertEquals("abandonedAt: ", null, reading.getAbandonedAt());
    assertEquals("finishedAt: ", "2010-12-14T10:36:31Z", reading.toUTC(reading.getFinishedAt()));
    assertEquals("createdAt: ", "2010-11-29T20:25:56Z", reading.toUTC(reading.getCreatedAt()));
    assertEquals("touchedAt; ", "2010-12-14T10:36:21Z", reading.toUTC(reading.getTouchedAt()));
    assertEquals("startedAt: ", "2010-11-29T20:25:56Z", reading.toUTC(reading.getStartedAt()));
    assertEquals("duration: ", "25500", reading.getDuration());
    assertEquals("locations: ", "http://api.readmill.com/readings/21/locations", reading.getLocations());
    assertEquals("permalinkUrl; ", "http://readmill.com/henrik/reads/the-facebook-effect", reading.getPermalinkUrl());
    assertEquals("uri: ", "http://api.readmill.com/readings/21", reading.getUri());
    assertEquals("periods: ", "http://api.readmill.com/readings/21/periods", reading.getPeriods());
    assertEquals("highlights: ", "http://api.readmill.com/readings/21/highlights", reading.getHighlights());
    assertEquals("average period time: ", "5100.0", reading.getAveragePeriodTime());

    // book
    ReadmillBook book = reading.getBook();
    assertEquals("author of book: ", "David Kirkpatrick", book.getAuthor());
    assertEquals("title of book: ", "The Facebook Effect", book.getTitle());
    assertEquals("permalink of book: ", "the-facebook-effect", book.getPermalink());

    // user
    ReadmillUser user = reading.getUser();
    assertEquals("id of user: ", 2, user.getId());
    assertEquals("username: ", "henrik", user.getUserName());
    assertEquals("fullname: ", "Henrik Berggren", user.getFullName());
  }

  @Test// @Ignore
  public void testConvertToJSON() throws JSONException {
    ReadmillReading reading = new ReadmillReading();

    reading.setId(21);
    reading.setState(3);
    reading.setPrivate(false);
    reading.setRecommended(false);
    reading.setClosingRemark("The stories about the early days of Thefacebook was really great, hearing about how they were hacking like crazy from that house in Palo Alto. Although, like all tech-books it slowed down towards the end. The last 100 pages was a fight.");
    reading.setTouchedAt(reading.parseUTC("2010-12-14T10:36:21Z"));
    reading.setStartedAt(reading.parseUTC("2010-11-29T20:25:56Z"));
    reading.setFinishedAt(reading.parseUTC("2010-12-14T10:36:31Z"));
    reading.setAbandonedAt(reading.parseUTC("null"));
    reading.setCreatedAt(reading.parseUTC("2010-11-29T20:25:56Z"));
    reading.setDuration("25500");
    reading.setProgress(1.0);
    reading.setEstimatedTimeLeft(0);
    reading.setAveragePeriodTime("5100.0");
    reading.setLocations("http://api.readmill.com/readings/21/locations");
    reading.setPermalinkUrl("http://readmill.com/henrik/reads/the-facebook-effect");
    reading.setUri("http://api.readmill.com/readings/21");
    reading.setPeriods("http://api.readmill.com/readings/21/periods");
    reading.setHighlights("http://api.readmill.com/readings/21/highlights");

    // user
    ReadmillUser user = new ReadmillUser();

    user.setUserName("henrik");
    user.setFullName("Henrik Berggren");
    user.setId(22);

    reading.setUser(user);

    // book
    ReadmillBook book = new ReadmillBook();

    book.setId(33);
    book.setAuthor("David Kirkpatrick");
    book.setTitle("The Facebook Effect");
    book.setPermalink("the-facebook-effect");

    reading.setBook(book);

    JSONObject json = new JSONObject(reading.toJSON());

    assertEquals(21, json.optLong("id", -1));
    assertEquals("state: ", 3, json.optInt("state"));
    assertEquals("estimated time left: ", 0, json.optLong("estimated_time_left", 0));
    assertEquals("progress: ", 1.0, json.optDouble("progress"), 0.0001);

    // assertFalse might change for readability
    assertFalse("recommended: ", json.optBoolean("recommended"));
    assertFalse("is private: ", json.optBoolean("private"));

    assertEquals("The stories about the early days of Thefacebook was really great, hearing about how they were hacking like crazy from that house in Palo Alto. Although, like all tech-books it slowed down towards the end. The last 100 pages was a fight.", json.optString("closing_remark"));
    assertEquals("abandonedAt: ", "null", json.optString("abandoned_at"));
    assertEquals("finishedAt: ", "2010-12-14T10:36:31Z", json.optString("finished_at"));
    assertEquals("createdAt: ", "2010-11-29T20:25:56Z", json.optString("created_at"));
    assertEquals("touchedAt; ", "2010-12-14T10:36:21Z", json.optString("touched_at"));
    assertEquals("startedAt: ", "2010-11-29T20:25:56Z", json.optString("started_at"));
    assertEquals("duration: ", "25500", json.optString("duration"));
    assertEquals("locations: ", "http://api.readmill.com/readings/21/locations", json.optString("locations"));
    assertEquals("permalinkUrl; ", "http://readmill.com/henrik/reads/the-facebook-effect", json.optString("permalink_url"));
    assertEquals("uri: ", "http://api.readmill.com/readings/21", json.optString("uri"));
    assertEquals("periods: ", "http://api.readmill.com/readings/21/periods", json.optString("periods"));
    assertEquals("highlights: ", "http://api.readmill.com/readings/21/highlights", json.optString("highlights"));
    assertEquals("average period time: ", "5100.0", json.optString("average_period_time"));

    // should not include sub resources when converting to json
    assertNull("Does not include subresource user", json.optJSONObject("user"));
    assertNull("Does not include subresource book", json.optJSONObject("book"));

  }
}