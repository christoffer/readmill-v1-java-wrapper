package com.readmill.dal;

import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;

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
	private long progress; //double or long here??
	private long duration;
	private Date occurredAt;
	 
	/**
	 * constructor one
	 */
	public ReadmillPing() { 
		super(); 
	}
  
	/**
	 * constructor two
	 * @param json
	 */
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
	 * @param json
	 */
	@Override
	public void convertFromJSON(JSONObject json) {
		readingId = json.optLong("reading_id", 0);
		identifier = json.optString("identifier", "");
		progress = json.optLong("progress", 0);
		duration = json.optLong("duration", 0);
		occurredAt = this.fromUTC(json.optString("occurred_at", ""));
	}
	
	/**
	 * convertToJSON
	 */
	@Override
	public JSONObject convertToJSON() throws DatatypeConfigurationException {
		JSONObject json = new JSONObject();
		try {
			json.put("reading_id", readingId);
		    json.put("identifier", identifier);
		    json.put("progress", progress);
		    json.put("duration", duration);
		    json.put("occurred_at", this.toUTC(occurredAt));
		} catch (JSONException e) {
		    e.printStackTrace();
		  }
		return json;
	}
	
}
