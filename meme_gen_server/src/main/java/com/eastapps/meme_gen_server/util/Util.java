package com.eastapps.meme_gen_server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.eastapps.meme_gen_server.constants.Constants;

public class Util {
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
		if (session != null) {
			if (session.getTransaction() != null) {
				session.getTransaction().commit();
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
