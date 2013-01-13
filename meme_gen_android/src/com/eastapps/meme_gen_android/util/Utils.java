package com.eastapps.meme_gen_android.util;

import java.io.ByteArrayOutputStream;
import java.util.Collection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_server.domain.MemeText;

public class Utils {
	
	public static byte[] getBytesFromBitmap(final Bitmap bm) {
		byte[] bytes = new byte[0];
		
		if (bm != null) {
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				bm.compress(CompressFormat.JPEG, 100, baos);
				baos.flush();
				
				bytes = baos.toByteArray();
				
			} catch (Exception e) {
				Log.e(Utils.class.getSimpleName(), "err", e);
			
			} finally {
				if (baos != null) {
					try {
						baos.close();
					} catch (Exception e) { }
				}
			}
		}
		
		
		return bytes;
	}
	
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
