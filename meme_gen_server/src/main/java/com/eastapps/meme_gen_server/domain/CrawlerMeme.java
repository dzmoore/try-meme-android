package com.eastapps.meme_gen_server.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.eastapps.meme_gen_server.constants.Constants;


@Entity
@Table(name = "crawler_meme")
public class CrawlerMeme implements Serializable {
	private static final long serialVersionUID = -3942250267862178577L;
	
	private Integer id;
	private String name;
	private String topText;
	private String bottomText;
	private Date lastMod;
	
	public CrawlerMeme() {
		super();
		id = -1;
		name = topText = bottomText = Constants.EMPTY;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", length = 255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "top_text", length = 255)
	public String getTopText() {
		return topText;
	}

	public void setTopText(String topText) {
		this.topText = topText;
	}

	@Column(name = "bottom_text", length = 255)
	public String getBottomText() {
		return bottomText;
	}

	public void setBottomText(String bottomText) {
		this.bottomText = bottomText;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_mod", nullable = false, length = 19)
	public Date getLastMod() {
		return lastMod;
	}

	public void setLastMod(Date lastMod) {
		this.lastMod = lastMod;
	}
}
