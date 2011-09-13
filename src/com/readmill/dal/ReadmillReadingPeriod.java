package com.readmill.dal;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class ReadmillReadingPeriod extends ReadmillEntity  {

  private long readingId;
  private long userId;
  private Date startedAt;
  private double progress;
  private long duration;

  // TODO locations

  public ReadmillReadingPeriod() { super(); }
  public ReadmillReadingPeriod(JSONObject json) { super(json); }

  @Override protected void convertFromJSON(JSONObject json) {
    id = json.optLong("id", -1);
    userId = json.optLong("user_id", -1);
    readingId = json.optLong("reading_id", -1);
    startedAt = parseUTC(getString(json, "started_at"));
    progress = json.optDouble("progress", 0.0);
    duration = json.optLong("duration", 0);
  }

  @Override protected JSONObject convertToJSON() throws JSONException {
    JSONObject json = new JSONObject();

    json.put("id", id);
    json.put("reading_id", readingId);
    json.put("user_id", userId);
    json.put("started_at", toUTC(startedAt));
    json.put("progress", progress);
    json.put("duration", duration);

    return json;
  }



  // Access
  // ======



  public long getReadingId() {
    return readingId;
  }

  public void setReadingId(long readingId) {
    this.readingId = readingId;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public Date getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(Date startedAt) {
    this.startedAt = startedAt;
  }

  public double getProgress() {
    return progress;
  }

  public void setProgress(double progress) {
    this.progress = progress;
  }
  public long getDuration() {
    return duration;
  }
  public void setDuration(long duration) {
    this.duration = duration;
  }

}
