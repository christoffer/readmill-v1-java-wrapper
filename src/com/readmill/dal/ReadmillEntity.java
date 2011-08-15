package com.readmill.dal;

import java.text.ParseException;
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
  public final String TAG = this.getClass().getName();
  private static final SimpleDateFormat utcDateFormat = createUTCTimeFormat();

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
      JSONObject json = convertToJSON();
      return json.toString();
    } catch (JSONException e) {
      e.printStackTrace();
      return "{}";
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
   * Helper method to parse a UTC string into a Date
   * 
   * @param dateString UTC Date
   * @return Date of the parsed date
   */
  public Date parseUTC(String dateString) {
    if(dateString == null || dateString.equals("") || dateString.equals("null")) {
      return null;
    } else {
      try {
        return utcDateFormat.parse(dateString);
      } catch (ParseException ignored) {
        return null;
      }
    }
  }

  public String toUTC(Date date) {
    if(date == null) {
      return "null";
    }
    return utcDateFormat.format(date);
  }

  private static SimpleDateFormat createUTCTimeFormat() {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    df.setTimeZone(TimeZone.getTimeZone("UTC"));
    return df;
  }

}
