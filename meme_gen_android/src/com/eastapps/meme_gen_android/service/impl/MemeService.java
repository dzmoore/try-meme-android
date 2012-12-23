package com.eastapps.meme_gen_android.service.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.eastapps.meme_gen_android.domain.MemeViewData;
import com.eastapps.meme_gen_android.json.JSONException;
import com.eastapps.meme_gen_android.json.JSONObject;
import com.eastapps.meme_gen_android.service.IMemeService;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.web.MemeServerClient;
import com.eastapps.meme_gen_android.widget.TagMgr;
import com.eastapps.meme_gen_server.domain.ShallowMeme;

public class MemeService implements IMemeService {
	private static final String TAG = MemeService.class.getSimpleName();
	private static MemeService instance;
	
	
	private MemeServerClient client;
	private static Context context;
	
	private MemeService() {
		super();
		
		client = new MemeServerClient(context);
	}
	
	public static synchronized void initialize(Context context) {
		if (MemeService.context == null) {
			MemeService.context = context;
			instance = new MemeService();
		}
	}
	
	public static synchronized MemeService getInstance() {
		return instance;
	}

	@Override
	public int storeMeme(ShallowMeme shallowMeme) {
		return client.storeMeme(shallowMeme);
	}

	@Override
	public ShallowMeme getMeme(int id) {
		final ShallowMeme m = client.getMeme(id);
		return m;
	}
	
	@Override
	public List<ShallowMeme> getSampleMemes(final int typeId) {
		return client.getSampleMemes(typeId);
	}

	@Override
	public MemeViewData createMemeViewData(int memeId) {
		final MemeViewData dat = new MemeViewData();
		
		dat.setBackground(client.getBackground(memeId));
		
		dat.setMeme(getMeme(memeId));
		
		if (dat.getMeme().getMemeTypeId() > 0) {
			dat.setSampleMemes(getSampleMemes(dat.getMeme().getMemeTypeId()));
		}
		
		return dat;
	}

}
