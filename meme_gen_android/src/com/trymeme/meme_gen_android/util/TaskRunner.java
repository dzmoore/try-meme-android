package com.trymeme.meme_gen_android.util;

import android.os.AsyncTask;
import com.trymeme.meme_gen_android.R;

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
