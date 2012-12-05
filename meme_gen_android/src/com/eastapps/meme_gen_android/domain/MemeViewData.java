package com.eastapps.meme_gen_android.domain;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

import com.eastapps.meme_gen_server.domain.ShallowMeme;

public class MemeViewData {
	private ShallowMeme meme;
	private List<ShallowMeme> sampleMemes;
	private Bitmap background;
	
	public MemeViewData() {
		super();
		background = null;
		meme = new ShallowMeme();
		sampleMemes = new ArrayList<ShallowMeme>(0);
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

	public List<ShallowMeme> getSampleMemes() {
		return sampleMemes;
	}

	public void setSampleMemes(List<ShallowMeme> sampleMemes) {
		this.sampleMemes = sampleMemes;
	}
}
