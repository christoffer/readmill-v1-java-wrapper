package com.readmill.dal;

import com.readmill.api.Endpoints;
import com.readmill.api.Params;
import com.readmill.api.Request;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Christoffer
 */
public class ReadmillReading extends ReadmillEntity {

	private long estimatedTimeLeft;
	private double progress;
	private boolean isPrivate;
	private int state; // = 1; //eller 0? if state has a default value, test goes
						// wrong
	private boolean recommended;
	private String closingRemark;

	private Date abandonedAt;
	private Date finishedAt;
	private Date createdAt;
	private Date touchedAt;
	private Date startedAt;

	private long duration;
	private double averagePeriodTime;

	private String locations;
	private String permalinkUrl;
	private String uri;

  // Resource Urls
	private String periodsResourceUrl;
	private String highlightResourceUrl;

	private ReadmillUser user;
	private ReadmillBook book;

	// TODO permalink???


	public static interface State {
		int INTERESTING = 1;
		int READING = 2;
		int FINISHED = 3;
		int ABANDONED = 4;
	}

	public static interface Privacy {
		int PUBLIC = 0;
		int PRIVATE = 1;
	}

  public ReadmillReading(JSONObject json) {
    super(json);
  }

  /**
   * convertFromJSON
   */
  @Override
  public void convertFromJSON(JSONObject json) {
    id = json.optLong("id", -1);
    estimatedTimeLeft = json.optLong("estimated_time_left");
    progress = json.optDouble("progress", 0.0);
    isPrivate = json.optBoolean("private", false);
    state = json.optInt("state", State.READING);
    recommended = json.optBoolean("recommended", false);
    closingRemark = getString(json, "closing_remark");
    abandonedAt = parseUTC(getString(json, "abandoned_at"));
    finishedAt = parseUTC(getString(json, "finished_at"));
    createdAt = parseUTC(getString(json, "created_at"));
    touchedAt = parseUTC(getString(json, "touched_at"));
    startedAt = parseUTC(getString(json, "started_at"));
    duration = json.optLong("duration");
    locations = getString(json, "locations");
    permalinkUrl = getString(json, "permalink_url");
    uri = getString(json, "uri");
    periodsResourceUrl = getString(json, "periods");
    highlightResourceUrl = getString(json, "highlights");
    averagePeriodTime = json.optDouble("average_period_time", 0.0);

    JSONObject jsonUser = json.optJSONObject("user");
    user = new ReadmillUser(jsonUser);

    JSONObject jsonBook = json.optJSONObject("book");
    book = new ReadmillBook(jsonBook);
  }

  @Override
  public JSONObject convertToJSON() {
    JSONObject json = new JSONObject();
    try {
      json.put("id", id);
      json.put("estimated_time_left", estimatedTimeLeft);
      json.put("progress", progress);
      json.put("closing_remark", closingRemark);
      json.put("abandoned_at", toISO8601(abandonedAt));
      json.put("finished_at", toISO8601(finishedAt));
      json.put("created_at", toISO8601(createdAt));
      json.put("touched_at", toISO8601(touchedAt));
      json.put("started_at", toISO8601(startedAt));
      json.put("estimated_time_left", estimatedTimeLeft);
      json.put("duration", duration);
      json.put("locations", locations);
      json.put("permalink_url", permalinkUrl);
      json.put("progress", progress);
      json.put("uri", uri);
      json.put("periods", periodsResourceUrl);
      json.put("recommended", recommended);
      json.put("private", isPrivate);
      json.put("state", state);
      json.put("highlights", highlightResourceUrl);
      json.put("average_period_time", averagePeriodTime);
    } catch (JSONException ignored) {
    }
    return json;
  }

  // DAL Methods

  /**
   * Retrieve a Readmill Reading by resource URI
   *
   * @param location resource URI
   * @return Readmill Reading
   * @throws ReadmillException When fetching the reading failed
   */
  public static ReadmillReading get(String location) throws ReadmillException {
    JSONObject jsonReading = getAssertedJSON(Request.to(toResourceURI(location)));
    return new ReadmillReading(jsonReading);
  }

  /**
   * Retrieve a Readmill Reading by ID
   *
   * @param id resource ID
   * @return Readmill Reading
   * @throws ReadmillException When fetching the reading failed
   */
  public static ReadmillReading get(long id) throws ReadmillException {
    return get(String.format(Endpoints.READINGS, id));
  }

  /**
   * Return all readings for a given user
   *
   * @param userId Id of user to fetch all readings for
   * @return A list of all readings for the given user (an empty list is return if the user could not be found)
   */
  public static ArrayList<ReadmillReading> getAllForUser(long userId) throws ReadmillException {
    ArrayList<ReadmillReading> readingsForUser = new ArrayList<ReadmillReading>();
    String uri = String.format(Endpoints.USER_READINGS, userId);
    Request request = Request.to(toResourceURI(uri));
    request.with("count", "100");
    request.with("states", "reading,finished,abandoned");
    System.out.println(request.toUrl());
    JSONArray jsonResult = getAssertedJSONArray(request);

    for(int i = 0; i < jsonResult.length(); i++) {
      try {
        ReadmillReading reading = new ReadmillReading((JSONObject) jsonResult.get(i));
        readingsForUser.add(reading);
      } catch(JSONException ignored) {
        throw new ReadmillException("Failed to parse JSON object: " + ignored.getMessage());
      }
    }
    return readingsForUser;
  }

  /**
   * Create a new reading for the provided book
   *
   * @param bookId  Id of the Readmill book to create a reading for
   * @param state   Reading state of the reading to create (ReadmillReading.State)
   * @param privacy Privacy setting for the reading (ReadmillReading.Privacy)
   * @return the created ReadmillReading
   * @throws ReadmillException When the reading could not be created, or retrieving the created reading failed
   */
  public static ReadmillReading create(long bookId, int state, int privacy) throws ReadmillException {
    Request request = Request.to(Endpoints.BOOK_READINGS, bookId);

    request.add(Params.Reading.IS_PRIVATE, privacy);
    request.add(Params.Reading.STATE, state);

    try {
      HttpResponse response = ReadmillDAL.getWrapper().post(request);

      int statusCode = response.getStatusLine().getStatusCode();

      if(statusCode == HttpStatus.SC_CREATED || statusCode == HttpStatus.SC_CONFLICT) {
        String location = response.getFirstHeader("Location").getValue();
        return get(location);
      } else {
        throw new ReadmillException(response.getStatusLine().getStatusCode());
      }
    } catch(IOException e) {
      e.printStackTrace();
      throw new ReadmillException();
    }
  }

  /**
   * Update the state of a Reading on the server
   *
   * @param reading Reading to update on Readmill
   * @throws ReadmillException When the operation failed
   */
  public static void update(ReadmillReading reading) throws ReadmillException {
    Request request = Request.to(Endpoints.READINGS, reading.id);

    request.add(Params.Reading.STATE, reading.getState());
    request.add(Params.Reading.IS_PRIVATE, reading.isPrivate() ? 1 : 0);

    try {
      HttpResponse response = ReadmillDAL.getWrapper().put(request);
      assertResponseCode(HttpStatus.SC_OK, response);
    } catch(IOException e) {
      e.printStackTrace();
    }
  }
  public void update() throws ReadmillException {
    update(this);
  }

  // Relations
  public ArrayList<ReadmillReadingPeriod> getPeriods() throws ReadmillException {
    return null;
  }

  public ArrayList<ReadmillHighlight> getHighlights() throws ReadmillException {
    return ReadmillHighlight.getAllForReadingId(id);
  }

  // Getters and setters

	public long getEstimatedTimeLeft() {
		return estimatedTimeLeft;
	}

	public void setEstimatedTimeLeft(long estimatedTimeLeft) {
		this.estimatedTimeLeft = estimatedTimeLeft;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isRecommended() {
		return recommended;
	}

	public void setRecommended(boolean recommended) {
		this.recommended = recommended;
	}

	public String getClosingRemark() {
		return closingRemark;
	}

	public void setClosingRemark(String closingRemark) {
		this.closingRemark = closingRemark;
	}

	public Date getAbandonedAt() {
		return abandonedAt;
	}

	public void setAbandonedAt(Date abandonedAt) {
		this.abandonedAt = abandonedAt;
	}

	public Date getFinishedAt() {
		return finishedAt;
	}

	public void setFinishedAt(Date finishedAt) {
		this.finishedAt = finishedAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getTouchedAt() {
		return touchedAt;
	}

	public void setTouchedAt(Date touchedAt) {
		this.touchedAt = touchedAt;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getLocations() {
		return locations;
	}

	public void setLocations(String locations) {
		this.locations = locations;
	}

	public String getPermalinkUrl() {
		return permalinkUrl;
	}

	public void setPermalinkUrl(String permalinkUrl) {
		this.permalinkUrl = permalinkUrl;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getPeriodsResourceUrl() {
		return periodsResourceUrl;
	}

	public void setPeriodsResourceUrl(String periodsResourceUrl) {
		this.periodsResourceUrl = periodsResourceUrl;
	}

	public String getHighlightResourceUrl() {
		return highlightResourceUrl;
	}

	public void setHighlightResourceUrl(String highlightResourceUrl) {
		this.highlightResourceUrl = highlightResourceUrl;
	}

	public double getAveragePeriodTime() {
		return averagePeriodTime;
	}

	public void setAveragePeriodTime(double averagePeriodTime) {
		this.averagePeriodTime = averagePeriodTime;
	}

	public ReadmillUser getUser() {
		return user;
	}

	public void setUser(ReadmillUser user) {
		this.user = user;
	}

	public ReadmillBook getBook() {
		return book;
	}

	public void setBook(ReadmillBook book) {
		this.book = book;
	}


}
