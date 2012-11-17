package com.eastapps.meme_gen_server.domain;

// Generated Nov 10, 2012 3:21:20 PM by Hibernate Tools 3.4.0.CR1

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
 * SampleMeme generated by hbm2java
 */
@Entity
@Table(name = "sample_meme", catalog = "mgsdb")
public class SampleMeme implements java.io.Serializable {

	private Integer id;
	private Meme meme;

	public SampleMeme() {
	}

	public SampleMeme(Meme meme) {
		this.meme = meme;
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

}