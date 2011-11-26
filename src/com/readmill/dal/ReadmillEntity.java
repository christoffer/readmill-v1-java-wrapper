package com.readmill.dal;

import com.readmill.api.Http;
import com.readmill.api.Request;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Abstract Readmill Entity. Requires implementations to implement fromJSON and toJSON for
 * serializing and deserializing to JSON format.
 *
 * @author christoffer
 */
public abstract class ReadmillEntity {
  public final String TAG = this.getClass().getName();
  private static final SimpleDateFormat iso8601DateFormat = _createISO8601Format();

  protected long id = -1;

  public ReadmillEntity(JSONObject json) {
    if(json == null) {
      convertFromJSON(new JSONObject());
    } else {
      convertFromJSON(json);
    }
  }

  /**
   * @return True if this entity has a set ID, false otherwise
   */
  public boolean isNew() {
    return id == -1;
  }

  /**
   * @return The entity ID
   */
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  /**
   * @return String value of this object in JSON syntax
   */
  public String toJSON() {
    try {
      JSONObject json = convertToJSON();
      return json.toString();
    } catch(JSONException e) {
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
   *
   * @return The converted JSON object
   * @throws org.json.JSONException When the object could not be converted
   */
  abstract protected JSONObject convertToJSON() throws JSONException;

  /**
   * Helper method to parse a UTC string into a Date
   *
   * @param dateString UTC Date
   * @return Date of the parsed date
   */
  public static Date parseUTC(String dateString) {
    if(dateString == null || dateString.equals("") || dateString.equals("null")) {
      return null;
    } else {
      try {
        return iso8601DateFormat.parse(dateString);
      } catch(ParseException ignored) {
        return null;
      }
    }
  }

  public static String toISO8601(Date date) {
    if(date == null) {
      return "";
    }
    return iso8601DateFormat.format(date);
  }

  private static SimpleDateFormat _createISO8601Format() {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    df.setTimeZone(TimeZone.getTimeZone("UTC"));
    return df;
  }

  /**
   * Handle the (broken) optString behavior in Android org.json.
   * Android uses an older version of org.json which returns the literal string "null"
   * when the string is null.
   * <p/>
   * Always use this instead of <jsonObject>.optString()
   *
   * @param json JSONObject to extract key from
   * @param key  Key to extract
   * @return The correct string value, or an empty string if not set
   */
  protected String getString(JSONObject json, String key) {
    return json.isNull(key) ? "" : json.optString(key);
  }


  /**
   * Get a JSON response from the given request. Only returns the JSON if the server responds with the expected
   * status code, otherwise throw an error.
   *
   * @param request    Request to send
   * @param httpStatus Expected status code
   * @return JSON Object
   * @throws ReadmillException if an error occurred while responding, or if the status code was not the expected one.
   */
  protected static JSONObject getAssertedJSON(Request request, int httpStatus) throws ReadmillException {
    try {
      HttpResponse response = ReadmillDAL.getWrapper().get(request);
      int receivedStatus = response.getStatusLine().getStatusCode();
      if(receivedStatus != httpStatus)
        throw new ReadmillException(receivedStatus);
      return Http.getJSON(response);
    } catch(IOException e) {
      throw new ReadmillException();
    }
  }

  /**
   * Get a JSON response from the given request. Only returns the JSON if the server responds with 200 : OK
   *
   * @param request Request to send
   * @return JSON object
   * @throws ReadmillException if an error occurred or the response was not 200 : OK
   */
  protected static JSONObject getAssertedJSON(Request request) throws ReadmillException {
    return getAssertedJSON(request, HttpStatus.SC_OK);
  }

  /**
   * Get a JSON response from the given request. Only returns the JSON if the server responds with the expected
   * status code, otherwise throw an error.
   *
   * @param request    Request to send
   * @param httpStatus Expected status code
   * @return JSON Object
   * @throws ReadmillException if an error occurred while responding, or if the status code was not the expected one.
   */
  protected static JSONArray getAssertedJSONArray(Request request, int httpStatus) throws ReadmillException {
    try {
      HttpResponse response = ReadmillDAL.getWrapper().get(request);
      int receivedStatus = response.getStatusLine().getStatusCode();
      if(receivedStatus != httpStatus)
        throw new ReadmillException(receivedStatus);
      return Http.getJSONArray(response);
    } catch(IOException e) {
      throw new ReadmillException();
    }
  }

  /**
   * Make sure the response has the given Status code.
   *
   * @param statusCode Desired status code
   * @param response   response object
   * @throws ReadmillException if the status of the response is not the desired one
   */
  protected static void assertResponseCode(int statusCode, HttpResponse response) throws ReadmillException {
    if(response.getStatusLine().getStatusCode() != statusCode) {
      throw new ReadmillException(response.getStatusLine().getStatusCode());
    }
  }

  /**
   * Get a JSON response from the given request. Only returns the JSON if the server responds with 200 : OK
   *
   * @param request Request to send
   * @return JSON object
   * @throws ReadmillException if an error occurred or the response was not 200 : OK
   */
  protected static JSONArray getAssertedJSONArray(Request request) throws ReadmillException {
    return getAssertedJSONArray(request, HttpStatus.SC_OK);
  }

  // Helper methods

  /**
   * Strip scheme and host part of URI.
   * This is necessary since the API uses relative URIs (it sets the host itself)
   *
   * @param absoluteURI Absolute URI
   * @return relative URI
   */
  protected static String toResourceURI(String absoluteURI) {
    try {
      URI uri = new URI(absoluteURI);
      return String.format("%s%s%s", _blankOr(uri.getPath()), _blankOr(uri.getQuery()), _blankOr(uri.getFragment()));
    } catch(Exception ignored) {}
    return absoluteURI;
  }

  private static String _blankOr(String value) {
    return value == null ? "" : value;
  }

}
