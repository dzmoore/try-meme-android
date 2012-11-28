package com.eastapps.meme_gen_android.domain;

import android.graphics.Bitmap;

import com.eastapps.meme_gen_server.domain.ShallowMeme;

public class MemeViewData {
	private ShallowMeme meme;
	private Bitmap background;
	
	public MemeViewData() {
		super();
		background = null;
		meme = new ShallowMeme();
	}
	
	public Bitmap getBackground() {
		return background;
	}
	public void setBackground(Bitmap background) {
		this.background = background;
	}

	public ShallowMeme getMeme() {
		return meme;
	}

	public void setMeme(ShallowMeme meme) {
		this.meme = meme;
	}
}
