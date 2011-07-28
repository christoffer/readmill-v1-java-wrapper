import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Lovisa
 *
 */

public class ReadmillBookAsset extends ReadmillEntity{

	public String vendor;
	public String uri;
	public int acquisition_type;
	
	public ReadmillBookAsset() { 
		super(); 
	}

	public ReadmillBookAsset(JSONObject json) {
		super(json);
	}
	  
	@Override
	protected void convertFromJSON(JSONObject json) {
		vendor = json.optString("vendor");
		uri = json.optString("uri");
		acquisition_type = json.optInt("acquisition_type");	
	}

	@Override
	protected JSONObject convertToJSON() throws JSONException {
		JSONObject json = new JSONObject();
	    
		json.put("vendor", vendor);
	    json.put("uri", uri);
	    json.put("acquisition_type", acquisition_type);
	    
	    return json;
	}
}
