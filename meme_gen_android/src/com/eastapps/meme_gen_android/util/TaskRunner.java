package com.eastapps.meme_gen_android.util;

import android.os.AsyncTask;

public class TaskRunner {
	public static void runAsync(final Runnable runnable) {
		new AsyncTask<Object, Object, Object>() {
			@Override
			protected Object doInBackground(Object... params) {
				runnable.run();
				return null;
			}
			
		}.execute();
	}
}
