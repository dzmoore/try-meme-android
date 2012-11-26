package com.eastapps.meme_gen_server.domain;

import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.util.StringUtils;

public class ShallowMeme {
	private int backgroundFk;
	private int id;
	private String topText;
	private String bottomText;
	private int userId;
	private String memeTypeDescr;
	private int memeTypeId;
	private int topTextFontSize;
	private int bottomTextFontSize;
	
	
	public ShallowMeme() {
		super();
		backgroundFk = Constants.INVALID;
		id = Constants.INVALID;
		userId = Constants.INVALID;
		memeTypeId = Constants.INVALID;
		topTextFontSize = Constants.INVALID;
		bottomTextFontSize = Constants.INVALID;
		
		topText = Constants.EMPTY_STRING;
		bottomText = Constants.EMPTY_STRING;
		memeTypeDescr = Constants.EMPTY_STRING;
	}
	
	public ShallowMeme(final Meme from) {
		this();
		
		backgroundFk = from.getMemeBackground() == null 
				? Constants.INVALID : from.getMemeBackground().getId();
		
		id = from.getId();
		userId = from.getUser() == null 
				? Constants.INVALID : from.getUser().getId();
		
		memeTypeId = from.getLvMemeType() == null 
				? Constants.INVALID : from.getLvMemeType().getId();
		
		final MemeText topTextMemeText = getMemeText(from, Constants.TOP);
		topTextFontSize = topTextMemeText.getFontSize();
		topText = topTextMemeText.getText();
		
		final MemeText bottomTextMemeText = getMemeText(from, Constants.BOTTOM);
		bottomTextFontSize = bottomTextMemeText.getFontSize();
		bottomText = bottomTextMemeText.getText();
		
		memeTypeDescr = from.getLvMemeType() == null
				? Constants.EMPTY_STRING : from.getLvMemeType().getDescr();
		
		
	}
	
	private MemeText getMemeText(final Meme from, final String type) {
		MemeText memeText = new MemeText();
		
		for (final MemeText eaTxt : from.getMemeTexts()) {
			if (StringUtils.equalsIgnoreCase(eaTxt.getTextType(), type)) {
				memeText = eaTxt;
				break;
			}
		}
		
		return memeText;
	}


	public int getBackgroundFk() {
		return backgroundFk;
	}


	public void setBackgroundFk(int backgroundFk) {
		this.backgroundFk = backgroundFk;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
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


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public String getMemeTypeDescr() {
		return memeTypeDescr;
	}


	public void setMemeTypeDescr(String memeTypeDescr) {
		this.memeTypeDescr = memeTypeDescr;
	}


	public int getMemeTypeId() {
		return memeTypeId;
	}


	public void setMemeTypeId(int memeTypeId) {
		this.memeTypeId = memeTypeId;
	}


	public int getTopTextFontSize() {
		return topTextFontSize;
	}


	public void setTopTextFontSize(int topTextFontSize) {
		this.topTextFontSize = topTextFontSize;
	}


	public int getBottomTextFontSize() {
		return bottomTextFontSize;
	}


	public void setBottomTextFontSize(int bottomTextFontSize) {
		this.bottomTextFontSize = bottomTextFontSize;
	}
	
	
}