package com.eastapps.meme_gen_android.service.impl;

import java.util.List;

import android.content.Context;

import com.eastapps.meme_gen_android.domain.MemeListItemData;
import com.eastapps.meme_gen_android.domain.MemeViewData;
import com.eastapps.meme_gen_android.service.IMemeService;
import com.eastapps.mgs.model.Meme;
import com.eastapps.mgs.model.MemeBackground;
import com.eastapps.mgs.model.MemeUser;

public class MemeServiceV2 implements IMemeService {
	private final static String TAG = MemeServiceV2.class.getName();
	
	private static final Object instLock = new Object();
	private static MemeServiceV2 instance;

	public static Context context;
	
	public static MemeServiceV2 getInstance() {
		synchronized (instLock) {
			if (instance == null) {
				instance = new MemeServiceV2();
			}
			
			return instance;
		}
	}

	@Override
	public long storeMeme(Meme shallowMeme) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<MemeListItemData> getAllMemeTypesListData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNewInstallKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long storeNewUser(MemeUser shallowUser) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<MemeListItemData> getAllFavMemeTypesListData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MemeListItemData> getAllPopularTypesListData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MemeListItemData> getAllTypesForSearch(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemeViewData createMemeViewData(MemeBackground memeBackground) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Meme> getSampleMemes(long memeBackgroundId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MemeBackground> getFavMemeTypesForUser(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemeBackground getMemeBackground(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean storeFavType(long userId, long memeBackgroundId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeFavType(long userId, long memeBackgroundId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<MemeBackground> getAllMemeBackgrounds() {
		// TODO Auto-generated method stub
		return null;
	}

}
