package com.eastapps.meme_gen_android.web;

import java.util.List;

import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.mgs.model.Meme;
import com.eastapps.mgs.model.MemeBackground;
import com.eastapps.mgs.model.MemeUser;

import android.graphics.Bitmap;

public interface IMemeServerClient {

	Bitmap getBackground(int typeId);

	long storeMeme(Meme meme);

	List<MemeBackground> getPopularTypes();

	int storeNewUser(MemeUser shallowUser);


}
