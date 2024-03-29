package com.trymeme.meme_gen_android.mgr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.objenesis.instantiator.ObjectInstantiator;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.trymeme.meme_gen_android.BuildConfig;
import com.trymeme.meme_gen_android.util.Constants;
import com.trymeme.meme_gen_android.util.TaskRunner;

public class CacheMgr {
	private static final String TAG = CacheMgr.class.getSimpleName();
	private static CacheMgr instance;
	private Map<String, Serializable> installMap;
	private Context context;
	private AtomicBoolean isStoring;
	private AtomicBoolean additionalStoringWaiting;
	
	public CacheMgr(final Context context) {
		super();

		this.context = context;
		
		isStoring = new AtomicBoolean(false);
		additionalStoringWaiting = new AtomicBoolean(false);
		
//		getInstallFile().delete();
		initInstallMap(); 
	}
	
	public void clearCache() {
		Serializable user = null;
		if (installMap.containsKey(Constants.KEY_USER)) {
			user = installMap.get(Constants.KEY_USER);
		}
		
		installMap.clear();
		
		installMap.put(Constants.KEY_USER, user);
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
		synchronized (installMap) {
			return installMap.containsKey(key);
		}
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

		} catch (Throwable e1) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "err", e1);
			}
			
			installation.delete();
			initInstallMap();
		}
		
	}
	
	public void refreshCacheFromFile() {
		initInstallMap();
	}

	private void initInstallMap() {
		final File installation = getInstallFile();
		
		if (installation.exists()) {
			readMapFromInstallFile(installation);
		} 
		else {
			installMap = Collections.synchronizedMap(new HashMap<String, Serializable>());//new ConcurrentHashMap<String, Serializable>();
		}
	}

	public File getInstallFile() {
		return new File(context.getFilesDir(), Constants.INSTALL_FILE);
	}
	
	public void addToCache(final String key, final Serializable val) {
		if (installMap == null) {
			if (BuildConfig.DEBUG) {
				Log.e(getClass().getSimpleName(), "installMap is null");
			}
			return;
			
		} else if (val == null) {
			if (BuildConfig.DEBUG) {
				Log.e(getClass().getSimpleName(), "val for key " + String.valueOf(key) + " is null");
			}
			return;
			
		} else if (key == null) {
			if (BuildConfig.DEBUG) {
				Log.e(getClass().getSimpleName(), "key is null");
			}
			return;
		}
		
		installMap.put(key, val);
	}
	
	public Object getFromCache(final String key) {
		return installMap.get(key);
	}
	
	public <T> T getFromCache(final String key, final Class<T> type) {
		T resultObj = null;
		
		final Object object = installMap.get(key);
		
		if (object != null && type.isAssignableFrom(object.getClass())) {
			resultObj = type.cast(object);
		}
		
		return resultObj;
	}
	
	public void storeCacheToFile() {
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				doWriteMapIntoInstallFileWithSyncing();
				
			}
		};
		
		TaskRunner.runAsync(runnable);
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
			
			// write the map to the install file
			kryo.writeObject(output, installMap);
			
		} catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "err occurred while writing install file", e);
			}
			
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

	private void doWriteMapIntoInstallFileWithSyncing() {
		if (isStoring.compareAndSet(false, true)) {
			writeMapIntoInstallFile(getInstallFile());
			isStoring.set(false);
			
		} 
		else if (additionalStoringWaiting.compareAndSet(false, true)) {
			while (!isStoring.compareAndSet(false, true)) {
				try {
					Thread.sleep(150L);
				} catch (Exception e) {
					if (BuildConfig.DEBUG) {
						Log.e(CacheMgr.class.getSimpleName(), "error", e);
					}
				}
			}
			
			writeMapIntoInstallFile(getInstallFile());
			
			additionalStoringWaiting.set(false);
			isStoring.set(false);
		}
	}
	
}




















