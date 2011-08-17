package com.readmill.dal;

import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Christoffer
 */
public class ReadmillReading extends ReadmillEntity {

	private long estimatedTimeLeft = 0;
	private double progress = 0.0;
	private boolean isPrivate = false;
	private int state; // = 1; //eller 0? if state has a default value, test goes
						// wrong
	private boolean recommended;
	private String closingRemark;

	private Date abandonedAt;
	private Date finishedAt;
	private Date createdAt;
	private Date touchedAt;
	private Date startedAt;

	private String duration;
	private String locations;
	private String permalinkUrl;
	private String uri;
	private String periods;
	private String highlights;
	private String averagePeriodTime;

	private ReadmillUser user;
	private ReadmillBook book;

	// TODO permalink???

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

	/**
	 * constructor one
	 */
	public ReadmillReading() {
		super();
	}
 
	/**
	 * constructor two
	 * @param json
	 */
	public ReadmillReading(JSONObject json) {
		super(json);
	}

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

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
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

	public String getPeriods() {
		return periods;
	}

	public void setPeriods(String periods) {
		this.periods = periods;
	}

	public String getHighlights() {
		return highlights;
	}

	public void setHighlights(String highlights) {
		this.highlights = highlights;
	}

	public String getAveragePeriodTime() {
		return averagePeriodTime;
	}

	public void setAveragePeriodTime(String averagePeriodTime) {
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

	/**
	 * convertFromJSON
	 */
	@Override
	public void convertFromJSON(JSONObject json) {
		id = json.optLong("id", -1);
		estimatedTimeLeft = json.optLong("estimated_time_left", 0);
		progress = json.optDouble("progress");
		isPrivate = json.optBoolean("private");
		state = json.optInt("state");
		recommended = json.optBoolean("recommended");
		closingRemark = json.optString("closing_remark", "");
		abandonedAt = parseUTC(json.optString("abandoned_at", ""));
		finishedAt = parseUTC(json.optString("finished_at", ""));
		createdAt = parseUTC(json.optString("created_at", ""));
		touchedAt = parseUTC(json.optString("touched_at", ""));
		startedAt = parseUTC(json.optString("started_at", ""));
		duration = json.optString("duration");
		locations = json.optString("locations");
		permalinkUrl = json.optString("permalink_url");
		uri = json.optString("uri");
		periods = json.optString("periods");
		highlights = json.optString("highlights");
		averagePeriodTime = json.optString("average_period_time");

    JSONObject jsonUser = json.optJSONObject("user");
		user = jsonUser == null ? new ReadmillUser() : new ReadmillUser(jsonUser);

    JSONObject jsonBook = json.optJSONObject("book");
		book = jsonBook == null ? new ReadmillBook() : new ReadmillBook(jsonBook);

	}

	/**
	 * convertToJSON
	 * 
	 * @throws DatatypeConfigurationException
	 */
	@Override
	public JSONObject convertToJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("id", id);
			json.put("estimated_time_left", estimatedTimeLeft);
			json.put("progress", progress);
			json.put("closing_remark", closingRemark);
			json.put("abandoned_at", toUTC(abandonedAt));
			json.put("finished_at", toUTC(finishedAt));
			json.put("created_at", toUTC(createdAt));
			json.put("touched_at", toUTC(touchedAt));
			json.put("started_at", toUTC(startedAt));
			json.put("estimated_time_left", estimatedTimeLeft);
			json.put("duration", duration);
			json.put("locations", locations);
			json.put("permalink_url", permalinkUrl);
			json.put("progress", progress);
			json.put("uri", uri);
			json.put("periods", periods);
			json.put("recommended", recommended);
			json.put("private", isPrivate);
			json.put("state", state);
			json.put("highlights", highlights);
			json.put("average_period_time", averagePeriodTime);

			json.put("user", user.convertToJSON());
			json.put("book", book.convertToJSON());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

}
