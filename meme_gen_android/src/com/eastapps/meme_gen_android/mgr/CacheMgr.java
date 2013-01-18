package com.eastapps.meme_gen_android.mgr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.strategy.StdInstantiatorStrategy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.eastapps.meme_gen_android.util.Constants;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class CacheMgr {
	private static final String TAG = CacheMgr.class.getSimpleName();
	private static CacheMgr instance;
	private HashMap<String, Serializable> installMap;
//	private Set<String> unconvertedKeys;
	private Context context;
	
	private CacheMgr(final Context context) {
		super();

		this.context = context;
//		unconvertedKeys = new HashSet<String>();
		
		initInstallMap();
	}
	
	public synchronized void clearCache() {
		final CacheMgr inst = getInstance();
		if (inst != null && inst.installMap != null) {
			inst.installMap.clear();
		}
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
		final Kryo kryo = new Kryo();
		kryo.register(MemeListDataSerializer.class).setInstantiator(new ObjectInstantiator() {
			@Override
			public Object newInstance() {
				return Bitmap.createBitmap(0, 0, null);
			}
		});
		
		try {
			Input input = new Input(new FileInputStream(installation));
			this.installMap = kryo.readObject(input, HashMap.class);
			
		} catch (FileNotFoundException e1) {
			Log.e(TAG, "err", e1);
		}
		
//		RandomAccessFile raf = null;
//		byte[] bytes = new byte[0];
//		
//		try {
//			// open file and read into 'bytes' array
//			raf = new RandomAccessFile(installation, "r");
//			bytes = new byte[(int) installation.length()];
//			raf.readFully(bytes);
//			
//		} catch (Exception e) {
//			Log.e(TAG, "err while attempting to read install file", e);
//			
//		} finally {
//			if (raf != null) {
//				try {
//					raf.close();
//				} catch (Exception e) { }
//			}
//		} 
//		
//		if (bytes != null && bytes.length > 0) {
//			// get the user from the install map
//			final String jsonInstallMap = new String(bytes);
//			
//			this.installMap = (Map<String, Serializable>)new Gson().fromJson(jsonInstallMap, Map.class);
//			
////			unconvertedKeys.clear();
////			unconvertedKeys.addAll(installMap.keySet());
//		}
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
			installMap = new HashMap<String, Serializable>();
//			unconvertedKeys.clear();
		}
	}

	private File getInstallFile() {
		return new File(context.getFilesDir(), Constants.INSTALL_FILE);
	}
	
	public synchronized void addToCache(final String key, final Serializable val) {
		installMap.put(key, val);
		
//		if (unconvertedKeys.contains(key)) {
//			unconvertedKeys.remove(key);
//		}
	}
//	
//	@SuppressWarnings("unchecked")
//	public <T> List<T> getListFromCache(final String key, final Class<T> itemType) {
//		List<T> resultObj = null;
//		
//		if (installMap.containsKey(key)) {
//			
//			final Object object = installMap.get(key);
//			
//			if (unconvertedKeys.contains(key)) {
//				try {
//					Type listType = new TypeToken<Collection<T>>(){}.getType();
//					resultObj = new Gson().fromJson(new Gson().toJson(object), listType);
//					
//					int i = 0;
//					for (final T ea : resultObj) {
//						resultObj.set(i++, new Gson().fromJson(new Gson().toJson(ea), itemType));
//					}
//					
//					installMap.put(key, resultObj);
//					unconvertedKeys.remove(key);
//					
//				} catch (Exception e) {
//					Log.e(TAG, "err", e);
//				}
//				
//			} else {
//				if (List.class.isAssignableFrom(object.getClass())) {
//					try {
//						resultObj = (List<T>)object;
//						
//					} catch (Exception e) {
//						Log.e(TAG, "err", e);
//					}
//				}
//			}
//		}
//		
//		return resultObj;
//	}
	
	public <T> T getFromCache(final String key, final Class<T> type) {
		T resultObj = null;
		
		if (installMap.containsKey(key)) {
			
			final Object object = installMap.get(key);
			
//			if (unconvertedKeys.contains(key)) {
//				try {
//					resultObj = new Gson().fromJson(new Gson().toJson(object), type);
//					
//					installMap.put(key, resultObj);
//					unconvertedKeys.remove(key);
//					
//				} catch (Exception e) {
//					Log.e(TAG, "err", e);
//					
//				}
				
//			} else {
				if (type.isAssignableFrom(object.getClass())) {
					resultObj = type.cast(object);
				}
//			}
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
	
	private synchronized void writeMapIntoInstallFile(final File installation) {
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
//			out.write(new Gson().toJson(installMap).getBytes());
//			out.flush();
			
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




















