package com.eastapps.meme_gen_android.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.eastapps.meme_gen_android.ViewMeme;
import com.eastapps.meme_gen_android.json.JSONObject;
import com.eastapps.util.Conca;

public class WebClient implements IWebClient {
	private static final String TAG = WebClient.class.getSimpleName();
	
	@Override
	public JSONObject getJSONObject(final String addr) {
		JSONObject jsonObj = new JSONObject();
		
		try {
			final URLConnection conn = createURLConnection(addr);
			
			final BufferedReader bufReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuilder sb = new StringBuilder();
			String line = "";
			while ((line = bufReader.readLine()) != null) {
				sb.append(line);
			}
			
			jsonObj = new JSONObject(sb.toString());
			
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
	
	private static URLConnection createURLConnection(final String addr) throws IOException {
		final URL url = new URL(addr);
		final URLConnection connection = url.openConnection();
		connection.setUseCaches(true);	
		
		return connection;
	}

}
