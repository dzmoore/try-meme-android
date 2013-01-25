package com.eastapps.meme_gen_android;

import android.app.Application;

import com.eastapps.meme_gen_android.mgr.Ini;

public class MemeGenApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
	
		Ini.t(this);
	}
	
}
