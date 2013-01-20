package com.eastapps.meme_gen_server.domain.generated;

// Generated Jan 20, 2013 12:48:03 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * User generated by hbm2java
 */
@Entity
@Table(name = "user", catalog = "mgsdb")
public class User implements java.io.Serializable {

	private Integer id;
	private String username;
	private String password;
	private Boolean active;
	private Date lastMod;
	private String salt;
	private Set deviceInfos = new HashSet(0);
	private Set memes = new HashSet(0);
	private Set userFavMemeTypes = new HashSet(0);

	public User() {
	}

	public User(String username, String password, Date lastMod) {
		this.username = username;
		this.password = password;
		this.lastMod = lastMod;
	}

	public User(String username, String password, Boolean active, Date lastMod, String salt, Set deviceInfos, Set memes,
		Set userFavMemeTypes) {
		this.username = username;
		this.password = password;
		this.active = active;
		this.lastMod = lastMod;
		this.salt = salt;
		this.deviceInfos = deviceInfos;
		this.memes = memes;
		this.userFavMemeTypes = userFavMemeTypes;
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
	public Set getDeviceInfos() {
		return this.deviceInfos;
	}

	public void setDeviceInfos(Set deviceInfos) {
		this.deviceInfos = deviceInfos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set getMemes() {
		return this.memes;
	}

	public void setMemes(Set memes) {
		this.memes = memes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set getUserFavMemeTypes() {
		return this.userFavMemeTypes;
	}

	public void setUserFavMemeTypes(Set userFavMemeTypes) {
		this.userFavMemeTypes = userFavMemeTypes;
	}

}
