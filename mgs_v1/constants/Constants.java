package com.eastapps.meme_gen_server.constants;

import org.springframework.beans.factory.annotation.Autowired;

public class Constants {
    public static final int INVALID = -1;
    public static final String TOP = "TOP";
    public static final String BOTTOM = "BOTTOM";
	public static final String EMPTY = "";
	public static final String EMPTY_STRING = EMPTY;
	
	@Autowired
	public static String MGS_DB_NAME = "mgsdb";
	
	public static String getDbName() {
		return MGS_DB_NAME;
	}
}
