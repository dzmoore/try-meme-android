package com.trymeme.meme_gen_android.service;

import java.util.List;

import com.eastapps.mgs.model.Meme;
import com.eastapps.mgs.model.MemeBackground;
import com.eastapps.mgs.model.MemeUser;
import com.trymeme.meme_gen_android.domain.MemeListItemData;
import com.trymeme.meme_gen_android.domain.MemeViewData;
import com.trymeme.meme_gen_android.mgr.ICallback;

public interface IMemeService {
	MemeViewData createMemeViewData(final MemeBackground memeBackground);

	List<Meme> getSampleMemes(final long memeBackgroundId);

	List<MemeListItemData> getAllMemeTypesListData();

	String getNewInstallKey();


	List<MemeBackground> getAllMemeBackgrounds();

	List<MemeListItemData> getAllFavMemeTypesListData();

	List<MemeListItemData> getAllPopularTypesListData();

	List<MemeListItemData> getAllTypesForSearch(String query);
	long storeMeme(Meme shallowMeme);

	long storeNewUser(MemeUser shallowUser);

	boolean storeFavType(long userId, long memeBackgroundId);

	boolean removeFavType(long userId, long memeBackgroundId);

	void setConnectionExceptionCallback(
			ICallback<Exception> connectionExceptionCallback);

	List<MemeBackground> getPopularTypes();

	MemeViewData createMemeViewData(List<Meme> memes);

	List<MemeBackground> findMemeBackgroundsByName(String query);

	List<Meme> getMemesForUser(long userId);
	
	List<MemeBackground> getFavoriteBackgrounds();
}
