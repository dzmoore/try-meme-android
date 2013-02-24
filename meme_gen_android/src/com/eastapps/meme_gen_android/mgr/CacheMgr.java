package com.eastapps.meme_gen_android.mgr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objenesis.instantiator.ObjectInstantiator;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.eastapps.meme_gen_android.util.Constants;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class CacheMgr {
	private static final String TAG = CacheMgr.class.getSimpleName();
	private static CacheMgr instance;
	private Map<String, Serializable> installMap;
	private Context context;
	
	public CacheMgr(final Context context) {
		super();

		this.context = context;
		
		getInstallFile().delete();
		
		initInstallMap();
	}
	
	public void clearCache() {
		installMap.clear();
	}

	public static synchronized void initialize(final Context context) {
		if (instance == null) {
			instance = new CacheMgr(context);
		}
	}

	public static CacheMgr getInstance() {
		return instance;
	}
	
	public boolean containsKey(final String key) {
		return installMap.containsKey(key);
	}
	
	private void readMapFromInstallFile(final File installation) {
		final Kryo kryo = new Kryo();
		kryo.register(MemeListDataSerializer.class).setInstantiator(new ObjectInstantiator() {
			@Override
			public Object newInstance() {
				return Bitmap.createBitmap(0, 0, null);
			}
		});

		try {
			Input input = new Input(new FileInputStream(installation));
			this.installMap = kryo.readObject(input, ConcurrentHashMap.class);

		} catch (FileNotFoundException e1) {
			Log.e(TAG, "err", e1);
		}
		
	}
	
	public void refreshCacheFromFile() {
		initInstallMap();
	}

	private synchronized void initInstallMap() {
		final File installation = getInstallFile();
		
		if (installation.exists()) {
			readMapFromInstallFile(installation);
		} 
		else {
			installMap = new ConcurrentHashMap<String, Serializable>();
		}
	}

	public File getInstallFile() {
		return new File(context.getFilesDir(), Constants.INSTALL_FILE);
	}
	
	public void addToCache(final String key, final Serializable val) {
		installMap.put(key, val);
	}
	
	public <T> T getFromCache(final String key, final Class<T> type) {
		T resultObj = null;
		
		synchronized (installMap) {
			final Object object = installMap.get(key);
			
			if (object != null && type.isAssignableFrom(object.getClass())) {
				resultObj = type.cast(object);
			}
		}
		
		return resultObj;
	}
	
	public void storeCacheToFile() {
		storeCacheToFile(false);
	}
	
	public void storeCacheToFile(final boolean returnImmediately) {
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				writeMapIntoInstallFile(getInstallFile());
			}
		};
		
		if (returnImmediately) {
			new Thread(runnable).start();
			
		} else {
			runnable.run();
		}
		
	}
	
	private void writeMapIntoInstallFile(final File installation) {
		Kryo kryo = new Kryo();
		kryo.register(MemeListDataSerializer.class).setInstantiator(new ObjectInstantiator() {
			@Override
			public Object newInstance() {
				return Bitmap.createBitmap(0, 0, null);
			}
		});
	
		FileOutputStream out = null;
		Output output = null;
		
		try {
			out = new FileOutputStream(installation);
			
			output = new Output(out);
			
			synchronized (installMap) {
				// write the map to the install file
				kryo.writeObject(output, installMap);
			}
			
		} catch (Exception e) {
			Log.e(TAG, "err occurred while writing install file", e);
			
		} finally {
			if (output != null) {
				try {
					output.close();
					
				} catch (Exception e) {
				}
			}
			
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) { }
			}
		}
	}
	
}




















