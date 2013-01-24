package com.lin.Factory;

import org.json.JSONObject;

public interface CommandDelegate {
	public void OnCommandComplete(JSONObject json);
	public void OnCommandError(String errMsg);
}
