package com.eastapps.meme_gen_server.hibernate;

import org.hibernate.cfg.ImprovedNamingStrategy;

public class SimpleNamingStrategy extends ImprovedNamingStrategy {
    private static final long serialVersionUID = -5191951571667995133L;

    public SimpleNamingStrategy() {
	super();
    }
    
    @Override
    public String tableName(final String tableName) {
	return tableName;
    }
    

}
