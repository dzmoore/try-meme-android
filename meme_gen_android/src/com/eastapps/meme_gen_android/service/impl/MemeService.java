package com.eastapps.meme_gen_android.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Context;

import com.eastapps.meme_gen_android.domain.MemeListItemData;
import com.eastapps.meme_gen_android.domain.MemeViewData;
import com.eastapps.meme_gen_android.mgr.CacheMgr;
import com.eastapps.meme_gen_android.mgr.ICallback;
import com.eastapps.meme_gen_android.mgr.UserMgr;
import com.eastapps.meme_gen_android.service.IMemeService;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.util.Utils;
import com.eastapps.meme_gen_android.web.IMemeServerClient;
import com.eastapps.meme_gen_android.web.MemeServerClientV2;
import com.eastapps.mgs.model.Meme;
import com.eastapps.mgs.model.MemeBackground;
import com.eastapps.mgs.model.MemeUser;

public class MemeService implements IMemeService {
	private static final String TAG = MemeService.class.getSimpleName();
	private static MemeService instance;
	
	private IMemeServerClient client;
	private static Context context;
	private ICallback<Exception> connectionExceptionCallback;
	
	private MemeService() {
		super();
		
		client = new MemeServerClientV2(context);
		client.setExceptionCallback(new ICallback<Exception>() {
			@Override
			public void callback(Exception obj) {
				if (connectionExceptionCallback != null) {
					connectionExceptionCallback.callback(obj);
				}
			}
		});
	}
	
	public static synchronized void initialize(Context context) {
		MemeService.context = context;
		instance = new MemeService();
	}
	
	public static synchronized MemeService getInstance() {
		return instance;
	}

	@Override
	public long storeMeme(final Meme meme) {
		meme.setCreatedByUser(new MemeUser());
		meme.getCreatedByUser().setId(UserMgr.getUserId());
		return client.storeMeme(meme);
	}

	@Override
	public synchronized List<Meme> getSampleMemes(final long memeBackgroundId) {
		final CacheMgr cacheMgr = CacheMgr.getInstance();
		
		final HashMap<Long, List<Meme>> sMap = getSampleMap();
		List<Meme> samples = null;
		
		final boolean containsKey = sMap.containsKey(memeBackgroundId);
		if (containsKey) {
			samples = sMap.get(memeBackgroundId);
			
		} 
		
		if (!containsKey || samples == null || samples.size() == 0) {
			samples = client.getSampleMemes(memeBackgroundId);
			sMap.put(memeBackgroundId, samples);
			
			cacheMgr.storeCacheToFile(true);
		}
		
		return samples;
	}
	
	private synchronized HashMap<Long, List<Meme>> getSampleMap() {
		final CacheMgr cacheMgr = CacheMgr.getInstance();
		
		HashMap<Long, List<Meme>> map = null;
		if (cacheMgr.containsKey(Constants.KEY_SAMPLE_MAP)) {
			map = cacheMgr.getFromCache(Constants.KEY_SAMPLE_MAP, HashMap.class);
			
		} else {
			map = new HashMap<Long, List<Meme>>();
			cacheMgr.addToCache(Constants.KEY_SAMPLE_MAP, map);
		}
		
		return map;
	}

	public byte[] getBackgroundBytes(final String path) {
		final CacheMgr cacheMgr = CacheMgr.getInstance();
		
		byte[] bytes = new byte[0];
		
		final HashMap<String, Object> backgroundMap = getBackgroundMap();
		final boolean containsKey = backgroundMap.containsKey(path);
		if (containsKey) {
			bytes = (byte[]) backgroundMap.get(path);
			
		} 
		
		if (!containsKey || bytes == null || bytes.length == 0) {
			bytes = Utils.getBytesFromBitmap(client.getBackground(path));
			
			if (bytes != null && bytes.length > 0) {
				backgroundMap.put(path, bytes);
				cacheMgr.storeCacheToFile(true);
			}
		}
		
		return bytes;
	}
	
	@Override
	public MemeViewData createMemeViewData(final MemeBackground memeBackground) {
		final MemeViewData memeViewData = new MemeViewData();
		
		if (memeBackground != null) {
			memeViewData.setBackground(Utils.getBitmapFromBytes(getBackgroundBytes(memeBackground.getFilePath())));
			
			final Meme meme = new Meme();
			meme.setCreatedByUser(UserMgr.getUser());
			meme.setMemeBackground(memeBackground);
			
			memeViewData.setMeme(meme);
			memeViewData.setSampleMemes(getSampleMemes(memeBackground.getId()));
		}
		
		return memeViewData;
	}
	
	private void initAllTypesMap(final List<MemeBackground> memeBackgrounds) {
		final HashMap<Long, MemeBackground> allTypesMap = new HashMap<Long, MemeBackground>();
		
		for (final MemeBackground eaMemeBackground : memeBackgrounds) {
			allTypesMap.put(eaMemeBackground.getId(), eaMemeBackground);
		}
		
		CacheMgr.getInstance().addToCache(Constants.KEY_ALL_MEME_BACKGROUNDS_MAP, allTypesMap);
	}
	
	private synchronized HashMap<String, Object> getBackgroundMap() {
		final CacheMgr cm = CacheMgr.getInstance();
		
		HashMap<String, Object> bgMap;
		if (cm.containsKey(Constants.KEY_BACKGROUND_MAP)) {
			bgMap = cm.getFromCache(Constants.KEY_BACKGROUND_MAP, HashMap.class);
			
		} else {
			bgMap = new HashMap<String, Object>();
			cm.addToCache(Constants.KEY_BACKGROUND_MAP, bgMap);
		}
		return bgMap;
	}
	
	@Override
	public MemeBackground getMemeBackground(final long id) {
		final CacheMgr cacheMgr = CacheMgr.getInstance();
		if (!cacheMgr.containsKey(Constants.KEY_ALL_MEME_BACKGROUNDS_MAP)) {
			getAllMemeBackgrounds();
		}
		
		final Map<Long, MemeBackground> memeBackgroundMap = cacheMgr.getFromCache(Constants.KEY_ALL_MEME_BACKGROUNDS_MAP, Map.class);
		
		return memeBackgroundMap.get(id);
	}
	
	@Override
	public List<MemeBackground> getAllMemeBackgrounds() {
		List<MemeBackground> allMemeBackgrounds = new ArrayList<MemeBackground>(0);
		
		final boolean containsKey = CacheMgr.getInstance().containsKey(Constants.KEY_ALL_TYPES);
		if (containsKey) {
			allMemeBackgrounds = CacheMgr.getInstance().getFromCache(Constants.KEY_ALL_TYPES, List.class);
		
		} 
		
		if (!containsKey || allMemeBackgrounds == null || allMemeBackgrounds.size() == 0) {
			allMemeBackgrounds = client.getAllMemeBackgrounds();
			
			if (allMemeBackgrounds != null) {
				CacheMgr.getInstance().addToCache(Constants.KEY_ALL_TYPES, new ArrayList<MemeBackground>(allMemeBackgrounds));
				
				initAllTypesMap(allMemeBackgrounds);
			}
		}
		
		return allMemeBackgrounds;
	}
	
	@Override
	public List<MemeListItemData> getAllFavMemeTypesListData() {
		final List<MemeBackground> favTypes = UserMgr.getFavMemeTypes(false);
		
		final List<MemeListItemData> listData = populateMemeListItemDataList(favTypes);
		
		return listData;
	}
	
	@Override
	public List<MemeListItemData> getAllPopularTypesListData() {
		final List<MemeBackground> types = getPopularTypes();
		final List<MemeListItemData> listData = populateMemeListItemDataList(types);
		
		return listData;
	}
	
	@Override
	public List<MemeListItemData> getAllMemeTypesListData() {
		
		final List<MemeBackground> types = getAllMemeBackgrounds();
		
		final List<MemeListItemData> listData = populateMemeListItemDataList(types);
		
		return listData;
	}
	
	@Override
	public List<MemeListItemData> getAllTypesForSearch(final String query) {
		final List<MemeBackground> results = findMemeBackgroundsByName(query);
		
		return populateMemeListItemDataList(results);
	}
	

	private List<MemeListItemData> populateMemeListItemDataList(final List<MemeBackground> types) {
		final List<MemeListItemData> listData = new ArrayList<MemeListItemData>(types == null ? 0 : types.size());
		if (types == null) {
			return listData;
		}
		
		for (int i = 0; i < types.size(); i++) {
			listData.add(new MemeListItemData());
		}
		
		final int corePoolSize = 5;
		final int maxPoolSize = 8;
		
		final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
			
		ThreadPoolExecutor tPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 2000, TimeUnit.MILLISECONDS, workQueue);
		
		int index = 0;
		for (final MemeBackground eaMemeBackground : types) {
			final int finalIndex = index;
			tPool.execute(new Runnable() {
				@Override
				public void run() {
					doPopulateMemeListItemData(
						listData, 
						eaMemeBackground,
						finalIndex
					);
				}
			});
			
			index++;
		}
		
		try {
			tPool.shutdown();
			tPool.awaitTermination(2L * 60L * 1000L, TimeUnit.MILLISECONDS);
			
		} catch (Exception e) {
			try {
				tPool.shutdownNow();
			} catch (Throwable t) { }
		}		
		
		return listData;
	}
	
	private void doPopulateMemeListItemData(
			final List<MemeListItemData> listData,
			final MemeBackground eaMemeBackground,
			final int finalIndex) 
	{
		final MemeListItemData memeListItemData = new MemeListItemData();
		
		listData.set(finalIndex, memeListItemData);
		memeListItemData.setMemeBackground(eaMemeBackground);
		memeListItemData.setThumbBytes(getBackgroundBytes(eaMemeBackground.getFilePath()));
		
	}
	
	@Override
	public String getNewInstallKey() {
		return UUID.randomUUID().toString();
	}

	@Override
	public long storeNewUser(final MemeUser shallowUser) {
		return client.storeNewUser(shallowUser);
	}

	@Override
	public List<MemeBackground> getFavMemeBackgroundsForUser(final long userId) {
		return client.getFavMemeBackgroundsForUser(userId);
	}

	@Override
	public boolean storeFavType(final long userId, final long memeBackgroundId) {
		return client.storeFavMemeBackground(userId, memeBackgroundId);
	}

	@Override
	public boolean removeFavType(final long userId, final long memeBackgroundId) {
		return client.removeFavMeme(userId, memeBackgroundId);
	}

	public List<MemeBackground> getPopularTypes() {
		return client.getPopularMemeBackgrounds(Constants.POPULAR_TYPE_NAME);
	}

	public List<MemeBackground> findMemeBackgroundsByName(final String query) {
		return client.getMemeBackgroundsByName(query);
	}

	public ICallback<Exception> getConnectionExceptionCallback() {
		return connectionExceptionCallback;
	}

	@Override
	public void setConnectionExceptionCallback(
			ICallback<Exception> connectionExceptionCallback) {
		this.connectionExceptionCallback = connectionExceptionCallback;
	}


}






























