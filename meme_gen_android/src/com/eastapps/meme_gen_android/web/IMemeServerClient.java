package com.eastapps.meme_gen_android.web;

import java.util.List;

import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.mgs.model.Meme;
import com.eastapps.mgs.model.MemeBackground;
import com.eastapps.mgs.model.MemeUser;

import android.graphics.Bitmap;

public interface IMemeServerClient {

	Bitmap getBackground(String path);

	long storeMeme(Meme meme);

	List<MemeBackground> getPopularMemeBackgrounds(final String popularTypeName);

	long storeNewUser(MemeUser shallowUser);

	List<MemeBackground> getAllMemeBackgrounds();

	List<Meme> getSampleMemes(long memeBackgroundId);

	List<MemeBackground> getFavMemeBackgroundsForUser(long userId);

	String getNewInstallKey();

	boolean storeFavMemeBackground(long userId, long memeBackgroundId);

	boolean removeFavMeme(long userId, long typeId);

	List<MemeBackground> getMemeBackgroundsByName(String query);


}
