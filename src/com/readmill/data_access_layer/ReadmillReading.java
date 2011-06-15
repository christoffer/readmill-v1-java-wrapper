package com.readmill.data_access_layer;

import org.json.JSONException;
import org.json.JSONObject;

public class ReadmillReading extends ReadmillObject {
  public long estimatedTimeLeft = 0;
  public double progress = 0.0;
  
  public static interface State {
    int INTERESTING = 1;
    int OPEN = 2;
    int FINISHED = 3;
    int ABANDONED = 4;
  }

  public ReadmillReading(JSONObject json) {
    super(json);
  }

  public ReadmillReading(String json) throws JSONException {
    super(json);
  }

  @Override
  public void fromJSON(JSONObject json) {
    estimatedTimeLeft = json.optLong("estimated_time_left", 0);
    progress = json.optDouble("progress");
    id = json.optLong("id", -1);
  }

  @Override
  public String toJSON() {
    try {
      JSONObject book = new JSONObject();
      book.put("id", id);
      book.put("estimated_time_left", estimatedTimeLeft);
      book.put("progress", progress);
      return book.toString();
    } catch (JSONException e) {
      e.printStackTrace();
      return "{}";
    }
  }

}
