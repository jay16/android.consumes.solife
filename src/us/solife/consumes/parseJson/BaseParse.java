package us.solife.consumes.parseJson;

import org.json.JSONException;

public abstract class BaseParse<T> {
	public abstract T parseJSON(String jsonStr) throws JSONException;
}
