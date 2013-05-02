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
@Table(name = "crawler_background")
public class CrawlerBackground implements Serializable {
	private static final long serialVersionUID = 6913468400008595118L;
	
	private Integer id;
	private Date lastMod;
	private String crawlerImgFilename;
	private String crawlerImgDesc;
	private String sourceDesc;
	
	public CrawlerBackground() {
		super();
		id = Constants.INVALID;
		sourceDesc = crawlerImgDesc = crawlerImgFilename = Constants.EMPTY;
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
	
	@Column(name = "source_desc", length = 255)
	public String getSourceDesc() {
		return sourceDesc;
	}
	
	public void setSourceDesc(final String sourceDesc) {
		this.sourceDesc = sourceDesc;
	}
	
	@Column(name = "crawler_img_filename", length = 255)
	public String getCrawlerImgFilename() {
		return crawlerImgFilename;
	}

	public void setCrawlerImgFilename(String crawlerImgFilename) {
		this.crawlerImgFilename = crawlerImgFilename;
	}
	
	@Column(name = "crawler_img_desc", length = 255)
	public String getCrawlerImgDesc() {
		return crawlerImgDesc;
	}

	public void setCrawlerImgDesc(String crawlerImgDesc) {
		this.crawlerImgDesc = crawlerImgDesc;
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



























