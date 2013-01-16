package com.eastapps.meme_gen_android.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.util.Log;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.domain.MemeListItemData;
import com.eastapps.meme_gen_android.domain.MemeViewData;
import com.eastapps.meme_gen_android.json.JSONException;
import com.eastapps.meme_gen_android.json.JSONObject;
import com.eastapps.meme_gen_android.mgr.CacheMgr;
import com.eastapps.meme_gen_android.mgr.UserMgr;
import com.eastapps.meme_gen_android.service.IMemeService;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.util.Utils;
import com.eastapps.meme_gen_android.web.MemeServerClient;
import com.eastapps.meme_gen_android.widget.TagMgr;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.meme_gen_server.domain.ShallowUser;

public class MemeService implements IMemeService {
	private static final String TAG = MemeService.class.getSimpleName();
	private static MemeService instance;
	
	
	private MemeServerClient client;
	private static Context context;
	
	private MemeService() {
		super();
		
		client = new MemeServerClient(context);
	}
	
	public static synchronized void initialize(Context context) {
		if (MemeService.context == null) {
			MemeService.context = context;
			instance = new MemeService();
		}
	}
	
	public static synchronized MemeService getInstance() {
		return instance;
	}

	@Override
	public int storeMeme(ShallowMeme shallowMeme) {
		return client.storeMeme(shallowMeme);
	}

	@Override
	public ShallowMeme getMeme(int id) {
		final ShallowMeme m = client.getMeme(id);
		return m;
	}
	
	@Override
	public List<ShallowMeme> getSampleMemes(final int typeId) {
		return client.getSampleMemes(typeId);
	}

	@Override
	public MemeViewData createMemeViewData(int typeId) {
		final MemeViewData dat = new MemeViewData();
		
		if (typeId > 0) {
			final ShallowMemeType type = getType(typeId);
			
			if (type != null) {
				dat.setBackground(client.getBackground(typeId));
				
				final ShallowMeme meme = new ShallowMeme();
				meme.setUserId(UserMgr.getUserSync().getId());
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
		
		if (CacheMgr.getInstance().containsKey(Constants.KEY_ALL_TYPES)) {
			types = CacheMgr.getInstance().getFromCache(Constants.KEY_ALL_TYPES, List.class);
		
		} else {
			types = client.getMemeTypes();
			
			CacheMgr.getInstance().addToCache(Constants.KEY_ALL_TYPES, new ArrayList<ShallowMemeType>(types));
			
			initAllTypesMap(types);
		}
		
		return types;
	}
	
	@Override
	public List<MemeListItemData> getAllMemeTypesListData() {
		
		final List<ShallowMemeType> types = client.getMemeTypes();
		
		final List<MemeListItemData> listData = new ArrayList<MemeListItemData>(types.size());
		for (int i = 0; i < types.size(); i++) {
			listData.add(new MemeListItemData());
		}
		
		final int corePoolSize = 5;
		final int maxPoolSize = 8;
		
		final BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(Math.min(types.size(), maxPoolSize));
		ThreadPoolExecutor tPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 2000, TimeUnit.MILLISECONDS, workQueue);
		
		final AtomicInteger toProcess = new AtomicInteger(types.size());
		
		int index = 0;
		for (final ShallowMemeType eaType : types) {
			final int finalIndex = index;
			tPool.execute(new Runnable() {
				@Override
				public void run() {
					doPopulateMemeListItemData(
						listData, 
						toProcess, 
						eaType,
						finalIndex
					);
				}
			});
			
			index++;
		}
		
		try {
			synchronized (this) {
				wait();
			}
			
		} catch (InterruptedException e) {
			Log.e(TAG, "error occurred while getting meme type list item data", e);
		}
		
		return listData;
	}
	
	private void doPopulateMemeListItemData(
			final List<MemeListItemData> listData,
			final AtomicInteger toProcess,
			final ShallowMemeType eaType, final int finalIndex) {
		final MemeListItemData memeListItemData = new MemeListItemData();
		
		listData.set(finalIndex, memeListItemData);
		memeListItemData.setMemeType(eaType);
		memeListItemData.setThumbBytes(Utils.getBytesFromBitmap(client.getBackgroundForType(eaType.getTypeId())));
		
		final int newToProcess = toProcess.decrementAndGet();
		
		if (newToProcess == 0) {
			synchronized (MemeService.this) {
				MemeService.this.notify();
			}
		}
	}
	
	@Override
	public ShallowUser getUser(final int userId) {
		return client.getUserForId(userId);
	}

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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
