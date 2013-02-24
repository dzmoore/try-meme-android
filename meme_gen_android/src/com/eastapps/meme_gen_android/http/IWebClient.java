package com.eastapps.meme_gen_android.http;

import android.graphics.Bitmap;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.json.JSONObject;

public interface IWebClient {
	public String getJSONObject(final String addr);
	public Bitmap getBitmap(final String addr);
	public String getJSONObject(String addr, String jsonRequest);
	void setConnectionTimeoutMs(int connectTimeoutMs);
	void setConnectionUseCaches(boolean useCaches);
}
