package com.readmill.dal;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Lovisa
 *
 */

public class ReadmillBookAsset extends ReadmillEntity{

	private String vendor;
	private String uri;
	private int acquisitionType; //TODO CHANGE!!

	/**
	 * Constructor one
	 */
	public ReadmillBookAsset() {
		super();
	}

	/**
	 * Constructor two
	 */
	public ReadmillBookAsset(JSONObject json) {
		super(json);
	}
	  
	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public int getAcquisitionType() {
		return acquisitionType;
	}

	public void setAcquisitionType(int acquisition_type) {
		this.acquisitionType = acquisition_type;
	}

	/**
	 * convertFromJSON
	 */
	@Override
	protected void convertFromJSON(JSONObject json) {
		vendor = json.optString("vendor");
		uri = json.optString("uri");
		acquisitionType = json.optInt("acquisition_type");	
	}

	/**
	 * convertToJSON
	 */
	@Override
	protected JSONObject convertToJSON() throws JSONException {
		JSONObject json = new JSONObject();
	    
		json.put("vendor", vendor);
	    json.put("uri", uri);
	    json.put("acquisition_type", acquisitionType);
	    
	    return json;
	}
}
