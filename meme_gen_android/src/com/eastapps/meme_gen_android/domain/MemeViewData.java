package com.eastapps.meme_gen_android.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.widget.TagMgr;
import com.eastapps.meme_gen_server.domain.ShallowMeme;

public class MemeViewData 
	implements Serializable, Identifiable {
	private static final long serialVersionUID = 7334702044762722957L;
	
	private ShallowMeme meme;
	private List<ShallowMeme> sampleMemes;
	private Bitmap background;
	private int id;
	
	public MemeViewData() {
		super();
		background = null;
		meme = new ShallowMeme();
		sampleMemes = new ArrayList<ShallowMeme>(0);
		id = TagMgr.getNextMemeViewId();
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

	@Override
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
}
