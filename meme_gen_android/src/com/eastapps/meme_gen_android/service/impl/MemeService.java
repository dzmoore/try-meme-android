package com.eastapps.meme_gen_android.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.AsyncTask;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.domain.MemeListItemData;
import com.eastapps.meme_gen_android.domain.MemeViewData;
import com.eastapps.meme_gen_android.mgr.CacheMgr;
import com.eastapps.meme_gen_android.mgr.ICallback;
import com.eastapps.meme_gen_android.mgr.UserMgr;
import com.eastapps.meme_gen_android.service.IMemeService;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.util.TaskRunner;
import com.eastapps.meme_gen_android.util.Utils;
import com.eastapps.meme_gen_android.web.IMemeServerClient;
import com.eastapps.meme_gen_android.web.MemeServerClient;
import com.eastapps.mgs.model.Meme;
import com.eastapps.mgs.model.MemeBackground;
import com.eastapps.mgs.model.MemeUser;
import com.eastapps.util.Conca;

public class MemeService implements IMemeService {
	private static final String TAG = MemeService.class.getSimpleName();
	private static MemeService instance;
	
	private IMemeServerClient client;
	private static Context context;
	private ICallback<Exception> connectionExceptionCallback;
	
	private String backgroundThumbDir;
	
	private MemeService(final Context context2) {
		super();
		
		backgroundThumbDir = context2.getString(R.string.backgroundThumbDir);
		
		client = new MemeServerClient(context);
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
		instance = new MemeService(context);
	}
	
	public static synchronized MemeService getInstance() {
		return instance;
	}

	@Override
	public long storeMeme(final Meme meme) {
		meme.setCreatedByUser(new MemeUser());
		meme.getCreatedByUser().setId(UserMgr.getUserId());
		final long resultId = client.storeMeme(meme);
		
		if (resultId > 0) {
			meme.setId(resultId);
		}
		
		addMyMemeToCache(meme);
		
		return resultId;
	}

	private void addMyMemeToCache(final Meme meme) {
		final CacheMgr cacheMgr = CacheMgr.getInstance();
		final String key = context.getString(R.string.key_my_memes);
		ArrayList<Meme> memesForUser = null;
		if (cacheMgr.containsKey(key)) {
			memesForUser = new ArrayList<Meme>(getArrayListFromCache(key, Meme.class));
			
		} else {
			memesForUser = new ArrayList<Meme>();
		}
		
		memesForUser.add(meme);
		cacheMgr.addToCache(key, memesForUser);
		cacheMgr.storeCacheToFile();
	}

	private <T> ArrayList<T> getArrayListFromCache(final String key, final Class<T> type) {
		final CacheMgr cacheMgr = CacheMgr.getInstance();
		@SuppressWarnings("unchecked")
		ArrayList<T> fromCache = cacheMgr.getFromCache(key, ArrayList.class);
		if (fromCache == null) {
			fromCache = new ArrayList<T>(Collections.EMPTY_LIST);
		}
		return fromCache;
	}

	@Override
	public synchronized List<Meme> getSampleMemes(final long memeBackgroundId) {
		final CacheMgr cacheMgr = CacheMgr.getInstance();
	
		final String key = Conca.t(context.getString(R.string.key_sample_memes), memeBackgroundId);
		ArrayList<Meme> sampleMemes = null;
		if (cacheMgr.containsKey(key)) {
			sampleMemes = new ArrayList<Meme>(getArrayListFromCache(key, Meme.class));
		
		} else {
			sampleMemes = new ArrayList<Meme>(client.getSampleMemes(memeBackgroundId));
		}
		
		cacheMgr.addToCache(key, sampleMemes);
		
		cacheMgr.storeCacheToFile();
		
		return sampleMemes;
	}
	
	public byte[] getThumbBytes(final String path) {
		final String thumbPath = Conca.t(backgroundThumbDir, '/', path);
		final CacheMgr cacheMgr = CacheMgr.getInstance();
		
		byte[] bytes = new byte[0];
		
		final String key = Conca.t(context.getString(R.string.key_thumb), path);
		if (cacheMgr.containsKey(key)) {
			bytes = (byte[]) cacheMgr.getFromCache(key);
			
		} else {
			bytes = Utils.getBytesFromBitmap(client.getBackground(thumbPath));
			cacheMgr.addToCache(key, bytes);
		}
		
		return bytes;
	}

	public byte[] getBackgroundBytes(final String path) {
		final CacheMgr cacheMgr = CacheMgr.getInstance();
		
		byte[] bytes = new byte[0];
		
		final String key = Conca.t(context.getString(R.string.key_background), path);
		if (cacheMgr.containsKey(key)) {
			bytes = (byte[]) cacheMgr.getFromCache(key);
			
		} else {
			bytes = Utils.getBytesFromBitmap(client.getBackground(path));
			cacheMgr.addToCache(key, bytes);
		}
		
		return bytes;
	}
	
	@Override
	public MemeViewData createMemeViewData(final List<Meme> memes) {
		final MemeViewData memeViewData = new MemeViewData();
		
		if (memes != null && memes.size() > 0) {
			final Meme firstMeme = memes.get(0);
			memeViewData.setBackground(Utils.getBitmapFromBytes(getBackgroundBytes(firstMeme.getMemeBackground().getFilePath())));
			
			memeViewData.setMeme(firstMeme);
			
			if (memes.size() > 1) {
				memeViewData.setSampleMemes(new ArrayList<Meme>());
				for (int i = 1; i < memes.size(); i++) {
					memeViewData.getSampleMemes().add(memes.get(i));
				}
			}
		}
		
		return memeViewData;
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
		final List<MemeBackground> favTypes = getFavoriteBackgrounds();
		
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
			
		final ThreadPoolExecutor tPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 2000, TimeUnit.MILLISECONDS, workQueue);
		
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
		memeListItemData.setThumbBytes(getThumbBytes(eaMemeBackground.getFilePath()));
		
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
	public boolean storeFavType(final long userId, final long memeBackgroundId) {
		final CacheMgr cacheMgr = CacheMgr.getInstance();
		
		final boolean storeSuccess = client.storeFavMemeBackground(userId, memeBackgroundId);
		
		if (storeSuccess) {
			TaskRunner.runAsync(new Runnable() {
				@Override
				public void run() {
					final String key = context.getString(R.string.fav_backgrounds);
					cacheMgr.addToCache(key, new ArrayList<MemeBackground>(client.getFavMemeBackgroundsForUser(UserMgr.getUserId())));
					cacheMgr.storeCacheToFile();
				}
			});
		}
		
		return storeSuccess;
	}

	@Override
	public boolean removeFavType(final long userId, final long memeBackgroundId) {
		final CacheMgr cacheMgr = CacheMgr.getInstance();
		
		final boolean removeSuccess = client.removeFavMeme(userId, memeBackgroundId);
		
		if (removeSuccess) {
			final String key = context.getString(R.string.fav_backgrounds);
			ArrayList<MemeBackground> favBackgrounds = null;
			if (cacheMgr.containsKey(key)) {
				favBackgrounds = new ArrayList<MemeBackground>(getArrayListFromCache(key, MemeBackground.class));
				
				for (final Iterator<MemeBackground> itr = favBackgrounds.iterator(); itr.hasNext();) {
					final MemeBackground ea = itr.next();
					if (ea.getId() == memeBackgroundId) {
						itr.remove();
					}
				}
				cacheMgr.addToCache(key, favBackgrounds);
				cacheMgr.storeCacheToFile();
			} 
		}
		
		return removeSuccess;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MemeBackground> getPopularTypes() {
		final CacheMgr cacheMgr = CacheMgr.getInstance();
		
		final String key = context.getString(R.string.key_popular_backgrounds);
		ArrayList<MemeBackground> backgrounds = null;
		if (cacheMgr.containsKey(key)) {
			backgrounds = getArrayListFromCache(key, MemeBackground.class);
			
		} else {
			final List<MemeBackground> popularMemeBackgrounds = client.getPopularMemeBackgrounds(context.getString(R.string.popular_type_name));
			backgrounds = new ArrayList<MemeBackground>(popularMemeBackgrounds == null ? Collections.EMPTY_LIST : popularMemeBackgrounds);
			cacheMgr.addToCache(key, backgrounds);
			cacheMgr.storeCacheToFile();
		}
		
		return backgrounds;
	}

	@Override
	public List<MemeBackground> findMemeBackgroundsByName(final String query) {
		return client.getMemeBackgroundsByName(query);
	}
	
	@Override
	public List<Meme> getMemesForUser(final long userId) {
		final CacheMgr cacheMgr = CacheMgr.getInstance();
		
		final String key = context.getString(R.string.key_my_memes);
		ArrayList<Meme> memes = null;
		if (cacheMgr.containsKey(key)) {
			memes = getArrayListFromCache(key, Meme.class);
			
		} else {
			final List<Meme> memesForUser = client.getMemesForUser(userId);
			memes = new ArrayList<Meme>(memesForUser == null ? Collections.EMPTY_LIST : memesForUser);
			cacheMgr.addToCache(key, memes);
			cacheMgr.storeCacheToFile();
		}
		
		return memes;
	}

	public ICallback<Exception> getConnectionExceptionCallback() {
		return connectionExceptionCallback;
	}

	@Override
	public void setConnectionExceptionCallback(
			ICallback<Exception> connectionExceptionCallback) {
		this.connectionExceptionCallback = connectionExceptionCallback;
	}

	@Override
	public List<MemeBackground> getFavoriteBackgrounds() {
		final String key = context.getString(R.string.fav_backgrounds);
		
		final CacheMgr cacheMgr = CacheMgr.getInstance();
		ArrayList<MemeBackground> favBackgrounds = null;
		if (cacheMgr.containsKey(key)) {
			favBackgrounds = getArrayListFromCache(key, MemeBackground.class);
			
		} else {
			favBackgrounds = new ArrayList<MemeBackground>(client.getFavMemeBackgroundsForUser(UserMgr.getUserId()));
			cacheMgr.addToCache(key, favBackgrounds);
			cacheMgr.storeCacheToFile();
		}
		
		return favBackgrounds;
	}


}






























