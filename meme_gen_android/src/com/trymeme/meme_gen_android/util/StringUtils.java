package com.trymeme.meme_gen_android.util;

import com.trymeme.meme_gen_android.R;
import com.trymeme.util.Conca;

public class StringUtils {
	public static final String EMPTY = "";
	
	public static boolean endsWith(String str, String suffix) {
		if (str == null || suffix == null) {
			return (str == null && suffix == null);
		}
		if (suffix.length() > str.length()) {
			return false;
		}
		int strOffset = str.length() - suffix.length();
		return str.regionMatches(false, strOffset, suffix, 0, suffix.length());
	}

	public static boolean startsWith(String str, String prefix) {
		if (str == null || prefix == null) {
			return (str == null && prefix == null);
		}
		if (prefix.length() > str.length()) {
			return false;
		}
		return str.regionMatches(false, 0, prefix, 0, prefix.length());
	}

	public static String trunc(String str, int maxLen) {
		if (str == null) {
			return null;
		}

		if (maxLen <= 3 && str.length() > maxLen) {
			return str.substring(0, maxLen);
		}

		if (maxLen <= 3) {
			return str;
		}

		if (str.length() > maxLen - 3) {
			return Conca.t(str.substring(0, maxLen - 3), "...");
		}

		return str;
	}
	
	public static boolean equalsIgnoreCase(final String str1, final String str2) {
		boolean equal = false;
		
		if (str1 != null && str2 != null) {
			equal = str1.equalsIgnoreCase(str2);
		}
		
		
		return equal;
	}

	public static boolean equals(String str1, String str2) {
		boolean equal = false;
		
		if (str1 != null && str2 != null) {
			equal = str1.compareTo(str2) == 0;
		}
		
		
		return equal;
	}
	
	public static boolean isNotBlank(final String str) {
		boolean isBlank = str == null || str.trim().length() == 0;
		
		return !isBlank;
	}

	public static boolean isBlank(String addr) {
		return addr == null || addr.length() == 0;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}