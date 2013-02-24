package com.eastapps.meme_gen_android.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.eastapps.meme_gen_android.domain.MemeListItemData;
import com.eastapps.meme_gen_server.domain.MemeText;

public class Utils {
	private static final String TAG = Utils.class.getSimpleName();
	
	public static <T> T noValue(final T obj, final T defaultValue) {
		T result = defaultValue;
		
		if (obj == null) {
			if (defaultValue == null) {
				Log.w(TAG, "default value is null", new Exception("stack trace only"));
			}
			
		} else {
			result = obj;
		}
		
		return result;
	}
	
	
	public static Bitmap getBitmapFromBytes(final byte[] bytes) {
		final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		Bitmap bm = null;
		
		try {
			bm = BitmapFactory.decodeStream(bais);
			
		} catch (Exception e) {
			Log.e(MemeListItemData.class.getSimpleName(), "Err", e);
		
		} finally {
			if (bais != null) {
				try {
					bais.close();
				} catch (Exception e) { }
			}
		}
		
		return bm;
	}
	
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
