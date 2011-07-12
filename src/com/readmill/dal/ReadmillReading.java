package com.readmill.dal;

import org.json.JSONException;
import org.json.JSONObject;

public class ReadmillReading extends ReadmillEntity {

  public long estimatedTimeLeft = 0;
  public double progress = 0.0;
  public boolean isPrivate = false;
  public int state = 1;

  public static interface State {
    int INTERESTING = 1;
    int OPEN = 2;
    int FINISHED = 3;
    int ABANDONED = 4;
  }

  public static interface Privacy {
    int PUBLIC = 0;
    int PRIVATE = 1;
  }

  public ReadmillReading() {
    super();
  }

  public ReadmillReading(JSONObject json) {
    super(json);
  }

  @Override public void convertFromJSON(JSONObject json) {
    id = json.optLong("id", -1);
    estimatedTimeLeft = json.optLong("estimated_time_left", 0);
    progress = json.optDouble("progress");
    state = json.optInt("state");
    isPrivate = json.optInt("is_private") == 1;
  }

  @Override public JSONObject convertToJSON() {
    JSONObject json = new JSONObject();
    try {
      json.put("id", id);
      json.put("estimated_time_left", estimatedTimeLeft);
      json.put("progress", progress);
      json.put("state", state);
      json.put("is_private", isPrivate ? 1 : 0);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return json;
  }

  public int getState() {
    return state;
  }

}
