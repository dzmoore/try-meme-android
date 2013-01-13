package com.eastapps.meme_gen_android.mgr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.util.Log;

import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CacheMgr {
	private static final String TAG = CacheMgr.class.getSimpleName();
	private static CacheMgr instance;
	private Map<String, Object> installMap;
	private Set<String> unconvertedKeys;
	private Context context;
	
	private CacheMgr(final Context context) {
		super();

		this.context = context;
		unconvertedKeys = new HashSet<String>();
		
		initInstallMap();
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
		return installMap != null && installMap.containsKey(key);
	}
	
	private void readMapFromInstallFile(final File installation) {
		RandomAccessFile raf = null;
		byte[] bytes = new byte[0];
		
		try {
			// open file and read into 'bytes' array
			raf = new RandomAccessFile(installation, "r");
			bytes = new byte[(int) installation.length()];
			raf.readFully(bytes);
			
		} catch (Exception e) {
			Log.e(TAG, "err while attempting to read install file", e);
			
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (Exception e) { }
			}
		} 
		
		if (bytes != null && bytes.length > 0) {
			// get the user from the install map
			final String jsonInstallMap = new String(bytes);
			
			this.installMap = (Map<String, Object>)new Gson().fromJson(jsonInstallMap, Map.class);
			
			unconvertedKeys.clear();
			unconvertedKeys.addAll(installMap.keySet());
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
			installMap = new HashMap<String, Object>();
			unconvertedKeys.clear();
		}
	}

	private File getInstallFile() {
		return new File(context.getFilesDir(), Constants.INSTALL_FILE);
	}
	
	public synchronized void addToCache(final String key, final Object val) {
		installMap.put(key, val);
		
		if (unconvertedKeys.contains(key)) {
			unconvertedKeys.remove(key);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getListFromCache(final String key, final Class<T> itemType) {
		List<T> resultObj = null;
		
		if (installMap.containsKey(key)) {
			
			final Object object = installMap.get(key);
			
			if (unconvertedKeys.contains(key)) {
				try {
					Type listType = new TypeToken<Collection<T>>(){}.getType();
					resultObj = new Gson().fromJson(new Gson().toJson(object), listType);
					
					int i = 0;
					for (final T ea : resultObj) {
						resultObj.set(i++, new Gson().fromJson(new Gson().toJson(ea), itemType));
					}
					
					installMap.put(key, resultObj);
					unconvertedKeys.remove(key);
					
				} catch (Exception e) {
					Log.e(TAG, "err", e);
				}
				
			} else {
				if (List.class.isAssignableFrom(object.getClass())) {
					try {
						resultObj = (List<T>)object;
						
					} catch (Exception e) {
						Log.e(TAG, "err", e);
					}
				}
			}
		}
		
		return resultObj;
	}
	
	public <T> T getFromCache(final String key, final Class<T> type) {
		T resultObj = null;
		
		if (installMap.containsKey(key)) {
			
			final Object object = installMap.get(key);
			
			if (unconvertedKeys.contains(key)) {
				try {
					resultObj = new Gson().fromJson(new Gson().toJson(object), type);
					
					installMap.put(key, resultObj);
					unconvertedKeys.remove(key);
					
				} catch (Exception e) {
					Log.e(TAG, "err", e);
					
				}
				
			} else {
				if (type.isAssignableFrom(object.getClass())) {
					resultObj = type.cast(object);
				}
			}
		}
		
		return resultObj;
	}
	
	public void storeCacheToFile() {
		writeMapIntoInstallFile(getInstallFile());
	}
	
	private void writeMapIntoInstallFile(final File installation) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(installation);
			
			// write the map to the install file
			out.write(new Gson().toJson(installMap).getBytes());
			out.flush();
			
		} catch (Exception e) {
			Log.e(TAG, "err occurred while writing install file", e);
			
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) { }
			}
		}
	}
	
}




















