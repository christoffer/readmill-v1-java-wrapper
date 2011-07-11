package com.readmill.dal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Abstract Readmill Entity. Requires implementations to implement fromJSON and toJSON for
 * serializing and deserializing to JSON format.
 * 
 * @author christoffer
 * 
 */
public abstract class ReadmillEntity {
  private static final SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
  private static final TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
  public final String TAG = this.getClass().getName();
  public long id = -1;

  public ReadmillEntity(JSONObject json) {
    convertFromJSON(json);
  }

  public ReadmillEntity() {}

  /**
   * @return True if this entity has a set ID, false otherwise
   */
  public boolean isNew() {
    return id == -1;
  }

  /**
   * @return String value of this object in JSON syntax
   */
  public String toJSON() {
    try {
      return convertToJSON().toString();
    } catch (JSONException e) {
      e.printStackTrace();
      return "{}";
    }
  }

  /**
   * Parse a JSON string and apply the values to this object
   * 
   * @param json JSON string to pull values from
   */
  public void fromJSON(String json) {
    try {
      convertFromJSON(new JSONObject(json));
    } catch (JSONException e) {
      throw new RuntimeException("Malformed JSON: " + (json.length() > 80 ? json.substring(0, 80) + "..." : json));
    }
  }

  /**
   * Extensions must implement a way to initialize the DAL object from a JSON object
   * 
   * @param json JSON to extract values from
   */
  abstract protected void convertFromJSON(JSONObject json);

  /**
   * Extensions must implement a way to create a JSON string from a DAL object
   */
  abstract protected JSONObject convertToJSON() throws JSONException;

  /**
   * Convert the given date to an isoDate suitable for passing to the API
   *
   * @param date Date to convert
   * @return the given date in the ISO 8601 format
   */
  protected String isoDate(Date date) {
    iso8601Format.setTimeZone(utcTimeZone);
    return iso8601Format.format(date);
  }
}
