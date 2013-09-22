package com.eastapps.meme_gen_android.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.json.JSONObject;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.util.StringUtils;
import com.eastapps.util.Conca;
import com.google.gson.Gson;

public class WebClient implements IWebClient {
	private static final String TAG = WebClient.class.getSimpleName();
	
	private static final int DEFAULT_CONN_TIMEOUT_MS = 30 * 1000;
	private static final boolean DEFAULT_CONN_USE_CACHES = true;
	
	private int connectionTimeoutMs;
	private boolean connectionUseCaches;
	
	public WebClient() {
		super();
		this.connectionTimeoutMs = DEFAULT_CONN_TIMEOUT_MS;
		this.connectionUseCaches = DEFAULT_CONN_USE_CACHES;
	}
	
	@Override
	public String getJSONObject(final String addr) {
		String result = Constants.EMPTY_STRING;
		
		try {
			final URLConnection conn = createURLConnection(addr);
			
			result = getResponseText(conn);
			
		} catch (Exception e) {
			Log.e(
				TAG,
				Conca.t(
					"an error occurred while attempting to get a JSON object from [", 
					addr, 
					"]"
				), 
				e
			);
		}
		
		return result;
	}

	private String getResponseText(final URLConnection conn) {
		final StringBuilder sb = new StringBuilder();
		BufferedReader bufReader = null;
		try {
			bufReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			
			while ((line = bufReader.readLine()) != null) {
				sb.append(line);
			}
			
		} catch (Exception e) {
			Log.e(TAG, "err", e);
			
		} finally {
			if (bufReader != null) {
				try {
					bufReader.close();
					
				} catch (Exception e) { }
			}
		}
		
		return sb.toString();
	}

	@Override
	public Bitmap getBitmap(final String addr) {
		Bitmap bitmap = null;
		
		URLConnection conn = null;
		try {
			conn = createURLConnection(addr);
			bitmap = BitmapFactory.decodeStream(conn.getInputStream());
			
		} catch (Exception e) {
			Log.e(
				TAG,
				Conca.t(	
					"an error occurred while attempting to get a bitmap from [",
					addr,
					"]"
				),
				e
			);
		} 
		
		
		return bitmap;
	}
	
	@Override
	public String getJSONObject(final String addr, final String jsonRequest) {
		String responseStr = Constants.EMPTY_STRING;
		
		try {
			responseStr = postJson(addr, jsonRequest);
			if (!StringUtils.isNotBlank(responseStr)) {
				Log.w(TAG, Conca.t(
					"response empty:request=[", jsonRequest.toString(), 
					"],addr=[", addr, "]"
				));
			}
			
		} catch (Exception e) {
			Log.e(TAG, "error occurred while attempting to get JSON obj", e);
			
		} 		
		
		return responseStr;
	}

	private String postJson(final String addr, final String jsonRequest)
			throws UnsupportedEncodingException, IOException,
			ClientProtocolException {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(addr);
		StringEntity strEnt = new StringEntity(jsonRequest);
		
		httpPost.setEntity(strEnt);
		
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
		
		ResponseHandler<String> respHandler = new BasicResponseHandler();
		
		final String respStr = client.execute(httpPost, respHandler);
		return respStr;
	}
	
	private URLConnection createURLConnection(final String addr) throws IOException {
		final URL url = new URL(addr);
		final URLConnection connection = url.openConnection();
		connection.setUseCaches(connectionUseCaches);	
		connection.setConnectTimeout(connectionTimeoutMs);
		
		return connection;
	}

	@Override
	public void setConnectionTimeoutMs(int connectionTimeoutMs) {
		this.connectionTimeoutMs = connectionTimeoutMs;
	}
	
	@Override
	public void setConnectionUseCaches(final boolean useCaches) {
		this.connectionUseCaches = useCaches;
	}
	
	private String sendRequest(final String addr, final Object requestObj) {
		if (StringUtils.isBlank(addr) || requestObj == null) {
			Log.e(TAG, "problem sending request");
			return null;
		}
		
		final String requestObjJson = requestObj instanceof String ? requestObj.toString() : new Gson().toJson(requestObj);
		
		final String jsonResponse = getJSONObject(addr, requestObjJson);
		
		return jsonResponse;
	}

	@Override
	public <T> T sendRequestAsJson(final String addr, final Object requestObj, final Class<T> returnType) {
		final String jsonResponse = sendRequest(addr, requestObj);
		
		final T responseObj = new Gson().fromJson(jsonResponse, returnType);
		
		return responseObj;
	}

	@Override
	public List<?> sendRequestAsJsonReturnList(final String addr, final Object requestObj, final Type type) 
	{
		final String jsonResponse = sendRequest(addr, requestObj);
		
		final List<?> responseObj = new Gson().fromJson(jsonResponse, type);
		
		return responseObj;
	}

}
