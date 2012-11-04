package com.eastapps.meme_gen_android.web;

import android.content.Context;
import android.graphics.Bitmap;

import com.eastapps.meme_gen_android.http.IWebClient;
import com.eastapps.meme_gen_android.http.WebClient;
import com.eastapps.meme_gen_android.json.JSONObject;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.util.Conca;
import com.example.meme_gen_android.R;

public class MemeServerClient {
	private IWebClient webClient;
	private String webSvcAddr;
	private String webSvcBgrndSuffix;
	private String webSvcJsonSuffix;
	private String webSvcMemeDataSuffix;
	
	public MemeServerClient(final Context context) {
		super();
		
		webSvcAddr = context.getString(R.string.webServiceAddress);
		webSvcMemeDataSuffix = context.getString(R.string.webServiceMemeDataSuffix);
		webSvcBgrndSuffix = context.getString(R.string.webServiceBackgroundSuffix);
		webSvcJsonSuffix = context.getString(R.string.webServiceJsonSuffix);
		
		webClient = new WebClient();
	}
	
	public Bitmap getBackground(final int memeId) {
		return
			webClient.getBitmap(Conca.t(
				webSvcAddr, 
				Constants.URL_SEPARATOR, 
				webSvcMemeDataSuffix,
				Constants.URL_SEPARATOR,
				memeId,
				Constants.URL_SEPARATOR,
				webSvcBgrndSuffix
			));
	}
	
	public JSONObject getTexts(final int memeId) {
		return 
			webClient.getJSONObject(Conca.t(
				webSvcAddr,
				Constants.URL_SEPARATOR,
				webSvcMemeDataSuffix,
				Constants.URL_SEPARATOR,
				memeId,
				Constants.URL_SEPARATOR,
				webSvcJsonSuffix
			));
	}
}
