package com.trymeme.meme_gen_android.mgr;

import android.app.Activity;

import com.trymeme.meme_gen_android.R;

public class AdMgr {
	public static AdMgr instance;
	
	public AdMgr() {
		super();
	}
	
	public static void initialize() {
		getInstance();
	}
	
	public static synchronized AdMgr getInstance() {
		if (instance == null) {
			instance = new AdMgr();
		}
		
		return instance;
	}
	
	
	public void initAd(final Activity activity, final int adViewId, final long refreshRateMs) {
//		final MobclixMMABannerXLAdView ad = (MobclixMMABannerXLAdView) activity.findViewById(adViewId);
		
//		ad.setRefreshTime(refreshRateMs);
//		ad.getAd();
	}

	public void initAd(final Activity activity, final int viewId) {
		initAd(activity, viewId, activity.getResources().getInteger(R.integer.ad_refresh_time_ms));
	}
	
}
