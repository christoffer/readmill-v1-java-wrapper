package com.readmill.dal;

import org.json.JSONException;
import org.json.JSONObject;

public class ReadmillUser extends ReadmillEntity {
  public String username;
  public String fullname;
  public String avatarURL;

  public ReadmillUser() { super(); }
  public ReadmillUser(JSONObject json) {
    super(json);
  }

  public void convertFromJSON(JSONObject json) {
    id = json.optLong("id", -1);
    username = json.optString("username");
    fullname = json.optString("fullname");
    avatarURL = json.optString("avatar_url", null);
  }

  public JSONObject convertToJSON() {
    JSONObject json = new JSONObject();
    try {
      json.put("id", id);
      json.put("username", username);
      json.put("fullname", fullname);
      json.put("avatar_url", avatarURL);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return json;
  }

}
