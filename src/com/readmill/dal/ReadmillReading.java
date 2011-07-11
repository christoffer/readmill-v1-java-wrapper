package com.readmill.dal;

import org.json.JSONException;
import org.json.JSONObject;

public class ReadmillReading extends ReadmillEntity {

  public long estimatedTimeLeft = 0;
  public double progress = 0.0;

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
  }

  @Override public JSONObject convertToJSON() {
    JSONObject json = new JSONObject();
    try {
      json.put("id", id);
      json.put("estimated_time_left", estimatedTimeLeft);
      json.put("progress", progress);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return json;
  }

}
