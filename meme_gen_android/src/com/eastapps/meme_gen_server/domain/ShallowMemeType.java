package com.eastapps.meme_gen_server.domain;

import java.io.Serializable;

import com.eastapps.meme_gen_android.util.Constants;

public class ShallowMemeType implements Serializable {
	private static final long serialVersionUID = -8623799153302126317L;
	private int typeId;
	private String typeDescr;
	private int backgroundId;
	
	public ShallowMemeType() {
		super();
		typeId = Constants.INVALID;
		typeDescr = Constants.EMPTY;
		backgroundId = Constants.INVALID;
	}
	
	public ShallowMemeType(final LvMemeType lvMemeType) {
		this();
		
		typeId = lvMemeType.getId();
		typeDescr = lvMemeType.getDescr();
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getTypeDescr() {
		return typeDescr;
	}

	public void setTypeDescr(String typeDescr) {
		this.typeDescr = typeDescr;
	}

	public int getBackgroundId() {
		return backgroundId;
	}

	public void setBackgroundId(int backgroundId) {
		this.backgroundId = backgroundId;
	}
	
	

}
