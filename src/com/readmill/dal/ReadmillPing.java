package com.readmill.dal;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class ReadmillPing extends ReadmillEntity {

  public ReadmillPing() { super(); }
  public ReadmillPing(JSONObject json) {
    super(json);
  }

  public long readingId;
  public String identifier;
  public double progress;
  public long duration;
  public Date occuredAt;

  @Override
  public void convertFromJSON(JSONObject json) {
    readingId = json.optLong("reading_id", 0);
    identifier = json.optString("identifier", "");
    progress = json.optLong("progress", 0);
    duration = json.optLong("duration", 0);
    occuredAt = new Date(json.optLong("occurred_at", 0));
  }

  @Override
  public JSONObject convertToJSON() {
    JSONObject json = new JSONObject();
    try {
      json.put("reading_id", readingId);
      json.put("identifier", identifier);
      json.put("progress", progress);
      json.put("duration", duration);
      json.put("occurred_at", occuredAt.getTime());
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return json;
  }

}
