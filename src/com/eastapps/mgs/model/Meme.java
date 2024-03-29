package com.eastapps.mgs.model;

import com.trymeme.meme_gen_android.R;
import java.io.Serializable;


public class Meme implements Serializable {
	private static final long serialVersionUID = -3787149307805034232L;

	private MemeBackground memeBackground;

    private MemeText topText;

    private MemeText bottomText;

    private MemeUser createdByUser;

    private Long id;

    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	public MemeBackground getMemeBackground() {
        return this.memeBackground;
    }

	public void setMemeBackground(MemeBackground memeBackground) {
        this.memeBackground = memeBackground;
    }

	public MemeText getTopText() {
        return this.topText;
    }

	public void setTopText(MemeText topText) {
        this.topText = topText;
    }

	public MemeText getBottomText() {
        return this.bottomText;
    }

	public void setBottomText(MemeText bottomText) {
        this.bottomText = bottomText;
    }

	public MemeUser getCreatedByUser() {
        return this.createdByUser;
    }

	public void setCreatedByUser(MemeUser createdByUser) {
        this.createdByUser = createdByUser;
    }
}
