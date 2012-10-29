package com.eastapps.meme_gen_server.domain;

// Generated Oct 28, 2012 9:12:37 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Meme generated by hbm2java
 */
@Entity
@Table(name = "meme", catalog = "mgsdb")
public class Meme implements java.io.Serializable {

	private Integer id;
	private LvMemeType lvMemeType;
	private MemeBackground memeBackground;
	private Set memeTexts = new HashSet(0);

	public Meme() {
	}

	public Meme(LvMemeType lvMemeType, MemeBackground memeBackground,
			Set memeTexts) {
		this.lvMemeType = lvMemeType;
		this.memeBackground = memeBackground;
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
	public Set getMemeTexts() {
		return this.memeTexts;
	}

	public void setMemeTexts(Set memeTexts) {
		this.memeTexts = memeTexts;
	}

}
