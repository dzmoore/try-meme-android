package com.eastapps.meme_gen_server.domain;

import java.io.Serializable;

import com.eastapps.meme_gen_android.util.Constants;

public class ShallowUser implements Serializable {
	private static final long serialVersionUID = 1853797399462675948L;
	private int id;
	private String username;
	private String installKey;
	
	public ShallowUser() {
		super();
		id = Constants.INVALID;
		username = Constants.EMPTY;
		installKey = Constants.EMPTY;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ShallowUser [id=");
		builder.append(id);
		builder.append(", username=");
		builder.append(username);
		builder.append("]");
		return builder.toString();
	}

	public String getInstallKey() {
		return installKey;
	}

	public void setInstallKey(String installKey) {
		this.installKey = installKey;
	}
	
	
	
	
}
