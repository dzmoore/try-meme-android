package com.eastapps.meme_gen_android.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.R.string;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.eastapps.meme_gen_android.json.JSONObject;
import com.eastapps.meme_gen_android.util.StringUtils;
import com.eastapps.util.Conca;

public class WebClient implements IWebClient {
	private static final String TAG = WebClient.class.getSimpleName();
	
	@Override
	public JSONObject getJSONObject(final String addr) {
		JSONObject jsonObj = new JSONObject();
		
		try {
			final URLConnection conn = createURLConnection(addr);
			
			final String result = getResponseText(conn);
			
			jsonObj = new JSONObject(result);
			
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
		
		return jsonObj;
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
		
		try {
			final URLConnection conn = createURLConnection(addr);
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
	
	public JSONObject getJsonObject(final String addr, final JSONObject request) {
		JSONObject respJsonObj = new JSONObject();
		
		BufferedWriter bw = null;
		HttpURLConnection conn = null;
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(addr);
			StringEntity strEnt = new StringEntity(request.toString());
			
			httpPost.setEntity(strEnt);
			
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			
			ResponseHandler<String> respHandler = new BasicResponseHandler();
			
			final String respStr = client.execute(httpPost, respHandler);
			if (StringUtils.isNotBlank(respStr)) {
				respJsonObj = new JSONObject(respStr);
			}
			
		} catch (Exception e) {
			Log.e(TAG, "error occurred while attempting to get JSON obj", e);
			
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		
		
		return respJsonObj;
	}
	
	private static HttpURLConnection createHttpURLConnection(final String addr) throws IOException {
		final URL url = new URL(addr);
		final HttpURLConnection httpURLConn = (HttpURLConnection)url.openConnection();
		
		return httpURLConn;
	}
	
	private static URLConnection createURLConnection(final String addr) throws IOException {
		final URL url = new URL(addr);
		final URLConnection connection = url.openConnection();
		connection.setUseCaches(true);	
		
		return connection;
	}

}
