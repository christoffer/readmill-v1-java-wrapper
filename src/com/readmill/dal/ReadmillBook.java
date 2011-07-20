package com.readmill.dal;

import org.json.JSONException;
import org.json.JSONObject;

public class ReadmillBook extends ReadmillEntity {

  public String title;
  public String author;
  public String coverURL;
  
  public ReadmillBook() { super(); }

  public ReadmillBook(JSONObject json) {
    super(json);
  }

  @Override public void convertFromJSON(JSONObject json) {
    id = json.optLong("id", -1);
    title = json.optString("title");
    author = json.optString("author");
    coverURL = json.optString("cover_url", null);
  }

  @Override public JSONObject convertToJSON() throws JSONException {
    JSONObject json = new JSONObject();
    json.put("id", id);
    json.put("title", title);
    json.put("author", author);
    json.put("cover_url", coverURL);
    return json;
  }

}
