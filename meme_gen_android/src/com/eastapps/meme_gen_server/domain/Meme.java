package com.eastapps.meme_gen_server.domain;

// Generated Nov 10, 2012 3:21:20 PM by Hibernate Tools 3.4.0.CR1

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.eastapps.meme_gen_android.util.Constants;

/**
 * Meme generated by hbm2java
 */
public class Meme implements java.io.Serializable {
	private static final long serialVersionUID = 7984784355451389282L;
	private Integer id;
	private LvMemeType lvMemeType;
	private MemeBackground memeBackground;
	private User user;
	private Set<MemeText> memeTexts = new HashSet<MemeText>(0);
	private Set<SampleMeme> sampleMemes = new HashSet<SampleMeme>(0);

	public Meme() {
		super();
		id = Constants.INVALID;
		lvMemeType = new LvMemeType();
		memeBackground = new MemeBackground();
		user = new User();
		memeTexts = new HashSet<MemeText>(0);
		sampleMemes = new HashSet<SampleMeme>(0);
	}

	public Meme(
		LvMemeType lvMemeType, 
		MemeBackground memeBackground, 
		Set<MemeText> memeTexts, 
		Set<SampleMeme> sampleMemes,
		User user) 
	{
		this.lvMemeType = lvMemeType;
		this.memeBackground = memeBackground;
		this.memeTexts = memeTexts;
		this.sampleMemes = sampleMemes;
		this.user = user;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LvMemeType getLvMemeType() {
		return this.lvMemeType;
	}

	public void setLvMemeType(LvMemeType lvMemeType) {
		this.lvMemeType = lvMemeType;
	}

	public MemeBackground getMemeBackground() {
		return this.memeBackground;
	}

	public void setMemeBackground(MemeBackground memeBackground) {
		this.memeBackground = memeBackground;
	}

	public Set<MemeText> getMemeTexts() {
		return this.memeTexts;
	}

	public void setMemeTexts(Set<MemeText> memeTexts) {
		this.memeTexts = memeTexts;
	}

	public Set<SampleMeme> getSampleMemes() {
		return this.sampleMemes;
	}

	public void setSampleMemes(Set<SampleMeme> sampleMemes) {
		this.sampleMemes = sampleMemes;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("Meme [id=");
		builder.append(id);
		builder.append(", lvMemeType=");
		builder.append(lvMemeType);
		builder.append(", memeBackground=");
		builder.append(memeBackground);
		builder.append(", memeTexts=");
		builder.append(memeTexts != null ? toString(memeTexts, maxLen) : null);
		builder.append(", sampleMemes=");
		builder.append(sampleMemes == null ? "NULL" : "NOT NULL");
		builder.append("]");
		return builder.toString();
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	

}
