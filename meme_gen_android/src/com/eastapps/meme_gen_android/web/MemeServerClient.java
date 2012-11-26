package com.eastapps.meme_gen_android.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.eastapps.meme_gen_android.domain.MemeViewData;
import com.eastapps.meme_gen_android.http.IWebClient;
import com.eastapps.meme_gen_android.http.WebClient;
import com.eastapps.meme_gen_android.json.JSONException;
import com.eastapps.meme_gen_android.json.JSONObject;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.util.StringUtils;
import com.eastapps.meme_gen_server.domain.IntResult;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.util.Conca;
import com.example.meme_gen_android.R;
import com.google.gson.Gson;

public class MemeServerClient {
	private static final String TAG = MemeServerClient.class.getSimpleName();

	private IWebClient webClient;
	private String webSvcAddr;
	private String webSvcBgrndSuffix;
	private String webSvcJsonSuffix;
	private String webSvcMemeDataSuffix;
	private String webSvcStoreMemePrefix;

	public MemeServerClient(final Context context) {
		super();

		webSvcAddr = context.getString(R.string.webServiceAddress);
		webSvcMemeDataSuffix = context.getString(R.string.webServiceMemeDataSuffix);
		webSvcBgrndSuffix = context.getString(R.string.webServiceBackgroundSuffix);
		webSvcJsonSuffix = context.getString(R.string.webServiceJsonSuffix);
		webSvcStoreMemePrefix = context.getString(R.string.webServiceStoreMemePrefix);

		webClient = new WebClient();
	}

	public MemeViewData createMemeViewData(final int memeId) {
		final MemeViewData dat = new MemeViewData();
		dat.setBackground(getBackground(memeId));

		final JSONObject textsJson = getTexts(memeId);

		try {
			dat.setTopText(textsJson.getString(Constants.KEY_TOP_TEXT));
		} catch (JSONException e) {
			Log.e(TAG, "error while parsing top text", e);
		}

		try {
			dat.setBottomText(textsJson.getString(Constants.KEY_BOTTOM_TEXT));
		} catch (JSONException e) {
			Log.e(TAG, "error while parsing bottom text", e);
		}

		return dat;
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


	public int storeMeme(final ShallowMeme shallowMeme) {
		int resultId = Constants.INVALID;

		final String respStr = webClient.getJsonObject(
			Conca.t(
				webSvcAddr,
				Constants.URL_SEPARATOR,
				webSvcStoreMemePrefix
			),
			new Gson().toJson(shallowMeme)
		);

		if (StringUtils.isNotBlank(respStr)) {
			try {
				final IntResult intRes = new Gson().fromJson(respStr, IntResult.class);
				if (intRes != null) {
					resultId = intRes.getResult();
				}

			} catch (Exception e) {
				Log.e(TAG, "err", e);
			}
		}


		return resultId;
	}

}
