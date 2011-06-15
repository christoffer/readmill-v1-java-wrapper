package com.readmill.data_access_layer;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ReadmillObject {
  public final String TAG = this.getClass().getName();
  public long id;

  public ReadmillObject() {}

  public ReadmillObject(JSONObject json) {
    fromJSON(json);
  }

  public ReadmillObject(String json) throws JSONException {
    fromJSON(json);
  }

  public void fromJSON(String jsonString) throws JSONException {
    fromJSON(new JSONObject(jsonString));
  }

  abstract public void fromJSON(JSONObject json);

  abstract public String toJSON();
}
