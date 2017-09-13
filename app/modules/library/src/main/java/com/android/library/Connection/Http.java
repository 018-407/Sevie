package com.android.library.Connection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Http {
	public static String get(String url, String params, int timeout) {
		return getResponse(url, params, timeout, "GET");
	}

	public static String post(String url, String params, int timeout) {
		return getResponse(url, params, timeout, "POST");
	}

	public static String getResponse(String url, String params, int timeout, String method) {
		StringBuilder response = new StringBuilder();
		StringBuilder message = new StringBuilder();
		StringBuilder exception = new StringBuilder();
		try {
			if(!method.equals("POST")) {
				url += params;
			}
			URL address = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) address.openConnection();
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);
			connection.setRequestMethod(method);
			connection.setRequestProperty("Content-Type", "application/" + (method.equals("POST") ? "x-www-form-urlencoded" : "json"));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setRequestProperty("connection", "close");
			connection.setRequestProperty("Accept-Encoding", "");
			connection.setDoInput(true);
			connection.setDoOutput(method.equals("POST"));
			connection.setUseCaches(false);
			if(method.equals("POST")) {
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
				writer.write(params);
				writer.flush();
				writer.close();
			}
			connection.connect();
			if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
				if(!address.getHost().equals(connection.getURL().getHost())) {
					message.append("There was a problem in your internet connection. Please check and try again.");
				}
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				while((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();
				return response.toString();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			message.append(e.getMessage());
			exception.append(e.toString());
		}
		JSONObject error = new JSONObject();
		try {
			JSONObject field = new JSONObject();
			field.put("message", message.toString());
			field.put("exception", exception.toString());
			error.put("error", field);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		response.append(error.toString());
		return response.toString();
	}

	public static String jasonObjectToURL(JSONObject params) {
		StringBuilder url = new StringBuilder();
		Iterator<String> keys = params.keys();
		while(keys.hasNext()) {
			try {
				String key = keys.next();
				String value = params.getString(key);
				url.append((url.length() == 0 ? "?" : "&") + key + "=" + URLEncoder.encode(value, "UTF-8"));
			}
			catch(JSONException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return url.toString();
	}
}