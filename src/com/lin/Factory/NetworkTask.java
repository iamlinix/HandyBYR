package com.lin.Factory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import it.sauronsoftware.base64.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class NetworkTask extends AsyncTask<String, Void, JSONObject> {

	private AndroidHttpClient _client;
	private String _command;
	private String _caller;
	
	private String BasicAuthHeader(String user, String pass) {
		String header = "Basic ";
		header += Base64.encode(user + ":" + pass, "ASCII");
		return header;
	}
	
	public NetworkTask()
	{
		_client = AndroidHttpClient.newInstance("Android");
	}
	
	//for GET methods, have and only have 6 params
	//for POST methods, all params after 6th are valid
	//POST param should be like param[i] = name, param[i+1] = value
	//the param count has to be even
	@Override
	protected JSONObject doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		//the params for GET must have 5 entries: {uri, user, pass, method, command, caller's class name}
		if(params[3] == Command.GET && params.length != 6)
			return null;
		
		String method = params[3];
		_command = params[4];
		_caller = params[5];
		
		String uri = params[0];
		HttpRequestBase httpMethod = method == Command.GET ? new HttpGet(uri) :
			new HttpPost(uri);
		httpMethod.addHeader("Authorization", BasicAuthHeader(params[1], params[2]));
		
		//this is a POST request, write the params
		if(method == Command.POST) {
			try {
				ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
				for(int i = 6; i < params.length; i ++) {
					pairs.add(new BasicNameValuePair(params[i], params[++i]));
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, "utf-8");
				HttpPost hp = (HttpPost)httpMethod;
				hp.setEntity(entity);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			HttpResponse res = _client.execute(httpMethod);
			return new JSONObject(new String(
					EntityUtils.toByteArray(res.getEntity())));
		}
		catch(Exception e) {
			Log.v("LIN", e.getMessage());
		//	_client.close();
			JSONObject json = new JSONObject();
			try {
				json.put(Command.COMMAND_ERROR, e.getMessage());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return json;
		}
		
	}
	 
	@Override
	protected void onPostExecute(JSONObject json) {
        //Do something with result
		_client.close();
		if(json.has(Command.COMMAND_ERROR)) {
			try {
				Command.onRequestErrorStatic(json.getString(Command.COMMAND_ERROR), _caller);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return;
		}
			
		
        try {
			json.put(CommandName.CommandIndicator, _command);
			json.put(CommandName.ClassNameOfCaller, _caller);
			Command.onRequestReturnStatic(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        	
    }

}
