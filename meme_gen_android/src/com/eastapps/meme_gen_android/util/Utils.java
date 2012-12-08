package com.eastapps.meme_gen_android.util;

import java.util.Collection;

import com.eastapps.meme_gen_server.domain.MemeText;

public class Utils {
	
	public static MemeText getMemeText(final Collection<MemeText> texts, final String textType) {
		MemeText mt = new MemeText();
		
		for (final MemeText eaMt : texts) {
			if (StringUtils.equalsIgnoreCase(eaMt.getTextType(), textType)) {
				mt = eaMt;
				break;
			}
		}
		
		return mt;
	}
}
