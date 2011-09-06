package com.readmill.dal;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Christoffer
 *
 */
public class ReadmillPing extends ReadmillEntity {

  private long readingId;
  private String identifier;
  private long progress; // double or long here??
  private long duration;
  private Date occurredAt;

  public ReadmillPing() {
    super();
  }

  public ReadmillPing(JSONObject json) {
    super(json);
  }

  public long getReadingId() {
    return readingId;
  }

  public void setReadingId(long readingId) {
    this.readingId = readingId;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public long getProgress() {
    return progress;
  }

  public void setProgress(long progress) {
    this.progress = progress;
  }

  public long getDuration() {
    return duration;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }

  public Date getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(Date occurredAt) {
    this.occurredAt = occurredAt;
  }

  /**
   * convertFromJSON
   *
   * @param json JSONObject to convert from
   */
  @Override public void convertFromJSON(JSONObject json) {
    readingId = json.optLong("reading_id", 0);
    identifier = getString(json, "identifier");
    progress = json.optLong("progress", 0);
    duration = json.optLong("duration", 0);
    occurredAt = parseUTC(getString(json, "occurred_at"));
  }

  /**
   * convertToJSON
   */
  @Override public JSONObject convertToJSON() {
    JSONObject json = new JSONObject();
    try {
      json.put("reading_id", readingId);
      json.put("identifier", identifier);
      json.put("progress", progress);
      json.put("duration", duration);
      json.put("occurred_at", this.toUTC(occurredAt));
    } catch (JSONException ignored) {
    }
    return json;
  }

}
