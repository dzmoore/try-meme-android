package com.eastapps.meme_gen_server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eastapps.meme_gen_server.constants.Constants;

public class Util {
	private static final Logger logger = LoggerFactory.getLogger(Util.class);
	
	private static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	public static String SHA1(String text) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		MessageDigest md;
		md = MessageDigest.getInstance("SHA-1");
		byte[] sha1hash = new byte[40];
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		sha1hash = md.digest();
		return convertToHex(sha1hash);
	}
	
	public static int getInt(final Integer i) {
		return i == null ? Constants.INVALID : i;
	}
	
	
	public static int getId(final Serializable s) {
		int result = Constants.INVALID;
		
		if (s instanceof Integer) {
			result = (Integer)s;
		}
		
		return result;
	}
	
	public static void commit(final Session session) {
		try {
    		if (session != null) {
    			if (session.getTransaction() != null) {
    				session.getTransaction().commit();
    			}
    		}
    		
		} catch (Exception e) {
			logger.error("error committing session", e);
		}
	}
	
	public static void close(final Session sesh) {
		if (sesh != null) {
			try {
				sesh.close();
			} catch (Throwable t) {
			}
		}
	}
	
	public static void rollback(final Session sesh) {
		if (sesh != null && sesh.getTransaction() != null) {
			try {
				sesh.getTransaction().rollback();
				
			} catch (Throwable t) {
			}
		}
	}
	
	public static String concat(final Object...objs) {
		return StringUtils.join(objs);
	}
	
	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = null;
		byte[] bytes = new byte[0];

		try {
			is = new FileInputStream(file);

			// Get the size of the file
			long length = file.length();

			if (length > Integer.MAX_VALUE) {
				// File is too large
				throw new IOException("File is too large");
			}

			// Create the byte array to hold the data
			bytes = new byte[(int) length];

			// Read in the bytes
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}

			// Ensure all the bytes have been read in
			if (offset < bytes.length) {
				throw new IOException("Could not completely read file " + file.getName());
			}

		} finally {
			if (is != null) {
				// Close the input stream and return bytes
				is.close();
			}
		}

		return bytes;
	}
	
}
