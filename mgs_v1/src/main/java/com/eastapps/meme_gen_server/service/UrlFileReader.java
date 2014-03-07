package com.eastapps.meme_gen_server.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UrlFileReader implements IMemeFileReader {
	@Autowired
	private String urlBase;

	private int readChunkSizeBytes;
	
	private static final Logger logger = LoggerFactory.getLogger(UrlFileReader.class);
	
	public UrlFileReader() {
		super();
		
		urlBase = StringUtils.EMPTY;
		
		readChunkSizeBytes = 4096;
	}
	
	public UrlFileReader(final int readChunkSizeBytes) {
		this();
		
		this.readChunkSizeBytes = readChunkSizeBytes;
	}
	
	@Override
	public byte[] getBytes(final String addr) {
		final String fullUrl = StringUtils.join(new Object[] {
			urlBase, "/", addr
		});
		
		logger.debug(StringUtils.join(new Object[] {
			"attempting to use url [", fullUrl, "]"
		}));
		
		URL u = null;
		try {
			u = new URL(fullUrl);
			
		} catch (MalformedURLException e1) {
			logger.error("err", e1);
		}
		
		byte[] fileBytes = new byte[0];
		if (fullUrl != null) {
    		logger.debug(StringUtils.join(new Object[] {
    			"attempting to retrieve bytes from [", fullUrl, "]"
    		}));
    		
    		final ByteArrayOutputStream bais = new ByteArrayOutputStream();
    		InputStream is = null;
    		try {
    		  is = u.openStream();
    		  byte[] byteChunk = new byte[readChunkSizeBytes]; 
    		  int n;
    
    		  while ( (n = is.read(byteChunk)) > 0 ) {
    		    bais.write(byteChunk, 0, n);
    		  }
    		  
    		  fileBytes = bais.toByteArray();
    		  
    		} catch (IOException e) {
    		  logger.error(
    			  StringUtils.join(new Object[] {
            		  "Failed while reading bytes from ", fullUrl}), 
        		  e
    		  );
    		  
    		} finally {
    		  if (is != null) { 
    			  try { is.close(); } catch (IOException e) { } 
    		  }
    		}
    		
		} else {
			logger.warn("failed to open url stream");
			
		}
		
		return fileBytes;
	}

	public String getUrlBase() {
		return urlBase;
	}

	public void setUrlBase(String urlBase) {
		this.urlBase = urlBase;
	}
}







































