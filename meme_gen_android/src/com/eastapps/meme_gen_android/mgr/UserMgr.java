package com.eastapps.meme_gen_android.mgr;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import android.content.Context;
import android.util.Log;

import com.eastapps.meme_gen_android.service.impl.MemeService;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.meme_gen_server.domain.ShallowUser;

public class UserMgr {
	protected static final String TAG = UserMgr.class.getSimpleName();

	private static UserMgr instance;

	private ShallowUser user;
	private List<ShallowMemeType> favTypes;
	

	private UserMgr(final Context context) {
		super();
	}

	public static synchronized void initialize(final Context context) {
		instance = new UserMgr(context);
	}

	private static UserMgr getInstance() {
		return instance;
	}

	public static synchronized void getUserAsync(final ICallback<ShallowUser> setterCallback) {
		final UserMgr inst = getInstance();
		
		if (inst.user == null) {
			final CacheMgr cacheMgrInst = CacheMgr.getInstance();
			if (cacheMgrInst.containsKey(Constants.KEY_USER)) {
				inst.user = cacheMgrInst.getFromCache(Constants.KEY_USER, ShallowUser.class);
				
			} else {
				inst.createNewUser();
				cacheMgrInst.addToCache(Constants.KEY_USER, inst.user);
				cacheMgrInst.storeCacheToFile(true);
			}
		}
		
		setterCallback.callback(inst.user);
	}
	
	private MemeService getMemeService() {
		return MemeService.getInstance();
	}
	
	private void queryForAndInitFavTypes() {
		if (user != null && getMemeService() != null) {
			favTypes = getMemeService().getFavMemeTypesForUser(user.getId());
		}
	}

	private void createNewUser() {
		// get a new install key
		final String installKey = getMemeService().getNewInstallKey();
		
		// set the username and install key to the 
		// new install key from the server
		final ShallowUser newUser = new ShallowUser();
		newUser.setUsername(installKey);
		newUser.setInstallKey(installKey);
		
		// store the new user and check whether the 
		// store was successful
		final int newUserId = getMemeService().storeNewUser(newUser);
		if (newUserId > 0) {
			// store successful;
			// set the user id and set the
			// 'user' field to this new user object
			newUser.setId(newUserId);
			this.user = newUser;
			
		} else {
			Log.w("unable to create new user", new Exception("stack trace only"));
		}
	}
	
	public static synchronized List<ShallowMemeType> getFavMemeTypes(final boolean refresh) {
		final UserMgr inst = getInstance();
		
		if (refresh) {
			inst.queryForAndInitFavTypes();
		} 
			
		if (inst.favTypes == null) {
			final CacheMgr cacheMgrInst = CacheMgr.getInstance();
			if (cacheMgrInst.containsKey(Constants.KEY_FAV_TYPES)) {
				inst.favTypes = (List<ShallowMemeType>)cacheMgrInst.getFromCache(Constants.KEY_FAV_TYPES, List.class);
				
			} else {
				inst.favTypes = inst.getMemeService().getFavMemeTypesForUser(getUser().getId());
			}
		}
		
		return inst.favTypes;
	}

	public static synchronized void getFavMemeTypesAsync(
		final boolean refreshValue, 
		final ICallback<List<ShallowMemeType>> callback) 
	{
		callback.callback(getFavMemeTypes(refreshValue));
	}
	

	public static void saveFavForUser(int typeId, ICallback<Boolean> iCallback) {
		final UserMgr inst = getInstance();
		final ShallowUser u = getUser();
		
		iCallback.callback(inst.getMemeService().storeFavType(u.getId(), typeId));
	}
	
	public static ShallowUser getUser() {
		final AtomicReference<ShallowUser> u = new AtomicReference<ShallowUser>();
		
		getUserAsync(new ICallback<ShallowUser>() {
			@Override
			public void callback(ShallowUser obj) {
				u.set(obj);
			}
		});
		
		return u.get();
	}

	public static void removeFavForUser(int typeId, ICallback<Boolean> iCallback) {
		final UserMgr inst = getInstance();
		final ShallowUser u = getUser();
		
		iCallback.callback(inst.getMemeService().removeFavType(u.getId(), typeId));
	}

}












