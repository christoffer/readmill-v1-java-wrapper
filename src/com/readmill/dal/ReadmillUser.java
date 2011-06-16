package com.readmill.dal;

import org.json.JSONException;
import org.json.JSONObject;

public class ReadmillUser extends ReadmillEntity {
  public long readmillId;
  public String username;
  public String fullname;
  public String avatarUrl;

  public ReadmillUser() { super(); }
  public ReadmillUser(JSONObject json) {
    super(json);
  }

  public void convertFromJSON(JSONObject json) {
    readmillId = json.optLong("id", -1);
    username = json.optString("username");
    fullname = json.optString("fullname");
  }

  public JSONObject convertToJSON() {
    JSONObject json = new JSONObject();
    try {
      json.put("id", readmillId);
      json.put("username", username);
      json.put("fullname", fullname);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return json;
  }

}
