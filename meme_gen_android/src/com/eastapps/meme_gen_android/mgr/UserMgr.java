package com.eastapps.meme_gen_android.mgr;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.eastapps.meme_gen_android.BuildConfig;
import com.eastapps.meme_gen_android.service.IMemeService;
import com.eastapps.meme_gen_android.service.impl.MemeService;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.mgs.model.MemeBackground;
import com.eastapps.mgs.model.MemeUser;

public class UserMgr {
	protected static final String TAG = UserMgr.class.getSimpleName();

	private static UserMgr instance;

	private MemeUser user;
	private List<MemeBackground> favTypes;
	

	private UserMgr(final Context context) {
		super();
	}

	public static synchronized void initialize(final Context context) {
		instance = new UserMgr(context);
	}

	private static UserMgr getInstance() {
		return instance;
	}

	private IMemeService getMemeService() {
		return MemeService.getInstance();
	}
	
	private void queryForAndInitFavTypes() {
		if (user != null && getMemeService() != null) {
			favTypes = getMemeService().getFavMemeBackgroundsForUser(user.getId());
		}
	}

	private void createNewUser() {
		final String installKey = getMemeService().getNewInstallKey();
		
		final MemeUser newUser = new MemeUser();
		newUser.setUsername(installKey);
		newUser.setActive(true);
		
		final long newUserId = getMemeService().storeNewUser(newUser);
		if (newUserId > 0) {
			// store successful;
			// set the user id and set the
			// 'user' field to this new user object
			newUser.setId(newUserId);
			this.user = newUser;
			
		} else if (BuildConfig.DEBUG) {
			Log.w(TAG, "unable to create new user (id invalid)");
		}
	}
	
	public static synchronized List<MemeBackground> getFavMemeTypes(final boolean refresh) {
		final UserMgr inst = getInstance();
		
		if (refresh) {
			inst.queryForAndInitFavTypes();
		} 
			
		if (inst.favTypes == null) {
			final CacheMgr cacheMgrInst = CacheMgr.getInstance();
			if (cacheMgrInst.containsKey(Constants.KEY_FAV_TYPES)) {
				
				@SuppressWarnings("unchecked")
				final List<MemeBackground> fromCache 
					= (List<MemeBackground>)cacheMgrInst.getFromCache(Constants.KEY_FAV_TYPES, List.class);
				
				inst.favTypes = fromCache;
				
			} else {
				inst.favTypes = inst.getMemeService().getFavMemeBackgroundsForUser(getUser().getId());
			}
		}
		
		return inst.favTypes;
	}

	public static MemeUser getUser() {
		final UserMgr inst = getInstance();
		
		if (inst.user == null) {
			final CacheMgr cacheMgrInst = CacheMgr.getInstance();
			if (cacheMgrInst.containsKey(Constants.KEY_USER)) {
				inst.user = cacheMgrInst.getFromCache(Constants.KEY_USER, MemeUser.class);
				
			} else {
				inst.createNewUser();
				cacheMgrInst.addToCache(Constants.KEY_USER, inst.user);
				cacheMgrInst.storeCacheToFile(true);
			}
		}
		
		return inst.user;
	}

}












