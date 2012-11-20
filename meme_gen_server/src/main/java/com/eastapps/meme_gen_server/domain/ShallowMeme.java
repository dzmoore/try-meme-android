package com.eastapps.meme_gen_server.domain;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.eastapps.meme_gen_server.constants.Constants;

public class ShallowMeme implements Serializable {
	private static final long serialVersionUID = 4479464552039295027L;
	private Meme innerMeme;
	private MemeText memeTextTopText;
	private MemeText memeTextBottomText;
	
	public ShallowMeme() {
		this(new Meme());
	}

	public ShallowMeme(final Meme inner) {
		super();

		this.setInnerMeme(inner);

		if (this.getInnerMeme() == null) {
			this.setInnerMeme(new Meme());
		}

		initTopAndBottomText();
	}

	private void initTopAndBottomText() {
		setMemeTextBottomText(setMemeTextTopText(null));
		for (Iterator<MemeText> itr = getInnerMeme().getMemeTexts().iterator(); itr.hasNext(); ) {
			final MemeText ea = itr.next();

			final String type = ea.getTextType();
			if (StringUtils.equals(type,Constants.TOP)) {
				setMemeTextTopText(ea);

			} else if (StringUtils.equals(type, Constants.BOTTOM)) {
				setMemeTextBottomText(ea);
			}

			if (getMemeTextTopText() != null && getMemeTextBottomText() != null) {
				break;
			}
		}
	}
	
	public int getBackgroundFk() {
		return getInnerMeme().getMemeBackground().getId();
	}
	
	public void setBackgroundFk(int fk) {
		getInnerMeme().getMemeBackground().setId(fk);
	}

	public int getId() {
		return getInnerMeme().getId();
	}

	public void setId(int id) {
		getInnerMeme().setId(id);
	}

	public String getTopText() {
		return getMemeTextTopText().getText();
	}

	public void setTopText(String topText) {
		this.getMemeTextTopText().setText(topText);
	}

	public String getBottomText() {
		return getMemeTextBottomText().getText();
	}

	public void setBottomText(String bottomText) {
		this.getMemeTextBottomText().setText(bottomText);
	}

	public int getTopTextFontSize() {
		return getMemeTextTopText().getFontSize();
	}

	public void setTopTextFontSize(int topTextFontSize) {
		this.getMemeTextTopText().setFontSize(topTextFontSize);
	}

	public int getBottomTextFontSize() {
		return getMemeTextBottomText().getFontSize();
	}

	public void setBottomTextFontSize(int bottomTextFontSize) {
		this.getMemeTextBottomText().setFontSize(bottomTextFontSize);
	}

	public int getUserId() {
		return getInnerMeme().getUser().getId();
	}

	public void setUserId(int id) {
		this.getInnerMeme().getUser().setId(id);
	}

	private synchronized Meme getInnerMeme() {
		if (innerMeme == null) {
			innerMeme = new Meme();
		}
		
		return innerMeme;
	}

	private void setInnerMeme(Meme innerMeme) {
		this.innerMeme = innerMeme;
	}
	
	public Meme toMeme() {
		return getInnerMeme();
	}

	public String getTopTextType() {
		return getMemeTextTopText().getTextType();
	}
	
	public void setTopTextType(final String type) {
		getMemeTextTopText().setTextType(type);
	}
	
	public String getBottomTextType() {
		return getMemeTextBottomText().getTextType();
	}
	
	public void setBottomTextType(final String textType) {
		getMemeTextBottomText().setTextType(textType);
	}

	private synchronized MemeText getMemeTextTopText() {
		if (memeTextTopText == null) {
			MemeText found = findText(Constants.TOP);
			if (found == null || getInnerMeme().getMemeTexts().size() == 0) {
				memeTextTopText = new MemeText(getInnerMeme(), StringUtils.EMPTY, Constants.TOP, 26);
			
			} else {
				memeTextTopText = found;
			}
		}
		
		return memeTextTopText;
	}

	private MemeText findText(final String textType) {
		for (Iterator<MemeText> iterator = getInnerMeme().getMemeTexts().iterator(); iterator.hasNext();) {
			MemeText type = iterator.next();
			
			if (StringUtils.equals(type.getTextType(), textType)) {
				return type;
			}
			
		}
		
		return new MemeText();
	}

	private MemeText setMemeTextTopText(MemeText memeTextTopText) {
		this.memeTextTopText = memeTextTopText;
		return memeTextTopText;
	}

	private synchronized MemeText getMemeTextBottomText() {
		if (memeTextBottomText == null) {
			MemeText found = findText(Constants.BOTTOM);
			if (found == null || getInnerMeme().getMemeTexts().size() == 0) {
				memeTextBottomText = new MemeText(getInnerMeme(), StringUtils.EMPTY, Constants.BOTTOM, 26);
			
			} else {
				memeTextBottomText = found;
			}
		}
		
		return memeTextBottomText;
	}

	private void setMemeTextBottomText(MemeText memeTextBottomText) {
		this.memeTextBottomText = memeTextBottomText;
	}
	
	
	


}
