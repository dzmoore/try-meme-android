package com.eastapps.meme_gen_android.web;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.http.IWebClient;
import com.eastapps.meme_gen_android.http.WebClient;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.util.StringUtils;
import com.eastapps.meme_gen_android.util.Utils;
import com.eastapps.meme_gen_server.domain.IntResult;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.mgs.model.Meme;
import com.eastapps.util.Conca;
import com.google.gson.Gson;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public class MemeServerClientV2 implements IMemeServerClient {
	private static final String TAG = MemeServerClientV2.class.getName();
	private IWebClient webClient;
	private String webSvcAddr;
	private String webSvcBgrndSuffix;
	private String webSvcMemeSuffix;
	private String webSvcJsonSuffix;
	private String webSvcStoreMemePrefix;
	private String webSvcSampleMemeDataPrefix;
	private String webSvcMemeTypePrefix;

	private String webSvcBgrndBytesSuffix;
	public MemeServerClientV2(Context context) {
		super();

		webSvcAddr = context.getString(R.string.webServiceAddress);
		webSvcBgrndSuffix = context.getString(R.string.webServiceBackgroundSuffix);
		webSvcJsonSuffix = context.getString(R.string.webServiceJsonSuffix);
		webSvcStoreMemePrefix = context.getString(R.string.webServiceStoreMemePrefix);
		webSvcSampleMemeDataPrefix = context.getString(R.string.webServiceSampleMemeDataPrefix);
		webSvcMemeTypePrefix = context.getString(R.string.webServiceMemeType);
		webSvcBgrndBytesSuffix = context.getString(R.string.webServiceBackgroundBytesSuffix);
		webSvcMemeSuffix = context.getString(R.string.webServiceMemeSuffix);

		webClient = new WebClient();
		webClient.setConnectionTimeoutMs(context.getResources().getInteger(R.integer.connectionTimeoutMs));
		webClient.setConnectionUseCaches(context.getResources().getBoolean(R.bool.connectionUseCaches));		
	}

	@Override
	public Bitmap getBackground(int bgId) {
		return
			webClient.getBitmap(Conca.t(
				webSvcAddr, 
				Constants.URL_SEPARATOR, 
				webSvcBgrndSuffix,
				Constants.URL_SEPARATOR,
				webSvcBgrndBytesSuffix,
				Constants.URL_SEPARATOR,
				bgId
			));
	}

	@Override
	public long storeMeme(Meme meme) {
		int resultId = Constants.INVALID;

		final String respStr = Utils.noValue(webClient.getJSONObject(
			Conca.t(
				webSvcAddr,
				Constants.URL_SEPARATOR,
				webSvcMemeSuffix
			),
			new Gson().toJson(meme)
		), Constants.EMPTY);

		if (StringUtils.isNotBlank(respStr)) {
			
			try {
				resultId = new Gson().fromJson(respStr, Integer.class);

			} catch (Exception e) {
				Log.e(TAG, "err", e);
			}
		}


		return resultId;
	}

}
