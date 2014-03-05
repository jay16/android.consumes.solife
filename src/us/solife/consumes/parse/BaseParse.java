package us.solife.consumes.parse;

import org.json.JSONException;

public abstract class BaseParse<T> {
	public abstract T parseJSON(String jsonStr) throws JSONException;
}
