package com.readmill.dal;

import com.readmill.api.Endpoints;
import com.readmill.api.Request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class ReadmillReadingPeriod extends ReadmillEntity {

  private long readingId;
  private long userId;
  private Date startedAt;
  private double progress;
  private long duration;
  private String identifier;

  public ReadmillReadingPeriod(JSONObject json) { super(json); }

  @Override
  protected void convertFromJSON(JSONObject json) {
    id = json.optLong("id", -1);
    userId = json.optLong("user_id", -1);
    readingId = json.optLong("reading_id", -1);
    startedAt = parseUTC(getString(json, "started_at"));
    progress = json.optDouble("progress", 0.0);
    duration = json.optLong("duration", 0);
    identifier = getString(json, "identifier");
  }

  @Override
  protected JSONObject convertToJSON() throws JSONException {
    JSONObject json = new JSONObject();

    json.put("id", id);
    json.put("reading_id", readingId);
    json.put("user_id", userId);
    json.put("started_at", toISO8601(startedAt));
    json.put("progress", progress);
    json.put("duration", duration);
    json.put("identifier", identifier);

    return json;
  }

  // DAL methods

  /**
   * Fetch all reading periods for a given reading
   *
   * @param resourceURI URI of reading to fetch periods for
   * @return A list of all reading periods for that reading
   * @throws ReadmillException
   */
  public static ArrayList<ReadmillReadingPeriod> getAllForReading(String resourceURI) throws ReadmillException {
    ArrayList<ReadmillReadingPeriod> periods = new ArrayList<ReadmillReadingPeriod>();
    JSONArray jsonResult = getAssertedJSONArray(Request.to(toResourceURI(resourceURI)));

    for(int i = 0; i < jsonResult.length(); i++) {
      try {
        ReadmillReadingPeriod period = new ReadmillReadingPeriod((JSONObject) jsonResult.get(i));
        periods.add(period);
      } catch(JSONException ignored) {
        throw new ReadmillException();
      }
    }
    return periods;
  }

  /**
   * Retrieve a Reading Period list by Reading id
   *
   * @param readingId Reading to fetch periods for
   * @return list of Reading Periods
   * @throws ReadmillException When fetching failed
   */
  public static ArrayList<ReadmillReadingPeriod> getAllForReading(long readingId) throws ReadmillException {
    return getAllForReading(String.format(Endpoints.PERIODS, readingId));
  }

  // Relations

  /**
   * Return a list of all the Readings for the user
   * @return
   */
  public ArrayList<ReadmillReading> getReadings() {
    return null;
  }

  // Getters and setters

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

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }
}
