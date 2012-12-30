package com.eastapps.meme_gen_server.domain;

// Generated Nov 10, 2012 3:21:20 PM by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.eastapps.meme_gen_server.constants.Constants;

/**
 * Meme generated by hbm2java
 */
@Entity
@Table(name = "meme", catalog = "mgsdb")
public class Meme implements java.io.Serializable {
	private static final long serialVersionUID = 7984784355451389282L;
	
	private Integer id;
	
	private LvMemeType lvMemeType;
	
	private MemeBackground memeBackground;
	
	private User user;
	
	private Set<MemeText> memeTexts;
	
	private Boolean isSampleMeme;
	
	private Date lastMod;

	public Meme() {
		super();
		id = Constants.INVALID;
		lvMemeType = new LvMemeType();
		memeBackground = new MemeBackground();
		user = new User();
		memeTexts = new HashSet<MemeText>(0);
	}
	
	public Meme(LvMemeType lvMemeType, User user, MemeBackground memeBackground, Boolean isSampleMeme, Date lastMod, Set<MemeText> memeTexts) {
		this.lvMemeType = lvMemeType;
		this.user = user;
		this.memeBackground = memeBackground;
		this.isSampleMeme = isSampleMeme;
		this.lastMod = lastMod;
		this.memeTexts = memeTexts;
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lv_meme_type_fk")
	public LvMemeType getLvMemeType() {
		return this.lvMemeType;
	}

	public void setLvMemeType(LvMemeType lvMemeType) {
		this.lvMemeType = lvMemeType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "meme_background_fk")
	public MemeBackground getMemeBackground() {
		return this.memeBackground;
	}

	public void setMemeBackground(MemeBackground memeBackground) {
		this.memeBackground = memeBackground;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "meme")
	public Set<MemeText> getMemeTexts() {
		return this.memeTexts;
	}

	public void setMemeTexts(Set<MemeText> memeTexts) {
		this.memeTexts = memeTexts;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by_user_fk")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<String, Object>();
		
		return null;
	}
	
	@Column(name = "is_sample_meme")
	public Boolean getIsSampleMeme() {
		return this.isSampleMeme;
	}

	public void setIsSampleMeme(Boolean isSampleMeme) {
		this.isSampleMeme = isSampleMeme;
	}
	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_mod", nullable = false, length = 19)
	public Date getLastMod() {
		return this.lastMod;
	}

	public void setLastMod(Date lastMod) {
		this.lastMod = lastMod;
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
		builder.append(", user=");
		builder.append(user);
		builder.append(", memeTexts=");
		builder.append(memeTexts != null ? toString(memeTexts, maxLen) : null);
		builder.append(", isSampleMeme=");
		builder.append(isSampleMeme);
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
	
	
}
