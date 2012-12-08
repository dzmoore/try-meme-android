package com.eastapps.meme_gen_android.service;

import java.util.List;

import com.eastapps.meme_gen_android.domain.MemeViewData;
import com.eastapps.meme_gen_server.domain.ShallowMeme;

public interface IMemeService {
	public ShallowMeme getMeme(final int id);
	
	int storeMeme(ShallowMeme shallowMeme);
	MemeViewData createMemeViewData(int memeId);

	List<ShallowMeme> getSampleMemes(int typeId);
}
