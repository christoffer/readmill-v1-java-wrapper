package com.readmill.dal;

import com.readmill.api.*;
import com.readmill.api.ReadmillAPI.TokenStateListener;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class ReadmillDAL {
  private static final SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
  private static final TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");

  private ApiWrapper mWrapper;


  public ReadmillDAL(ApiWrapper wrapper) {
    mWrapper = wrapper;
  }

  public ReadmillDAL(String clientId, String clientSecret, URI redirectUri, Token token, Env env) {
    this(new ApiWrapper(clientId, clientSecret, redirectUri, token, env));
  }

  public void setAccessToken(Token newToken) {
    mWrapper.setToken(newToken);
  }

  public Token getAccessToken() {
    return mWrapper.getToken();
  }

  public boolean exchangeCodeForToken(String code) {
    try {
      mWrapper.authorizationCode(code);
      return mWrapper.getToken().valid();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public String getAuthorizeURL() {
    return mWrapper.authorizationCodeUrl().toString();
  }

  public Env getEnvironment() {
    return mWrapper.env;
  }

  public void addTokenStateChangeListener(TokenStateListener listener) {
    mWrapper.addTokenStateListener(listener);
  }

  public ArrayList<ReadmillBook> searchBooks(String searchText) throws ReadmillException {
    ArrayList<ReadmillBook> foundBooks = new ArrayList<ReadmillBook>();
    JSONArray jsonResult = getAssertedJSONArray(Request.to(Endpoints.BOOK_SEARCH).with("q", searchText));
    for (int i = 0; i < jsonResult.length(); i++) {
      try {
        ReadmillBook book = new ReadmillBook((JSONObject) jsonResult.get(i));
        foundBooks.add(book);
      } catch (JSONException ignored) {
        throw new ReadmillException();
      }
    }
    return foundBooks;
  }

  public ReadmillUser getCurrentUser() throws ReadmillException {
    JSONObject data = getAssertedJSON(Request.to(Endpoints.ME));
    return new ReadmillUser(data);
  }

  /**
   * Retrieve a Readmill Reading by resource URI
   * @param location resource URI
   * @return Readmill Reading
   * @throws ReadmillException When fetching the reading failed
   */
  public ReadmillReading getReading(String location) throws ReadmillException {
    JSONObject jsonReading = getAssertedJSON(Request.to(toResourceURI(location)));
    return new ReadmillReading(jsonReading);
  }

  /**
   * Retrieve a Readmill Reading by ID
   * @param id resource ID
   * @return Readmill Reading
   * @throws ReadmillException When fetching the reading failed
   */
  public ReadmillReading getReading(long id) throws ReadmillException {
    return getReading(String.format(Endpoints.READINGS, id));
  }

  public ArrayList<ReadmillReadingPeriod> getReadingPeriods(String resourceURI) throws ReadmillException {
    ArrayList<ReadmillReadingPeriod> periods = new ArrayList<ReadmillReadingPeriod>();
    JSONArray jsonResult = getAssertedJSONArray(Request.to(toResourceURI(resourceURI)));

    for (int i = 0; i < jsonResult.length(); i++) {
      try {
        ReadmillReadingPeriod period = new ReadmillReadingPeriod((JSONObject) jsonResult.get(i));
        periods.add(period);
      } catch (JSONException ignored) {
        throw new ReadmillException();
      }
    }
    return periods;
  }

  /**
   * Retrieve a Reading Period list by Reading id
   * @param readingId Reading to fetch periods for
   * @return list of Reading Periods
   * @throws ReadmillException When fetching failed
   */
  public ArrayList<ReadmillReadingPeriod> getReadingPeriods(long readingId) throws ReadmillException {
    return getReadingPeriods(String.format(Endpoints.PERIODS, readingId));
  }

  /**
   * Fetch a book by URI
   *
   * @param location URI to the book to fetch
   * @return A ReadmillBook
   * @throws ReadmillException When fetching the book failed
   */
  public ReadmillBook getBook(String location) throws ReadmillException {
    JSONObject data = getAssertedJSON(new Request(toResourceURI(location)));
    return new ReadmillBook(data);
  }

  /**
   * Create a new reading for the provided book
   *
   * @param book ReadmillBook to create a reading for
   * @param state Reading state of the reading to create (ReadmillReading.State)
   * @param privacy Privacy setting for the reading (ReadmillReading.Privacy)
   * @return the created ReadmillReading
   * @throws ReadmillException When the reading could not be created, or retrieving the created reading failed
   */
  public ReadmillReading createReading(ReadmillBook book, int state, int privacy) throws ReadmillException {
    Request request = Request.to(Endpoints.BOOK_READINGS, book.id);
    request.add(Params.Reading.IS_PRIVATE, privacy);
    request.add(Params.Reading.STATE, state);

    try {
      HttpResponse response = mWrapper.post(request);

      int statusCode = response.getStatusLine().getStatusCode();

      if(statusCode == HttpStatus.SC_CREATED || statusCode == HttpStatus.SC_CONFLICT) {
        String location = response.getFirstHeader("Location").getValue();
        return getReading(location);
      } else {
        throw new ReadmillException(response.getStatusLine().getStatusCode());
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new ReadmillException();
    }
  }

  /**
   * Create a Book at Readmill
   *
   * @param title Title of book
   * @param author Author of book
   * @return the created ReadmillBook
   * @throws ReadmillException When the operation failed
   */
  public ReadmillBook createBook(String title, String author) throws ReadmillException {
    Request request = Request.to(Endpoints.BOOKS);
    request.add(Params.Book.TITLE, title);
    request.add(Params.Book.AUTHOR, author);

    try {
      HttpResponse response = mWrapper.post(request);
      assertResponseCode(HttpStatus.SC_CREATED, response);
      String location = response.getFirstHeader("Location").getValue();
      return getBook(location);
    } catch (IOException e) {
      e.printStackTrace();
      throw new ReadmillException();
    }
  }

  /**
   * Create a Ping at Readmill
   *
   * @param identifier Unique reading session identifier
   * @param readingId Reading to ping
   * @param progress Progress when this ping was sent [0, 1.0]
   * @param durationSeconds How many seconds of effective reading time that has passed
   *          since the last ping
   * @param occurredAt When the ping occurred
   * @throws ReadmillException When the operation failed
   */
  public void postPing(String identifier, long readingId, double progress, long durationSeconds, Date occurredAt) throws ReadmillException {
    Request request = Request.to(Endpoints.PINGS, readingId);

    request.add(Params.Ping.IDENTIFIER, identifier);
    request.add(Params.Ping.PROGRESS, progress);
    request.add(Params.Ping.DURATION, durationSeconds);
    request.add(Params.Ping.OCCURRED_AT, toISODate(occurredAt));

    try {
      HttpResponse response = mWrapper.post(request);
      assertResponseCode(HttpStatus.SC_CREATED, response);
    } catch (IOException e) {
      e.printStackTrace();
      throw new ReadmillException();
    }
  }

  private void assertResponseCode(int statusCode, HttpResponse response) throws ReadmillException {
    if(response.getStatusLine().getStatusCode() != statusCode) {
      throw new ReadmillException(response.getStatusLine().getStatusCode());
    }
  }

  /**
   * Update the state of a Reading on the server
   *
   * @param reading Reading to update on Readmill
   * @throws ReadmillException When the operation failed
   */
  public void updateReading(ReadmillReading reading) throws ReadmillException {
    Request request = Request.to(Endpoints.READINGS, reading.id);

    request.add(Params.Reading.STATE, reading.getState());
    request.add(Params.Reading.IS_PRIVATE, reading.isPrivate() ? 1 : 0);

    try {
      HttpResponse response = mWrapper.put(request);
      assertResponseCode(HttpStatus.SC_OK, response);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  // Private



  /**
   * Get a JSON response from the given request. Only returns the JSON if the server responds with the expected
   * status code, otherwise throw an error.
   *
   * @param request Request to send
   * @param httpStatus Expected status code
   * @return JSON Object
   * @throws ReadmillException if an error occurred while responding, or if the status code was not the expected one.
   */
  private JSONObject getAssertedJSON(Request request, int httpStatus) throws ReadmillException {
    try {
      HttpResponse response = mWrapper.get(request);
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
  private JSONObject getAssertedJSON(Request request) throws ReadmillException {
    return getAssertedJSON(request, HttpStatus.SC_OK);
  }

  /**
   * Get a JSON response from the given request. Only returns the JSON if the server responds with the expected
   * status code, otherwise throw an error.
   *
   * @param request Request to send
   * @param httpStatus Expected status code
   * @return JSON Object
   * @throws ReadmillException if an error occurred while responding, or if the status code was not the expected one.
   */
  private JSONArray getAssertedJSONArray(Request request, int httpStatus) throws ReadmillException {
    try {
      HttpResponse response = mWrapper.get(request);
      int receivedStatus = response.getStatusLine().getStatusCode();
      if(receivedStatus != httpStatus)
        throw new ReadmillException(receivedStatus);
      return Http.getJSONArray(response);
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
  private JSONArray getAssertedJSONArray(Request request) throws ReadmillException {
    return getAssertedJSONArray(request, HttpStatus.SC_OK);
  }

  // Helper methods

  /**
   * Strip scheme and host part of URI.
   * This is necessary since the API uses relative URIs (it sets the host itself)
   * @param absoluteURI Absolute URI
   * @return relative URI
   */
  private String toResourceURI(String absoluteURI) {
    try {
      URI uri = new URI(absoluteURI);
      return String.format("%s%s%s", _blankOr(uri.getPath()), _blankOr(uri.getQuery()), _blankOr(uri.getFragment()));
    } catch (Exception ignored) {}
    return absoluteURI;
  }

  private String _blankOr(String value) {
    return value == null ? "" : value;
  }

  /**
   * Convert the given date to an isoDate suitable for passing to the API
   *
   * @param date Date to convert
   * @return the given date in the ISO 8601 format
   */
  protected String toISODate(Date date) {
    iso8601Format.setTimeZone(utcTimeZone);
    return iso8601Format.format(date);
  }

}
