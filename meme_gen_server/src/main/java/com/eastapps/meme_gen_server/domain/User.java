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
 * User generated by hbm2java
 */
@Entity
@Table(name = "user", catalog = "mgsdb")
public class User implements java.io.Serializable {
	private static final long serialVersionUID = 8212798718211670129L;
	
	@PrimitiveField(fieldName = "id")
	private Integer id;
	
	@PrimitiveField(fieldName = "un")
	private String username;
	
	private String password;
	
	private Boolean active;
	private Date lastMod;
	
	private String salt;
	private Set<DeviceInfo> deviceInfos = new HashSet<DeviceInfo>(0);
	private Set<Meme> memes = new HashSet<Meme>(0);

	public User() {
		super();
		id = Constants.INVALID;
		username = StringUtils.EMPTY;
		password = StringUtils.EMPTY;
		active = true;
		lastMod = new Date();
		salt = StringUtils.EMPTY;
		deviceInfos = new HashSet<DeviceInfo>(0);
		memes = new HashSet<Meme>(0);
	}

	public User(String username, String password, Date lastMod) {
		this();
		this.username = username;
		this.password = password;
		this.lastMod = lastMod;
	}

	public User(
		String username, 
		String password, 
		Boolean active, 
		Date lastMod, 
		String salt, 
		Set<DeviceInfo> deviceInfos, 
		Set<Meme> memes) 
	{
		this();
		this.username = username;
		this.password = password;
		this.active = active;
		this.lastMod = lastMod;
		this.salt = salt;
		this.deviceInfos = deviceInfos;
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

	@Column(name = "username", nullable = false, length = 40)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", nullable = false, length = 60)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "active")
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_mod", nullable = false, length = 19)
	public Date getLastMod() {
		return this.lastMod;
	}

	public void setLastMod(Date lastMod) {
		this.lastMod = lastMod;
	}

	@Column(name = "salt", length = 40)
	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<DeviceInfo> getDeviceInfos() {
		return this.deviceInfos;
	}

	public void setDeviceInfos(Set<DeviceInfo> deviceInfos) {
		this.deviceInfos = deviceInfos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<Meme> getMemes() {
		return this.memes;
	}

	public void setMemes(Set<Meme> memes) {
		this.memes = memes;
	}

}
