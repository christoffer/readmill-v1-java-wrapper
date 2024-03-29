package com.readmill.tests;

import com.readmill.dal.ReadmillEntity;
import com.readmill.dal.ReadmillPing;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class PingTests {

  /**
   * Test that the defaults set when creating an "empty" object contains expected values.
   * This is to avoid having for example a null value show up when you are expecting an
   * integer or a String.
   */
  @Test
  public void testDefaults() {
    ReadmillPing ping = new ReadmillPing(null);

    assertEquals("readingId: ", 0, ping.getReadingId());
    assertEquals("identifier: ", "", ping.getIdentifier());
    assertEquals("progress: ", 0, ping.getProgress());
    assertEquals("duration: ", 0, ping.getDuration());
    assertEquals("occurredAt: ", null, ping.getOccurredAt());

  }

  @Test
  public void testInitFromJSON() throws JSONException {
    String mSampleResponse = TestUtils.getResourceContent("sample_ping_data.json");

    JSONObject json = new JSONObject(mSampleResponse);
    ReadmillPing ping = new ReadmillPing(json);

    assertEquals("readingId: ", 19, ping.getReadingId());
    assertEquals("identifier: ", "identify me!", ping.getIdentifier());
    assertEquals("progress: ", 14, ping.getProgress());
    assertEquals("duration: ", 4, ping.getDuration());
    assertEquals("occurredAt: ", "2011-07-29T23:16:43Z", ReadmillEntity.toISO8601(ping.getOccurredAt()));

  }

  @Test
  public void testConvertToJSON() throws JSONException {
    ReadmillPing ping = new ReadmillPing(null);

    ping.setReadingId(19);
    ping.setIdentifier("identify me!");
    ping.setProgress(14);
    ping.setDuration(4);
    ping.setOccurredAt(ReadmillEntity.parseUTC("2011-07-29T23:16:43Z"));

    JSONObject json = new JSONObject(ping.toJSON());

    assertEquals("readingId: ", 19, json.optLong("reading_id", 0));
    assertEquals("identifier: ", "identify me!", json.optString("identifier", ""));
    assertEquals("progress: ", 14, json.optLong("progress", 0));
    assertEquals("duration: ", 4, json.optLong("duration", 0));
    assertEquals("occurredAt: ", "2011-07-29T23:16:43Z", json.optString("occurred_at", ""));

  }

}
