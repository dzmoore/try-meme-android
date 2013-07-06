package com.eastapps.meme_gen_android.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Context;

import com.eastapps.meme_gen_android.domain.MemeListItemData;
import com.eastapps.meme_gen_android.domain.MemeViewData;
import com.eastapps.meme_gen_android.mgr.CacheMgr;
import com.eastapps.meme_gen_android.mgr.UserMgr;
import com.eastapps.meme_gen_android.service.IMemeService;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.util.Utils;
import com.eastapps.meme_gen_android.web.IMemeServerClient;
import com.eastapps.meme_gen_android.web.MemeServerClient;
import com.eastapps.meme_gen_android.web.MemeServerClientV2;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.meme_gen_server.domain.ShallowUser;
import com.eastapps.mgs.model.Meme;

public class MemeService implements IMemeService {
	private static final String TAG = MemeService.class.getSimpleName();
	private static MemeService instance;
	
	private IMemeServerClient client;
	private static Context context;
//	private UserMgr userMgr;
	
	private MemeService() {
		super();
		
		client = new MemeServerClientV2(context);
	}
	
	public static synchronized void initialize(Context context) {
		MemeService.context = context;
		instance = new MemeService();
	}
	
	public static synchronized MemeService getInstance() {
		return instance;
	}

	@Override
	public long storeMeme(Meme shallowMeme) {
		return client.storeMeme(shallowMeme);
	}

//	@Override
//	public ShallowMeme getMeme(int id) {
//		final ShallowMeme m = client.getMeme(id);
//		return m;
//	}
	
	@Override
	public synchronized List<ShallowMeme> getSampleMemes(final int typeId) {
		final CacheMgr cacheMgr = CacheMgr.getInstance();
		
		final HashMap<Integer, List<ShallowMeme>> sMap = getSampleMap();
		List<ShallowMeme> samples = null;
		
		final boolean containsKey = sMap.containsKey(typeId);
		if (containsKey) {
			samples = sMap.get(typeId);
			
		} 
		
		if (!containsKey || samples == null || samples.size() == 0) {
			samples = client.getSampleMemes(typeId);
			sMap.put(typeId, samples);
			
			cacheMgr.storeCacheToFile(true);
		}
		
		return samples;
	}
	
	private synchronized HashMap<Integer, List<ShallowMeme>> getSampleMap() {
		final CacheMgr cacheMgr = CacheMgr.getInstance();
		
		HashMap<Integer, List<ShallowMeme>> map = null;
		if (cacheMgr.containsKey(Constants.KEY_SAMPLE_MAP)) {
			map = cacheMgr.getFromCache(Constants.KEY_SAMPLE_MAP, HashMap.class);
			
		} else {
			map = new HashMap<Integer, List<ShallowMeme>>();
			cacheMgr.addToCache(Constants.KEY_SAMPLE_MAP, map);
		}
		
		return map;
	}

	public byte[] getBackgroundBytes(final int typeId) {
		final CacheMgr cacheMgr = CacheMgr.getInstance();
		
		byte[] bytes = new byte[0];
		
		final HashMap<Integer, Object> backgroundMap = getBackgroundMap();
		final boolean containsKey = backgroundMap.containsKey(typeId);
		if (containsKey) {
			bytes = (byte[]) backgroundMap.get(typeId);
			
		} 
		
		if (!containsKey || bytes == null || bytes.length == 0) {
			bytes = Utils.getBytesFromBitmap(client.getBackground(typeId));
			
			if (bytes != null && bytes.length > 0) {
				backgroundMap.put(typeId, bytes);
				cacheMgr.storeCacheToFile(true);
			}
		}
		
		return bytes;
	}
	
	@Override
	public MemeViewData createMemeViewData(int typeId) {
		final MemeViewData dat = new MemeViewData();
		
		if (typeId > 0) {
			final ShallowMemeType type = getType(typeId);
			
			if (type != null) {
				dat.setBackground(Utils.getBitmapFromBytes(getBackgroundBytes(typeId)));
				
				final ShallowMeme meme = new ShallowMeme();
				meme.setUserId(UserMgr.getUser().getId());
				meme.setMemeTypeId(typeId);
				meme.setBackgroundFk(type.getBackgroundId());
				
				dat.setMeme(meme);
			
				dat.setSampleMemes(getSampleMemes(typeId));
			}
		}
		
		return dat;
	}
	
	private void initAllTypesMap(final List<ShallowMemeType> types) {
		final HashMap<Integer, ShallowMemeType> allTypesMap = new HashMap<Integer, ShallowMemeType>();
		
		for (final ShallowMemeType eaType : types) {
			allTypesMap.put(eaType.getTypeId(), eaType);
		}
		
		CacheMgr.getInstance().addToCache(Constants.KEY_ALL_TYPES_MAP, allTypesMap);
	}
	
	private synchronized HashMap<Integer, Object> getBackgroundMap() {
		final CacheMgr cm = CacheMgr.getInstance();
		
		HashMap<Integer, Object> bgMap;
		if (cm.containsKey(Constants.KEY_BACKGROUND_MAP)) {
			bgMap = cm.getFromCache(Constants.KEY_BACKGROUND_MAP, HashMap.class);
			
		} else {
			bgMap = new HashMap<Integer, Object>();
			cm.addToCache(Constants.KEY_BACKGROUND_MAP, bgMap);
		}
		return bgMap;
	}
	
	@Override
	public ShallowMemeType getType(final int typeId) {
		final CacheMgr cacheMgr = CacheMgr.getInstance();
		if (!cacheMgr.containsKey(Constants.KEY_ALL_TYPES_MAP)) {
			getAllMemeTypes();
		}
		
		final Map<Integer, ShallowMemeType> typeMap = cacheMgr.getFromCache(Constants.KEY_ALL_TYPES_MAP, Map.class);
		
		return typeMap.get(typeId);
	}
	
	@Override
	public List<ShallowMemeType> getAllMemeTypes() {
		List<ShallowMemeType> types = new ArrayList<ShallowMemeType>(0);
		
		final boolean containsKey = CacheMgr.getInstance().containsKey(Constants.KEY_ALL_TYPES);
		if (containsKey) {
			types = CacheMgr.getInstance().getFromCache(Constants.KEY_ALL_TYPES, List.class);
		
		} 
		
		if (!containsKey || types == null || types.size() == 0) {
			types = client.getMemeTypes();
			
			CacheMgr.getInstance().addToCache(Constants.KEY_ALL_TYPES, new ArrayList<ShallowMemeType>(types));
			
			initAllTypesMap(types);
		}
		
		return types;
	}
	
	@Override
	public List<MemeListItemData> getAllFavMemeTypesListData() {
		final List<ShallowMemeType> favTypes = UserMgr.getFavMemeTypes(false);
		
		final List<MemeListItemData> listData = populateMemeListItemDataList(favTypes);
		
		return listData;
	}
	
	@Override
	public List<MemeListItemData> getAllPopularTypesListData() {
		final List<ShallowMemeType> types = getPopularTypes();
		final List<MemeListItemData> listData = populateMemeListItemDataList(types);
		
		return listData;
	}
	
	@Override
	public List<MemeListItemData> getAllMemeTypesListData() {
		
		final List<ShallowMemeType> types = getAllMemeTypes();
		
		final List<MemeListItemData> listData = populateMemeListItemDataList(types);
		
		return listData;
	}
	
	@Override
	public List<MemeListItemData> getAllTypesForSearch(final String query) {
		final List<ShallowMemeType> results = getTypesForSearch(query);
		
		return populateMemeListItemDataList(results);
	}
	

	private List<MemeListItemData> populateMemeListItemDataList(
			final List<ShallowMemeType> types) {
		final List<MemeListItemData> listData = new ArrayList<MemeListItemData>(types.size());
		for (int i = 0; i < types.size(); i++) {
			listData.add(new MemeListItemData());
		}
		
		final int corePoolSize = 5;
		final int maxPoolSize = 8;
		
		final BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(
			Math.max(Math.min(types.size(), maxPoolSize), 1));
			
		ThreadPoolExecutor tPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 2000, TimeUnit.MILLISECONDS, workQueue);
		
		int index = 0;
		for (final ShallowMemeType eaType : types) {
			final int finalIndex = index;
			tPool.execute(new Runnable() {
				@Override
				public void run() {
					doPopulateMemeListItemData(
						listData, 
						eaType,
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
			final ShallowMemeType eaType,
			final int finalIndex) {
		final MemeListItemData memeListItemData = new MemeListItemData();
		
		listData.set(finalIndex, memeListItemData);
		memeListItemData.setMemeType(eaType);
		memeListItemData.setThumbBytes(getBackgroundBytes(eaType.getTypeId()));
		
	}
	
//	@Override
//	public ShallowUser getUser(final int userId) {
//		return client.getUserForId(userId);
//	}

	@Override
	public String getNewInstallKey() {
		return client.getNewInstallKey();
	}

	@Override
	public int storeNewUser(final ShallowUser shallowUser) {
		return client.storeNewUser(shallowUser);
	}

	@Override
	public List<ShallowMemeType> getFavMemeTypesForUser(final int userId) {
		return client.getFavMemeTypesForUser(userId);
	}

	@Override
	public boolean storeFavType(int userId, int typeId) {
		return client.storeFavMeme(userId, typeId);
	}

	@Override
	public boolean removeFavType(int userId, int typeId) {
		return client.removeFavMeme(userId, typeId);
	}

	public List<ShallowMemeType> getPopularTypes() {
		return client.getPopularTypes();
	}

	public List<ShallowMemeType> getTypesForSearch(String searchTerm) {
		return client.getTypesForSearch(searchTerm);
	}
	

}






























