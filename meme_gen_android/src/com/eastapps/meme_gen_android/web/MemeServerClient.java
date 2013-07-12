package com.eastapps.meme_gen_android.web;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.http.IWebClient;
import com.eastapps.meme_gen_android.http.WebClient;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.util.StringUtils;
import com.eastapps.meme_gen_android.util.Utils;
import com.eastapps.meme_gen_server.domain.IntResult;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.meme_gen_server.domain.ShallowUser;
import com.eastapps.util.Conca;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MemeServerClient implements IMemeServerClient {
	private static final String TAG = MemeServerClient.class.getSimpleName();

	private IWebClient webClient;
	private String webSvcAddr;
	private String webSvcBgrndSuffix;
	private String webSvcJsonSuffix;
	private String webSvcStoreMemePrefix;
	private String webSvcSampleMemeDataPrefix;
	private String webSvcMemeTypePrefix;

	private String webSvcBgrndBytesSuffix;

	public MemeServerClient(final Context context) {
		super();

		webSvcAddr = context.getString(R.string.webServiceAddress);
		webSvcBgrndSuffix = context.getString(R.string.webServiceBackgroundSuffix);
		webSvcJsonSuffix = context.getString(R.string.webServiceJsonSuffix);
		webSvcStoreMemePrefix = context.getString(R.string.webServiceStoreMemePrefix);
		webSvcSampleMemeDataPrefix = context.getString(R.string.webServiceSampleMemeDataPrefix);
		webSvcMemeTypePrefix = context.getString(R.string.webServiceMemeType);
		webSvcBgrndBytesSuffix = context.getString(R.string.webServiceBackgroundBytesSuffix);

		webClient = new WebClient();
		webClient.setConnectionTimeoutMs(context.getResources().getInteger(R.integer.connectionTimeoutMs));
		webClient.setConnectionUseCaches(context.getResources().getBoolean(R.bool.connectionUseCaches));
	}

	@Override
	public Bitmap getBackground(final int bgId) {
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

	public int storeMeme(final ShallowMeme shallowMeme) {
		int resultId = Constants.INVALID;

		final String respStr = Utils.noValue(webClient.getJSONObject(
			Conca.t(
				webSvcAddr,
				Constants.URL_SEPARATOR,
				webSvcStoreMemePrefix
			),
			new Gson().toJson(shallowMeme)
		), Constants.EMPTY);

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
			Utils.noValue(webClient.getJSONObject(Conca.t(
				webSvcAddr,
				Constants.URL_SEPARATOR,
				webSvcSampleMemeDataPrefix,
				Constants.URL_SEPARATOR,
				typeId,
				Constants.URL_SEPARATOR,
				webSvcJsonSuffix
		)), Constants.EMPTY);
		
		List<ShallowMeme> shMemeResult = new ArrayList<ShallowMeme>(0);
		if (StringUtils.isNotBlank(result)) {
			Type listType = new TypeToken<Collection<ShallowMeme>>(){}.getType();
			shMemeResult = new Gson().fromJson(result, listType);
		}
		
		return shMemeResult;
	}
	
	public List<ShallowMemeType> getMemeTypes() {
		final String result =
			Utils.noValue(webClient.getJSONObject(Conca.t(
				webSvcAddr,
				Constants.URL_SEPARATOR,
				webSvcMemeTypePrefix,
				Constants.URL_SEPARATOR,
				webSvcJsonSuffix
		)), Constants.EMPTY);
				
		List<ShallowMemeType> types = new ArrayList<ShallowMemeType>(0);
		if (StringUtils.isNotBlank(result)) {
			Type listType = new TypeToken<Collection<ShallowMemeType>>(){}.getType();
			types = new Gson().fromJson(result, listType);
		}
		
		return types;
	}

//	private <T> Type getTypeForTypedList(final Class<T> type) {
//		Type listType = new TypeToken<Collection<T>>(){}.getType();
//		return listType;
//	}
	
//	public Bitmap getBackgroundForType(final int typeId) {
//		return
//			webClient.getBitmap(Conca.t(
//				webSvcAddr, 
//				Constants.URL_SEPARATOR, 
//				webSvcMemeTypePrefix,
//				Constants.URL_SEPARATOR,
//				typeId,
//				Constants.URL_SEPARATOR,
//				webSvcBgrndSuffix
//			));
//	}
	
	public ShallowUser getUserForId(final int id) {
		final String result =
			Utils.noValue(webClient.getJSONObject(Conca.t(
				webSvcAddr,
				Constants.URL_SEPARATOR,
				"user_data",
				Constants.URL_SEPARATOR,
				"user",
				Constants.URL_SEPARATOR,
				id,
				Constants.URL_SEPARATOR,
				webSvcJsonSuffix
			)), Constants.EMPTY);
				
		return new Gson().fromJson(result, ShallowUser.class);
	}

	public <T> T getObject(final Class<T> type, final Object... addrParts) {
		T typedResult = null;
		
		try {
			final String result = Utils.noValue(webClient.getJSONObject(Conca.t(addrParts)), Constants.EMPTY);
			typedResult = new Gson().fromJson(result, type);
			
		} catch (Exception e) {
			Log.e(TAG, "err", e);
		}
		
		return typedResult;
	}
	
	public <T> List<T> getList(final Class<T> itemType, final Object... addrParts) {
		final String result = Utils.noValue(webClient.getJSONObject(Conca.t(addrParts)), Constants.EMPTY);
		List<T> typedResult = new ArrayList<T>();
		
		try {
			typedResult = new Gson().fromJson(result, new TypeToken<Collection<T>>(){}.getType());
			
		} catch (Exception e) {
			Log.e(TAG, "err", e);
		}
		
		return typedResult;
	}
	
	public <T> T getObjectWithArg(
		final Class<T> type, 
		final String addr, 
		final Object arg)
	{
		T typedResult = null;
		
		try {
			final String result = webClient.getJSONObject(addr, new Gson().toJson(arg));
			typedResult = new Gson().fromJson(result, type);
			
		} catch (Exception e) {
			Log.e(TAG, "err", e);
		}
		
		return typedResult;
	}
	
	public String getNewInstallKey() {
		return getObject(String.class, 
			webSvcAddr,
			Constants.URL_SEPARATOR,
			"user_data",
			Constants.URL_SEPARATOR,
			"new_install_key",
			Constants.URL_SEPARATOR,
			webSvcJsonSuffix
		);
	}

	public int storeNewUser(ShallowUser shallowUser) {
		return Utils.noValue(getObjectWithArg(Integer.class, 
			Conca.t(
				webSvcAddr,
				Constants.URL_SEPARATOR,
				"user_data",
				Constants.URL_SEPARATOR,
				"store_new",
				Constants.URL_SEPARATOR,
				webSvcJsonSuffix
			),
			shallowUser
		), Constants.INVALID);
			
	}
	
	public List<ShallowMemeType> getFavMemeTypesForUser(int userId) {
		List<ShallowMemeType> types = new ArrayList<ShallowMemeType>(0);
		String result = Constants.EMPTY;
		
		final String addr = Conca.t(
			webSvcAddr,
			Constants.URL_SEPARATOR,
			"user_data",
			Constants.URL_SEPARATOR,
			"user",
			Constants.URL_SEPARATOR,
			userId,
			Constants.URL_SEPARATOR,
			"favtypes",
			Constants.URL_SEPARATOR,
			webSvcJsonSuffix
		);
		
		result = Utils.noValue(webClient.getJSONObject(addr), Constants.EMPTY);
		
		if (StringUtils.isNotBlank(result)) {
			Type listType = new TypeToken<Collection<ShallowMemeType>>(){}.getType();
			types = new Gson().fromJson(result, listType);
		}
		
		return types;
	}
	
	public boolean storeFavMeme(int userId, int typeId) {
		return 
			getObject(Boolean.class, 
				concatForUrl(
					webSvcAddr,
					"user_data",
					"user",
					userId,
					"favtypes",
					typeId,
					"store"
				)); 
	}
	
	public boolean removeFavMeme(int userId, int typeId) {
		return 
			getObject(Boolean.class, 
				concatForUrl(
					webSvcAddr,
					"user_data",
					"user",
					userId,
					"favtypes",
					typeId,
					"remove"
				));
	}
	
	private static final String concatForUrl(final Object... parts) {
		final StringBuilder sb = new StringBuilder();
		
		boolean isNotFirst = false;
		for (final Object ea : parts) { 
			if (isNotFirst) {
				sb.append(Constants.URL_SEPARATOR);
			}
			
			sb.append(ea);
			
			isNotFirst = true;
		}
		
		return sb.toString();
	}

//	"/meme_type_data/popular/json")
	public List<ShallowMemeType> getPopularTypes() {
		List<ShallowMemeType> types = new ArrayList<ShallowMemeType>(0);
		String result = Constants.EMPTY;
		
		final String addr = concatForUrl(
			webSvcAddr,
			webSvcMemeTypePrefix,
			"popular",
			webSvcJsonSuffix
		);
		
		result = Utils.noValue(webClient.getJSONObject(addr), Constants.EMPTY);
		
		if (StringUtils.isNotBlank(result)) {
			Type listType = new TypeToken<Collection<ShallowMemeType>>(){}.getType();
			types = new Gson().fromJson(result, listType);
		}
		
		return types;
	}

//	/meme_type_data/search/{searchTerm}/json")
	public List<ShallowMemeType> getTypesForSearch(String searchTerm) {
		List<ShallowMemeType> types = new ArrayList<ShallowMemeType>(0);
		String result = Constants.EMPTY;
		
		String addr;
		try {
			addr = concatForUrl(
				webSvcAddr,
				webSvcMemeTypePrefix,
				"search",
				URLEncoder.encode(searchTerm, "utf-8"),
				webSvcJsonSuffix
			);
			
			result = Utils.noValue(webClient.getJSONObject(addr), Constants.EMPTY);
			
			if (StringUtils.isNotBlank(result)) {
				Type listType = new TypeToken<Collection<ShallowMemeType>>(){}.getType();
				types = new Gson().fromJson(result, listType);
			}
			
		} catch (Exception e) {
			Log.e(TAG, "err", e);
		}
		
		return types;
	}

	
	
	
}























