package com.readmill.dal;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.readmill.api.ApiWrapper;
import com.readmill.api.Endpoints;
import com.readmill.api.Env;
import com.readmill.api.Http;
import com.readmill.api.Params;
import com.readmill.api.ReadmillAPI.TokenStateListener;
import com.readmill.api.Request;
import com.readmill.api.Token;

public class ReadmillDAL {

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

  public void addTokenStateChangeListener(TokenStateListener listener) {
    mWrapper.addTokenStateListener(listener);
  }

  public ArrayList<ReadmillBook> searchBooks(String searchText) {
    JSONArray receivedArray;
    ArrayList<ReadmillBook> foundBooks = new ArrayList<ReadmillBook>();
    try {
      Request request = Request.to(Endpoints.BOOK_SEARCH).with("q", searchText);

      HttpResponse response = mWrapper.get(request);
      if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
        throw new IOException("Failed to search for books");
      }

      receivedArray = Http.getJSONArray(mWrapper.get(request));
      for (int i = 0; i < receivedArray.length(); i++) {
        try {
          ReadmillBook book = new ReadmillBook((JSONObject) receivedArray.get(i));
          foundBooks.add(book);
        } catch (JSONException ignored) {}
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return foundBooks;
  }

  public ReadmillUser getCurrentUser() {
    try {
      JSONObject data = Http.getJSON(mWrapper.get(Request.to(Endpoints.ME)));
      return new ReadmillUser(data);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ReadmillReading getReading(String uri) {
    try {
      JSONObject data = Http.getJSON(mWrapper.get(Request.to(uri)));
      return new ReadmillReading(data);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ReadmillReading getReading(long id) {
    return getReading(String.format(Endpoints.READINGS, id));
  }

  public ReadmillReading createReading(ReadmillBook book) {
    Request request = Request.to(Endpoints.BOOK_READINGS, book.id);
    request.add(Params.Reading.IS_PRIVATE, 0);
    request.add(Params.Reading.STATE, ReadmillReading.State.OPEN);

    try {
      HttpResponse response = mWrapper.post(request);

      int statusCode = response.getStatusLine().getStatusCode();

      if(statusCode == HttpStatus.SC_CREATED || statusCode == HttpStatus.SC_CONFLICT) {
        String location = _getNakedURI(response.getFirstHeader("Location").getValue());
        return getReading(location);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  public boolean post(ReadmillPing ping) {
    Request request = Request.to(Endpoints.PING, ping.readingId);

    request.add(Params.Ping.IDENTIFIER, ping.identifier);
    request.add(Params.Ping.PROGRESS, ping.progress);
    request.add(Params.Ping.DURATION, ping.duration);
    request.add(Params.Ping.OCCURRED_AT, ping.occuredAt);

    try {
      HttpResponse response = mWrapper.post(request);
      System.out.println("PING RETURNED: " + response.getStatusLine().getStatusCode());
      return response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED;
    } catch (IOException e) {
      e.printStackTrace();
    }

    return false;
  }

  /**
   * Strip the Scheme and Host from the given URI string. This is used on returned URIs
   * since they are absolute, but this wrapper operates on relative URIs. TODO: Make the
   * wrapper accept full URIs instead.
   */
  private String _getNakedURI(String uriToStrip) {
    try {
      URI uri = new URI(uriToStrip);
      String host = uri.getHost();
      int splitIndex = uriToStrip.indexOf(host) + host.length();
      return uriToStrip.substring(splitIndex);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return uriToStrip;
  }
}
