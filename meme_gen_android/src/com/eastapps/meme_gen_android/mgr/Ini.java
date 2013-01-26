package com.eastapps.meme_gen_android.mgr;

import android.content.Context;

import com.eastapps.meme_gen_android.service.impl.MemeService;

public class Ini {
	
	public static void t(final Context context) {
		CacheMgr.initialize(context);
		UserMgr.initialize(context);
		MemeService.initialize(context);
		AdMgr.initialize();
	}
}
