package com.eastapps.meme_gen_android.service;

import java.util.List;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.domain.MemeListItemData;
import com.eastapps.meme_gen_android.domain.MemeViewData;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.meme_gen_server.domain.ShallowUser;

public interface IMemeService {
	public ShallowMeme getMeme(final int id);
	
	int storeMeme(ShallowMeme shallowMeme);
	MemeViewData createMemeViewData(int typeId);

	List<ShallowMeme> getSampleMemes(int typeId);

	List<MemeListItemData> getAllMemeTypesListData();

	ShallowUser getUser(int userId);

	String getNewInstallKey();

	int storeNewUser(ShallowUser shallowUser);

	List<ShallowMemeType> getFavMemeTypesForUser(int userId);

	boolean storeFavType(int userId, int typeId);

	boolean removeFavType(int userId, int typeId);

	List<ShallowMemeType> getAllMemeTypes();

	ShallowMemeType getType(int typeId);
}
