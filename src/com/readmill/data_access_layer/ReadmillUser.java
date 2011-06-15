package com.readmill.data_access_layer;

import org.json.JSONException;
import org.json.JSONObject;

public class ReadmillUser extends ReadmillObject {
  public long readmillId;
  public String username;
  public String fullname;
  public String avatarUrl;

  public ReadmillUser(String string) throws JSONException {
    super(string);
  }

  public ReadmillUser(JSONObject userData) {
    super(userData);
  }

  public void fromJSON(JSONObject json) {
    readmillId = json.optLong("id", -1);
    username = json.optString("username");
    fullname = json.optString("fullname");
  }

  public String toJSON() {
    try {
      JSONObject user = new JSONObject();
      user.put("id", readmillId);
      user.put("username", username);
      user.put("fullname", fullname);
      return user.toString();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return "{}";
  }

  public String toString() {
    return String.format("<ReadmillUser[%d]: %s>", readmillId, username);
  }

}
