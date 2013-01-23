package com.eastapps.meme_gen_android.mgr;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;

import com.eastapps.meme_gen_android.service.impl.MemeService;
import com.eastapps.meme_gen_android.util.Constants;

public class Ini {
	private static AtomicBoolean inited = new AtomicBoolean(false);  
	
	public static void t(final Context context) {
		if (inited.compareAndSet(false, true)) {
//			new File(context.getFilesDir(), Constants.INSTALL_FILE).delete();		
			
			CacheMgr.initialize(context);
			UserMgr.initialize(context);
			MemeService.initialize(context);
		}
	}
}
