package com.eastapps.meme_gen_android.service;

import java.util.List;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.domain.MemeListItemData;
import com.eastapps.meme_gen_android.domain.MemeViewData;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.meme_gen_server.domain.ShallowUser;
import com.eastapps.mgs.model.Meme;
import com.eastapps.mgs.model.MemeBackground;
import com.eastapps.mgs.model.MemeUser;

public interface IMemeService {
//	public ShallowMeme getMeme(final int id);
	
	MemeViewData createMemeViewData(final MemeBackground memeBackground);

	List<Meme> getSampleMemes(final long memeBackgroundId);

	List<MemeListItemData> getAllMemeTypesListData();

//	ShallowUser getUser(int userId);

	String getNewInstallKey();


	List<ShallowMemeType> getFavMemeTypesForUser(int userId);

	boolean storeFavType(int userId, int typeId);

	boolean removeFavType(int userId, int typeId);

	List<MemeBackground> getAllMemeBackgrounds();
	MemeBackground getMemeBackground(final long id);

	ShallowMemeType getType(int typeId);

	List<MemeListItemData> getAllFavMemeTypesListData();

	List<MemeListItemData> getAllPopularTypesListData();

	List<MemeListItemData> getAllTypesForSearch(String query);
	long storeMeme(Meme shallowMeme);

	long storeNewUser(MemeUser shallowUser);
}
