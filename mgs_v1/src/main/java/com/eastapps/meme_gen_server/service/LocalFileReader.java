package com.eastapps.meme_gen_server.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.eastapps.meme_gen_server.util.Util;

public class LocalFileReader implements IMemeFileReader {
	private static final Logger logger = LoggerFactory.getLogger(LocalFileReader.class);
	
	@Autowired
	private String memeImagesRootDir;
	
	public LocalFileReader() {
		super();
		
		memeImagesRootDir = StringUtils.EMPTY;
	}

	@Override
	public byte[] getBytes(final String addr) {
		final String imgPath = StringUtils.join(new Object[]{memeImagesRootDir, File.separator, addr});

		logger.debug(StringUtils.join(new Object[] {"attempting to read image from [", imgPath, "]"}));
		
		byte[] resultBytes = new byte[0];
		final File img = new File(imgPath);
		try {
			 resultBytes = Util.getBytesFromFile(img);
			
		} catch (IOException e) {
			logger.error("err", e);
		}
		
		logger.debug(StringUtils.join(new Object[] {"bytes read [", resultBytes.length, "]"}));
		
		return resultBytes;
	}

}
