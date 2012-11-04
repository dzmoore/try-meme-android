package com.eastapps.meme_gen_android.domain;

import com.eastapps.meme_gen_android.util.Constants;

import android.graphics.Bitmap;

public class MemeViewData {
	private String topText;
	private String bottomText;
	private Bitmap background;
	
	public MemeViewData() {
		super();
		topText = Constants.EMPTY_STRING;
		bottomText = Constants.EMPTY_STRING;
		background = null;
	}
	
	public String getTopText() {
		return topText;
	}
	public void setTopText(String topText) {
		this.topText = topText;
	}
	public String getBottomText() {
		return bottomText;
	}
	public void setBottomText(String bottomText) {
		this.bottomText = bottomText;
	}
	public Bitmap getBackground() {
		return background;
	}
	public void setBackground(Bitmap background) {
		this.background = background;
	}
	
	
}
