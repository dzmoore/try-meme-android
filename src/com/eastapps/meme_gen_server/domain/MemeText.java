package com.eastapps.meme_gen_server.domain;

// Generated Nov 10, 2012 3:21:20 PM by Hibernate Tools 3.4.0.CR1

import com.trymeme.meme_gen_android.R;
import com.trymeme.meme_gen_android.util.Constants;
import com.trymeme.meme_gen_android.util.StringUtils;

/**
 * MemeText generated by hbm2java
 */
public class MemeText implements java.io.Serializable {
	private static final long serialVersionUID = -2665509303313696077L;
	private Integer id;
	private Meme meme;
	private String text;
	private String textType;
	private Integer fontSize;

	public MemeText() {
		super();
		id = Constants.INVALID;
		meme = new Meme();
		text = StringUtils.EMPTY;
		textType = StringUtils.EMPTY;
		fontSize = Constants.INVALID;
	}

	public MemeText(Meme meme, String text, String textType, Integer fontSize) {
		this.meme = meme;
		this.text = text;
		this.textType = textType;
		this.fontSize = fontSize;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Meme getMeme() {
		return this.meme;
	}

	public void setMeme(Meme meme) {
		this.meme = meme;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTextType() {
		return this.textType;
	}

	public void setTextType(String textType) {
		this.textType = textType;
	}
	
	public Integer getFontSize() {
		return this.fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MemeText [id=");
		builder.append(id);
		builder.append(", text=");
		builder.append(text);
		builder.append(", textType=");
		builder.append(textType);
		builder.append("]");
		return builder.toString();
	}


}
