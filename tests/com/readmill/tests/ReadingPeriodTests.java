package com.readmill.tests;

import static junit.framework.Assert.assertEquals;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.readmill.dal.ReadmillReadingPeriod;


/**
 * @author Christoffer
 */

public class ReadingPeriodTests {

  @Test
  public void testDefault() throws JSONException {
    ReadmillReadingPeriod period = new ReadmillReadingPeriod();

    assertEquals(-1, period.getId());
    assertEquals(-1, period.getReadingId());
    assertEquals(-1, period.getUserId());
    assertEquals(null, period.getStartedAt());
    assertEquals(0, period.getDuration());
    assertEquals(0.0, period.getProgress(), 0.001);
  }

  @Test
  public void testInitFromJSON() throws JSONException {
    String sampleResponse = com.readmill.tests.TestUtils.getResourceContent("sample_reading_period.json");
    JSONObject json = new JSONObject(sampleResponse);
    ReadmillReadingPeriod period = new ReadmillReadingPeriod(json);

    assertEquals(13, period.getId());
    assertEquals(21, period.getReadingId());
    assertEquals(2, period.getUserId());
    assertEquals("2010-12-14T10:36:21Z", period.toUTC(period.getStartedAt()));
    assertEquals(300, period.getDuration());
    assertEquals(0.87, period.getProgress(), 0.001);
  }

  @Test
  public void testConvertToJSON() throws JSONException {
    ReadmillReadingPeriod period = new ReadmillReadingPeriod();

    period.setId(12);
    period.setReadingId(34);
    period.setUserId(56);
    period.setStartedAt(period.parseUTC("2009-03-14T09:05:35Z"));
    period.setDuration(61922);
    period.setProgress(0.6543f);

    JSONObject json = new JSONObject(period.toJSON());

    assertEquals(12, json.optLong("id"));
    assertEquals(34, json.optLong("reading_id"));
    assertEquals(56, json.optLong("user_id"));
    assertEquals("2009-03-14T09:05:35Z", json.optString("started_at"));
    assertEquals(61922, json.optLong("duration"));
    assertEquals(0.6543, json.optDouble("progress"), 0.001);

  }
}