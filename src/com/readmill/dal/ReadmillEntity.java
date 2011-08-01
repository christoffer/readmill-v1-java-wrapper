package com.readmill.dal;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Abstract Readmill Entity. Requires implementations to implement fromJSON and toJSON for
 * serializing and deserializing to JSON format.
 * 
 * @author christoffer
 * 
 */
public abstract class ReadmillEntity {
  public final String TAG = this.getClass().getName();
  public long id = -1;

  public ReadmillEntity(JSONObject json) {
    convertFromJSON(json);
  }

  public ReadmillEntity() {}

  /**
   * @return True if this entity has a set ID, false otherwise
   */
  public boolean isNew() {
    return id == -1;
  }

  /**
   * @return String value of this object in JSON syntax
 * @throws DatatypeConfigurationException 
   */
  public String toJSON() throws DatatypeConfigurationException{
    try {
      return convertToJSON().toString();
    } catch (JSONException e) {
      e.printStackTrace();
      return "{}";
    }
  }

  /**
   * Parse a JSON string and apply the values to this object
   * 
   * @param json JSON string to pull values from
   */
	public void fromJSON(String json) {
		try {
			convertFromJSON(new JSONObject(json));
		} catch (JSONException e) {
			throw new RuntimeException("Malformed JSON: "
					+ (json.length() > 80 ? json.substring(0, 80) + "..."
							: json));
		}
	}

	public Date fromUTC(String date) {
		// Customized parse method
		Calendar cal = DatatypeConverter.parseDateTime(date);
		return new Date(cal.getTimeInMillis());
	}
	
	public String toUTC(Date date) throws DatatypeConfigurationException {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		
		XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance()
				.newXMLGregorianCalendar(gc);
		
		String utcTime = xmlCal.normalize().toXMLFormat(); //does everything right except the .000
		
		Pattern p = Pattern.compile("\\.{1}\\d{3}"); //chop off any millisecond, or do we want to round off?
		Matcher m = p.matcher(utcTime);
		utcTime = m.replaceAll("");

		return utcTime;
	}
		
  /**
   * Extensions must implement a way to initialize the DAL object from a JSON object
   * 
   * @param json JSON to extract values from
   */
  abstract protected void convertFromJSON(JSONObject json);

  /**
   * Extensions must implement a way to create a JSON string from a DAL object
 * @throws DatatypeConfigurationException 
   */
  abstract protected JSONObject convertToJSON() throws JSONException, DatatypeConfigurationException;
}
