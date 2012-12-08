package com.eastapps.meme_gen_server.domain.generated;

// Generated Dec 1, 2012 9:12:22 AM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * MemeText generated by hbm2java
 */
@Entity
@Table(name = "meme_text", catalog = "mgsdb")
public class MemeText implements java.io.Serializable {

	private Integer id;
	private Meme meme;
	private String text;
	private String textType;
	private Integer fontSize;

	public MemeText() {
	}

	public MemeText(Meme meme, String text, String textType, Integer fontSize) {
		this.meme = meme;
		this.text = text;
		this.textType = textType;
		this.fontSize = fontSize;
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
	@JoinColumn(name = "meme_fk")
	public Meme getMeme() {
		return this.meme;
	}

	public void setMeme(Meme meme) {
		this.meme = meme;
	}

	@Column(name = "text", length = 200)
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name = "text_type", length = 20)
	public String getTextType() {
		return this.textType;
	}

	public void setTextType(String textType) {
		this.textType = textType;
	}

	@Column(name = "font_size")
	public Integer getFontSize() {
		return this.fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}

}
