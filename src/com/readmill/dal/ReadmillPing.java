package com.readmill.dal;

import com.readmill.api.Endpoints;
import com.readmill.api.Params;
import com.readmill.api.Request;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

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
      json.put("occurred_at", toISO8601(occurredAt));
    } catch (JSONException ignored) {
    }
    return json;
  }

  // DAL methods

  /**
   * Post a ping to Readmill
   *
   * @param identifier      Unique reading session identifier
   * @param readingId       Reading to ping
   * @param progress        Progress when this ping was sent [0, 1.0]
   * @param durationSeconds How many seconds of effective reading time that has passed
   *                        since the last ping
   * @param occurredAt      When the ping occurred
   * @throws ReadmillException When the operation failed
   */
  public static void post(String identifier, long readingId, double progress, long durationSeconds, Date occurredAt) throws ReadmillException {
    Request request = Request.to(Endpoints.PINGS, readingId);

    request.add(Params.Ping.IDENTIFIER, identifier);
    request.add(Params.Ping.PROGRESS, progress);
    request.add(Params.Ping.DURATION, durationSeconds);
    request.add(Params.Ping.OCCURRED_AT, toISO8601(occurredAt));

    try {
      HttpResponse response = ReadmillDAL.getWrapper().post(request);
      assertResponseCode(HttpStatus.SC_CREATED, response);
    } catch(IOException e) {
      e.printStackTrace();
      throw new ReadmillException();
    }
  }

  /**
   * Post the instance to Readmill
   */
  public void post() throws ReadmillException {
    post(getIdentifier(), getReadingId(), getProgress(), getDuration(), getOccurredAt());
  }

  // Getters and setters

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

}
