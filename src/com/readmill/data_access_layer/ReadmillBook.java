package com.readmill.data_access_layer;

import org.json.JSONException;
import org.json.JSONObject;

public class ReadmillBook extends ReadmillObject {
  public String title;
  public String author;
  public String coverURL;

  public ReadmillBook(JSONObject json) {
    super(json);
  }

  public ReadmillBook(String json) throws JSONException {
    super(json);
  }

  @Override
  public void fromJSON(JSONObject json) {
    title = json.optString("title", "Unknown Title");
    author = json.optString("author", "Unknown Author");
    coverURL = json.optString("cover_url", null);
    id = json.optLong("id", -1);
  }

  @Override
  public String toJSON() {
    try {
      JSONObject book = new JSONObject();
      book.put("title", title);
      book.put("author", author);
      book.put("cover_url", coverURL);
      book.put("id", id);
      return book.toString();
    } catch (JSONException e) {
      e.printStackTrace();
      return "{}";
    }
  }
  
}
