package com.readmill.dal;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Christoffer & Lovisa
 */
public class ReadmillBook extends ReadmillEntity {

  public String title;
  public String author;
  public String coverURL;
  public String isbn;
  public String story;
  public String publishedAT; //or date?
  public String language; 
  public String permalink;
  public String permalinkURL;
  public String uri;
  public ArrayList<ReadmillBookAsset> assets;
  
  
  public ReadmillBook() {
	 super();
	 assets = new ArrayList<ReadmillBookAsset>();
  }

  public ReadmillBook(JSONObject json) {
	  super(json);
  }
  
  @Override 
  public void convertFromJSON(JSONObject json){
	//must be here, because super(json) use this method
	assets = new ArrayList<ReadmillBookAsset>();
    
	id = json.optLong("id", -1);

    title = json.optString("title");
    author = json.optString("author");
    coverURL = json.optString("cover_url");
    isbn = json.optString("isbn");
    story = json.optString("story");
    publishedAT = json.optString("published_at");
    language = json.optString("language");
    permalink = json.optString("permalink");
    permalinkURL = json.optString("permalink_url");
    uri = json.optString("uri");
    
    if(json.optJSONArray("assets") == null){
    	assets.add(new ReadmillBookAsset());
    }else{
    	for(int i = 0; i < json.optJSONArray("assets").length(); i++){
    		assets.add(new ReadmillBookAsset(json.optJSONArray("assets").optJSONObject(i)));
    	}
    }  
  }

  @Override 
  public JSONObject convertToJSON() throws JSONException {
    JSONObject json = new JSONObject();
    JSONArray array = new JSONArray();
    
    json.put("id", id);
    json.put("title", title);
    json.put("author", author);
    json.put("cover_url", coverURL);
    json.put("isbn", isbn);
    json.put("story", story);
    json.put("published_at", publishedAT);
    json.put("language", language);
    json.put("permalink", permalink);
    json.put("permalink_url", permalinkURL);
    json.put("uri", uri);
 
    for(int i = 0; i < assets.size(); i++){
    	array.put(i, assets.get(i).convertToJSON());
        json.put("assets", array);
    }
    return json;
  }
}

