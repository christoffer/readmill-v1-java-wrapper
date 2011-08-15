package com.readmill.tests;

import java.util.Calendar;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.readmill.dal.ReadmillEntity;

public class ReadmillEntityTests extends ReadmillTestCase {
  ReadmillEntity instance = new DummyImplementation();

  @Test public void parseUTC() {
    String validUTCString = "2009-08-21T09:11:21Z";

    Calendar cal = Calendar.getInstance();
    cal.setTime(instance.parseUTC(validUTCString));

    // Need to convert the calendar to UTC so we can compare with the string above
    // even when this test is run on computer with a different time zone
    cal.setTimeZone(TimeZone.getTimeZone("UTC"));

    assertNotNull(cal);
    assertEquals(2009, cal.get(Calendar.YEAR));
    assertEquals(Calendar.AUGUST, cal.get(Calendar.MONTH));
    assertEquals(21, cal.get(Calendar.DAY_OF_MONTH));

    assertEquals(9, cal.get(Calendar.HOUR_OF_DAY));
    assertEquals(11, cal.get(Calendar.MINUTE));
    assertEquals(21, cal.get(Calendar.SECOND));

    assertEquals(TimeZone.getTimeZone("UTC"), cal.getTimeZone());
  }

  @Test public void parseUTCWithInvalidString() {
    assertEquals(null, instance.parseUTC("20090821T09:11:21Z")); // no dashes
  }

  @Test public void toUTC() {
    Calendar cal = Calendar.getInstance();

    cal.setTimeZone(TimeZone.getTimeZone("UTC"));

    cal.set(Calendar.YEAR, 2011);
    cal.set(Calendar.MONTH, Calendar.FEBRUARY); // February is not within DST
    cal.set(Calendar.DAY_OF_MONTH, 28);

    cal.set(Calendar.HOUR_OF_DAY, 7);
    cal.set(Calendar.MINUTE, 55);
    cal.set(Calendar.SECOND, 01);

    assertEquals("2011-02-28T07:55:01Z", instance.toUTC(cal.getTime()));
  }

  @Test public void toJSONDoesNotFailOnJSONError() {
    assertEquals("{}", instance.toJSON());
  }

  /**
   * Dummy implementation lets us test inherited methods on the abstract ReadmillEntity
   * class
   * 
   * @author christoffer
   */
  class DummyImplementation extends ReadmillEntity {
    @Override protected void convertFromJSON(JSONObject json) {}

    @Override protected JSONObject convertToJSON() throws JSONException {
      throw new JSONException("Failed to convert");
    }
  }

}
