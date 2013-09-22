package com.eastapps.meme_gen_android.web;

import java.util.ArrayList;
import java.util.Collection;
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
import com.eastapps.mgs.model.SampleMeme;
import com.eastapps.util.Conca;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public class MemeServerClientV2 implements IMemeServerClient {
	private static final String TAG = MemeServerClientV2.class.getName();
	private static IMemeServerClient instance;
	private IWebClient webClient;
	private String webServiceAddress;
	private String webServiceMemesCreateJson;
	private String backgroundFileAddress;
	private String createMemeUrl;
	private String findPopularMemeBackgroundsForTypeNameUrl;
	private String createMemeUserUrl;
	private String listMemeBackgroundsUrl;
	private String listSampleMemesForBackgroundIdUrlPre;
	private String listSampleMemesForBackgroundIdUrlPost;
	private String listFavoriteMemeBackgroundsForUserIdUrlPre;

	public MemeServerClientV2(Context context) {
		super();

		final String environment = context.getString(R.string.environment);
		if (StringUtils.equals("dev", environment)) {
			webServiceAddress = context.getString(R.string.devWebServiceAddress);
			backgroundFileAddress = context.getString(R.string.prodBackgroundFileAddress);
			
		} else {
			webServiceAddress = context.getString(R.string.prodWebServiceAddress);
			backgroundFileAddress = context.getString(R.string.prodBackgroundFileAddress);
			
		}
		
		webServiceMemesCreateJson = context.getString(R.string.webServiceMemesCreateJson);
		
		
		createMemeUrl = Conca.t(webServiceAddress, webServiceMemesCreateJson);
		findPopularMemeBackgroundsForTypeNameUrl = Conca.t(webServiceAddress, context.getString(R.string.webServiceMemeBackgroundPopularityByTypeNameJson));
		createMemeUserUrl = Conca.t(webServiceAddress, context.getString(R.string.webServiceMemeUserCreateJson));
		listMemeBackgroundsUrl = Conca.t(webServiceAddress, context.getString(R.string.webServiceListMemeBackgroundsJson));
		listSampleMemesForBackgroundIdUrlPre = Conca.t(webServiceAddress, context.getString(R.string.webServiceListSampleMemesForBackgroundIdPre));
		listSampleMemesForBackgroundIdUrlPost = context.getString(R.string.webServiceListSampleMemesForBackgroundIdPost);
		listFavoriteMemeBackgroundsForUserIdUrlPre = Conca.t(webServiceAddress, context.getString(R.string.webServiceListFavoriteMemeBackgroundsForUserId));
		
		webClient = new WebClient();
		webClient.setConnectionTimeoutMs(context.getResources().getInteger(R.integer.connectionTimeoutMs));
		webClient.setConnectionUseCaches(context.getResources().getBoolean(R.bool.connectionUseCaches));		
		
	}
	
	public static synchronized void initialize(Context context) {
		MemeServiceV2.context = context;
		instance = new MemeServerClientV2(context);
	}
	
	public static synchronized IMemeServerClient getInstance() {
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
	public long storeMeme(Meme meme) {
		return webClient.sendRequestAsJson(createMemeUrl, meme, Long.class);
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
	public List<MemeBackground> getPopularMemeBackgrounds(final String popularityTypeName) {
		@SuppressWarnings("unchecked")
		final List<MemeBackground> memeBackgrounds = (List<MemeBackground>) webClient.sendRequestAsJsonReturnList(
			findPopularMemeBackgroundsForTypeNameUrl, 
			popularityTypeName, 
			new TypeToken<Collection<MemeBackground>>(){}.getType()
		);
	
		return memeBackgrounds;
	}
	
	@Override
	public long storeNewUser(final MemeUser memeUser) {
		return webClient.sendRequestAsJson(createMemeUserUrl, memeUser, Long.class);
	}

	@Override
	public List<MemeBackground> getAllMemeBackgrounds() {
		final String responseJson = webClient.getJSONObject(listMemeBackgroundsUrl);
		
		final List<MemeBackground> memeBackgrounds = new Gson().fromJson(
			responseJson, 
			new TypeToken<Collection<MemeBackground>>(){}.getType()
		);
		
		return memeBackgrounds;
	}
	
	@Override
	public List<Meme> getSampleMemes(final long memeBackgroundId) {
		final String url = Conca.t(
			listSampleMemesForBackgroundIdUrlPre, 
			"/", memeBackgroundId, 
			listSampleMemesForBackgroundIdUrlPost
		);
		
		final String responseJson = webClient.getJSONObject(url);
		
		final List<SampleMeme> sampleMemes = new Gson().fromJson(
			responseJson,
			new TypeToken<Collection<SampleMeme>>(){}.getType()
		);
		
		final List<Meme> memesForBackground = new ArrayList<Meme>(sampleMemes == null ? 0 : sampleMemes.size());
		
		if (sampleMemes != null && sampleMemes.size() > 0) {
			for (final SampleMeme eaSampleMeme : sampleMemes) {
				memesForBackground.add(eaSampleMeme.getSampleMeme());
			}
		}
		
		return memesForBackground;
	}

	@Override
	public List<MemeBackground> getFavMemeBackgroundsForUser(long userId) {
		final String url = Conca.t(
			listFavoriteMemeBackgroundsForUserIdUrlPre, 
			"/", userId
		);
		
		@SuppressWarnings("unchecked")
		final List<MemeBackground> favoriteMemeBackgrounds = 
			(List<MemeBackground>) webClient.getRequestAsJsonReturnList(
				url, 
				new TypeToken<Collection<MemeBackground>>(){}.getType()
			);
		
		return favoriteMemeBackgrounds;
	}
	
	
	

	@Override
	public String getNewInstallKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean storeFavMeme(long userId, long memeBackgroundId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeFavMeme(long userId, long typeId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<MemeBackground> getMemeBackgroundsByName(String query) {
		// TODO Auto-generated method stub
		return null;
	}

}
