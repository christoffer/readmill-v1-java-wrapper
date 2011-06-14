package com.readmill.objectmodel;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ReadmillBaseObject {
  public final String TAG = this.getClass().getName();
  public long id;

  public ReadmillBaseObject() {}

  public ReadmillBaseObject(JSONObject json) {
    fromJSON(json);
  }

  public ReadmillBaseObject(String json) throws JSONException {
    fromJSON(json);
  }

  public void fromJSON(String jsonString) throws JSONException {
    fromJSON(new JSONObject(jsonString));
  }

  abstract public void fromJSON(JSONObject json);

  abstract public String toJSON();
}
