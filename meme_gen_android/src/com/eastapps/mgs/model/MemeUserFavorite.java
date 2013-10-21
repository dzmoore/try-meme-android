package com.eastapps.mgs.model;

import com.eastapps.meme_gen_android.R;
import java.io.Serializable;

public class MemeUserFavorite implements Serializable {
	private static final long serialVersionUID = -3528340674212252670L;

    private MemeUser memeUser;

    private MemeBackground memeBackground;

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

	public MemeUser getMemeUser() {
        return this.memeUser;
    }

	public void setMemeUser(MemeUser memeUser) {
        this.memeUser = memeUser;
    }

	public MemeBackground getMemeBackground() {
        return this.memeBackground;
    }

	public void setMemeBackground(MemeBackground memeBackground) {
        this.memeBackground = memeBackground;
    }


}
