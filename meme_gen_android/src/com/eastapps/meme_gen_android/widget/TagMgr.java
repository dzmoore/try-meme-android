package com.eastapps.meme_gen_android.widget;

import java.util.concurrent.atomic.AtomicInteger;

public class TagMgr {
	private static AtomicInteger memeViewIncrementor = new AtomicInteger();
	
	private static final String SEPARATOR = "-";
	private static final String TEXT_VIEW = "txt_view";
	private static final String TOP_TEXT_VIEW_TAG = "toptext";
	private static final String BOTTOM_TEXT_VIEW_TAG = "bottomtext";
	
	public static Object getTextViewTag(int id, boolean isTopText) {
		final StringBuilder sb = new StringBuilder();
		sb
			.append(TEXT_VIEW)
			.append(SEPARATOR)
			.append(isTopText ? TOP_TEXT_VIEW_TAG : BOTTOM_TEXT_VIEW_TAG)
			.append(SEPARATOR)
			.append(id);
		
		return sb.toString();
	}
	
	public static int getNextMemeViewId() {
		return memeViewIncrementor.incrementAndGet();
	}
	
}
