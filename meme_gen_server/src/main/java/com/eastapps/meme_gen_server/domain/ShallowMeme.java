package com.eastapps.meme_gen_server.domain;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.eastapps.meme_gen_server.constants.Constants;

public class ShallowMeme implements Serializable {
	private static final long serialVersionUID = 4479464552039295027L;
	private Meme innerMeme;
	private String topText, bottomText;

	public ShallowMeme(final Meme inner) {
		super();

		this.innerMeme = inner;

		if (this.innerMeme == null) {
			this.innerMeme = new Meme();
		}

		initTopAndBottomText();
	}

	private void initTopAndBottomText() {
		bottomText = topText = null;
		for (Iterator<MemeText> itr = innerMeme.getMemeTexts().iterator(); itr.hasNext(); ) {
			final MemeText ea = itr.next();

			final String type = ea.getTextType();
			if (StringUtils.equals(type,Constants.TOP)) {
				topText = ea.getText();

			} else if (StringUtils.equals(type, Constants.BOTTOM)) {
				bottomText = ea.getText();
			}

			if (topText != null && bottomText != null) {
				break;
			}
		}
	}

	public int getId() {
		return innerMeme.getId();
	}

	public void setId(int id) {
		innerMeme.setId(id);
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



}
