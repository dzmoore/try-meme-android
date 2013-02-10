package com.eastapps.meme_gen_server.domain;

// Generated Nov 10, 2012 3:21:20 PM by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;

import com.eastapps.meme_gen_server.annotation.PrimitiveField;
import com.eastapps.meme_gen_server.constants.Constants;

/**
 * MemeBackground generated by hbm2java
 */
@Entity
@Table(name = "meme_background")
public class MemeBackground implements java.io.Serializable {
	private static final long serialVersionUID = -5432995151764114800L;
	
	@PrimitiveField(fieldName = "id")
	private Integer id;
	
	private Boolean active;
	
	@PrimitiveField(fieldName = "path")
	private String path;
	
	private Set<Meme> memes = new HashSet<Meme>(0);
	
	private Date lastMod;

	public MemeBackground() {
		super();
		id = Constants.INVALID;
		active = true;
		path = StringUtils.EMPTY;
		memes = new HashSet<Meme>(0);
		lastMod = new Date();
	}

	public MemeBackground(Boolean active, String path, Date lastMod, Set<Meme> memes) {
		this();
		this.active = active;
		this.path = path;
		this.lastMod = lastMod;
		this.memes = memes;
	}

	
	public MemeBackground(Date lastMod) {
		this();
		this.lastMod = lastMod;
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

	@Column(name = "active")
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Column(name = "path", length = 200)
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "memeBackground")
	public Set<Meme> getMemes() {
		return this.memes;
	}

	public void setMemes(Set<Meme> memes) {
		this.memes = memes;
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
		StringBuilder builder = new StringBuilder();
		builder.append("MemeBackground [id=");
		builder.append(id);
		builder.append(", active=");
		builder.append(active);
		builder.append(", path=");
		builder.append(path);
		builder.append("]");
		return builder.toString();
	}
	
	

}
