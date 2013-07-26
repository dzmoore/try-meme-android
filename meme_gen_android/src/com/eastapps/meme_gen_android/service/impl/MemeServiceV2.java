package com.eastapps.meme_gen_android.service.impl;

import java.util.List;

import android.content.Context;

import com.eastapps.meme_gen_android.domain.MemeListItemData;
import com.eastapps.meme_gen_android.domain.MemeViewData;
import com.eastapps.meme_gen_android.service.IMemeService;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.meme_gen_server.domain.ShallowUser;
import com.eastapps.mgs.model.Meme;
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
	public MemeViewData createMemeViewData(int typeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ShallowMeme> getSampleMemes(int typeId) {
		// TODO Auto-generated method stub
		return null;
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
	public List<ShallowMemeType> getFavMemeTypesForUser(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean storeFavType(int userId, int typeId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeFavType(int userId, int typeId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ShallowMemeType> getAllMemeBackgrounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ShallowMemeType getType(int typeId) {
		// TODO Auto-generated method stub
		return null;
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

}
