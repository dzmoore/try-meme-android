package com.eastapps.meme_gen_server.domain;

// Generated Nov 10, 2012 3:21:20 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.eastapps.meme_gen_server.constants.Constants;

/**
 * LvMemeType generated by hbm2java
 */
@Entity
@Table(name = "lv_meme_type", catalog = "mgsdb")
public class LvMemeType implements java.io.Serializable {
	private static final long serialVersionUID = -5105139654526429935L;
	private Integer id;
	private String descr;
	private Boolean active;
	private Set<Meme> memes = new HashSet<Meme>(0);

	public LvMemeType() {
		super();
		id = Constants.INVALID;
		descr = StringUtils.EMPTY;
		active = true;
		memes = new HashSet<Meme>(0);
	}

	public LvMemeType(String descr, Boolean active, Set<Meme> memes) {
		this.descr = descr;
		this.active = active;
		this.memes = memes;
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

	@Column(name = "descr", length = 100)
	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	@Column(name = "active")
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lvMemeType")
	public Set<Meme> getMemes() {
		return this.memes;
	}

	public void setMemes(Set<Meme> memes) {
		this.memes = memes;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LvMemeType [id=");
		builder.append(id);
		builder.append(", descr=");
		builder.append(descr);
		builder.append(", active=");
		builder.append(active);
		builder.append("]");
		return builder.toString();
	}
	
	

}
