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
			conn = createHttpURLConnection(addr);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			final OutputStream connOut = conn.getOutputStream();
			bw = new BufferedWriter(new OutputStreamWriter(connOut));
			
			Log.d(TAG, Conca.t("writing json object:[", request.toString()));
			
			bw.write(request.toString());
			bw.flush();
			
			final String responseText = getResponseText(conn);
			if (StringUtils.isNotBlank(responseText)) {
				respJsonObj = new JSONObject(responseText);
			}
			
			
		} catch (Exception e) {
			Log.e(TAG, "error occurred while attempting to get JSON obj", e);
			
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) { }
			}
			
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
