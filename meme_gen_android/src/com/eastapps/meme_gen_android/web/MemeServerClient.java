package com.eastapps.meme_gen_android.web;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.domain.MemeViewData;
import com.eastapps.meme_gen_android.http.IWebClient;
import com.eastapps.meme_gen_android.http.WebClient;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.util.StringUtils;
import com.eastapps.meme_gen_server.domain.IntResult;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.util.Conca;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MemeServerClient {
	private static final String TAG = MemeServerClient.class.getSimpleName();

	private IWebClient webClient;
	private String webSvcAddr;
	private String webSvcBgrndSuffix;
	private String webSvcJsonSuffix;
	private String webSvcMemeDataSuffix;
	private String webSvcStoreMemePrefix;
	private String webSvcSampleMemeDataPrefix;
	private String webSvcMemeTypePrefix;

	public MemeServerClient(final Context context) {
		super();

		webSvcAddr = context.getString(R.string.webServiceAddress);
		webSvcMemeDataSuffix = context.getString(R.string.webServiceMemeDataSuffix);
		webSvcBgrndSuffix = context.getString(R.string.webServiceBackgroundSuffix);
		webSvcJsonSuffix = context.getString(R.string.webServiceJsonSuffix);
		webSvcStoreMemePrefix = context.getString(R.string.webServiceStoreMemePrefix);
		webSvcSampleMemeDataPrefix = context.getString(R.string.webServiceSampleMemeDataPrefix);
		webSvcMemeTypePrefix = context.getString(R.string.webServiceMemeType);

		webClient = new WebClient();
	}

//	public MemeViewData createMemeViewData(final int memeId) {
//		final MemeViewData dat = new MemeViewData();
//		dat.setBackground(getBackground(memeId));
//		
//		dat.setMeme(getMeme(memeId));
//
//		return dat;
//	}
	
	public ShallowMeme getMeme(final int memeId) {
		final String result = 
			webClient.getJSONObject(Conca.t(
				webSvcAddr,
				Constants.URL_SEPARATOR,
				webSvcMemeDataSuffix,
				Constants.URL_SEPARATOR,
				memeId,
				Constants.URL_SEPARATOR,
				webSvcJsonSuffix
		));
		
		ShallowMeme shMemeResult = new ShallowMeme();
		if (StringUtils.isNotBlank(result)) {
			shMemeResult = new Gson().fromJson(result, ShallowMeme.class);
		}
		
		return shMemeResult;
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

	public List<ShallowMeme> getSampleMemes(int typeId) {
		final String result = 
			webClient.getJSONObject(Conca.t(
				webSvcAddr,
				Constants.URL_SEPARATOR,
				webSvcSampleMemeDataPrefix,
				Constants.URL_SEPARATOR,
				typeId,
				Constants.URL_SEPARATOR,
				webSvcJsonSuffix
		));
		
		List<ShallowMeme> shMemeResult = new ArrayList<ShallowMeme>(0);
		if (StringUtils.isNotBlank(result)) {
			Type listType = new TypeToken<Collection<ShallowMeme>>(){}.getType();
			shMemeResult = new Gson().fromJson(result, listType);
		}
		
		return shMemeResult;
	}
	
	public List<ShallowMemeType> getMemeTypes() {
		final String result =
			webClient.getJSONObject(Conca.t(
				webSvcAddr,
				Constants.URL_SEPARATOR,
				webSvcMemeTypePrefix,
				Constants.URL_SEPARATOR,
				webSvcJsonSuffix
		));
				
		List<ShallowMemeType> types = new ArrayList<ShallowMemeType>(0);
		if (StringUtils.isNotBlank(result)) {
			Type listType = new TypeToken<Collection<ShallowMemeType>>(){}.getType();
			types = new Gson().fromJson(result, listType);
		}
		
		return types;
	}
	
	public Bitmap getBackgroundForType(final int typeId) {
		return
			webClient.getBitmap(Conca.t(
				webSvcAddr, 
				Constants.URL_SEPARATOR, 
				webSvcMemeTypePrefix,
				Constants.URL_SEPARATOR,
				typeId,
				Constants.URL_SEPARATOR,
				webSvcBgrndSuffix
			));
	}
}
