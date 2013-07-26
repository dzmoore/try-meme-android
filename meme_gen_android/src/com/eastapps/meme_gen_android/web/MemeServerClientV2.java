package com.eastapps.meme_gen_android.web;

import java.util.List;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.http.IWebClient;
import com.eastapps.meme_gen_android.http.WebClient;
import com.eastapps.meme_gen_android.service.impl.MemeService;
import com.eastapps.meme_gen_android.service.impl.MemeServiceV2;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.util.StringUtils;
import com.eastapps.meme_gen_android.util.Utils;
import com.eastapps.meme_gen_server.domain.IntResult;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.mgs.model.Meme;
import com.eastapps.mgs.model.MemeBackground;
import com.eastapps.mgs.model.MemeUser;
import com.eastapps.util.Conca;
import com.google.gson.Gson;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public class MemeServerClientV2 implements IMemeServerClient {
	private static final String TAG = MemeServerClientV2.class.getName();
	private static IMemeServerClient instance;
	private IWebClient webClient;
	private String webServiceAddress;
	private String webServiceMemesCreateJson;
	private String webServiceBackgroundBytes;
	private String backgroundFileAddress;

	public MemeServerClientV2(Context context) {
		super();

		webServiceAddress = context.getString(R.string.webServiceAddress);
		webServiceMemesCreateJson = context.getString(R.string.webServiceMemesCreateJson);
		webServiceBackgroundBytes = context.getString(R.string.webServiceBackgroundBytes);
		backgroundFileAddress = context.getString(R.string.backgroundFileAddress);
		
		webClient = new WebClient();
		webClient.setConnectionTimeoutMs(context.getResources().getInteger(R.integer.connectionTimeoutMs));
		webClient.setConnectionUseCaches(context.getResources().getBoolean(R.bool.connectionUseCaches));		
		
	}
	
	public static synchronized void initialize(Context context) {
		MemeServiceV2.context = context;
		instance = new MemeServerClientV2(context);
	}
	
	private static synchronized IMemeServerClient getInstance() {
		return instance;
	}
	
	private static String concatUrlPieces(final Object... parts) {
		final StringBuilder sb = new StringBuilder();
		
		boolean notFirst = false;
		for (final Object ea : parts) {
			if (notFirst) {
				sb.append(Constants.URL_SEPARATOR);
			}
			
			sb.append(ea);
			
			notFirst = true;
		}
		
		return sb.toString();
	}

	@Override
	public Bitmap getBackground(final String path) {
		return
			webClient.getBitmap(concatUrlPieces(
				backgroundFileAddress,
				path
			));
	}

	@Override
	public long storeMeme(Meme meme) {
		int resultId = Constants.INVALID;

		final String respStr = Utils.noValue(webClient.getJSONObject(
			concatUrlPieces(
				webServiceAddress,
				webServiceMemesCreateJson
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

	@Override
	public List<MemeBackground> getPopularMemeBackgrounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int storeNewUser(MemeUser shallowUser) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<MemeBackground> getAllMemeBackgrounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Meme> getSampleMemes(long memeId) {
		// TODO Auto-generated method stub
		return null;
	}

}
