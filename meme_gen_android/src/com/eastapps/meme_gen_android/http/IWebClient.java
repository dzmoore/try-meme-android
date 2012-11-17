package com.eastapps.meme_gen_android.http;

import android.graphics.Bitmap;

import com.eastapps.meme_gen_android.json.JSONObject;

public interface IWebClient {
	public JSONObject getJSONObject(final String addr);
	public Bitmap getBitmap(final String addr);
}