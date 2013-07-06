package com.eastapps.meme_gen_android.web;

import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.mgs.model.Meme;

import android.graphics.Bitmap;

public interface IMemeServerClient {

	Bitmap getBackground(int typeId);

	long storeMeme(Meme meme);

}
