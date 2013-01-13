package com.eastapps.meme_gen_android.mgr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import android.content.Context;
import android.location.Address;
import android.util.Log;

import com.eastapps.meme_gen_android.service.impl.MemeService;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.meme_gen_server.domain.ShallowUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UserMgr {
	protected static final String TAG = UserMgr.class.getSimpleName();

	private static UserMgr instance;

	private ShallowUser user;
	private List<ShallowMemeType> favTypes;
	
	private MemeService memeSvc;

	private UserMgr(final Context context) {
		super();

		MemeService.initialize(context);
		memeSvc = MemeService.getInstance();
	}

	public static synchronized void initialize(final Context context) {
		instance = new UserMgr(context);
	}

	private static UserMgr getInstance() {
		return instance;
	}

	public static synchronized void getUser(final ICallback<ShallowUser> setterCallback) {
		final UserMgr inst = getInstance();
		
		if (inst.user == null) {
			final CacheMgr cacheMgrInst = CacheMgr.getInstance();
			if (cacheMgrInst.containsKey(Constants.INSTALL_KEY_USER)) {
				inst.user = cacheMgrInst.getFromCache(Constants.INSTALL_KEY_USER, ShallowUser.class);
				
			} else {
				inst.createNewUser();
				cacheMgrInst.addToCache(Constants.INSTALL_KEY_USER, inst.user);
			}
		}
		
		setterCallback.callback(inst.user);
	}
	
	private void queryForAndInitFavTypes() {
		if (user != null && memeSvc != null) {
			favTypes = memeSvc.getFavMemeTypesForUser(user.getId());
		}
	}

	private void createNewUser() {
		// get a new install key
		final String installKey = memeSvc.getNewInstallKey();
		
		// set the username and install key to the 
		// new install key from the server
		final ShallowUser newUser = new ShallowUser();
		newUser.setUsername(installKey);
		newUser.setInstallKey(installKey);
		
		// store the new user and check whether the 
		// store was successful
		final int newUserId = memeSvc.storeNewUser(newUser);
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

	@SuppressWarnings("unchecked")
	public static synchronized void getFavMemeTypes(final boolean refreshValue, final ICallback<List<ShallowMemeType>> callback) {
		final UserMgr inst = getInstance();
		
		final ShallowUser user = getUserSync();
		
		if (refreshValue) {
			inst.queryForAndInitFavTypes();
		} 
			
		if (inst.favTypes == null) {
			final CacheMgr cacheMgrInst = CacheMgr.getInstance();
			if (cacheMgrInst.containsKey(Constants.INSTALL_KEY_FAV_TYPES)) {
				inst.favTypes = (List<ShallowMemeType>)cacheMgrInst.getFromCache(Constants.INSTALL_KEY_FAV_TYPES, List.class);
				
			} else {
				inst.favTypes = inst.memeSvc.getFavMemeTypesForUser(user.getId());
//				cacheMgrInst.addToCache(Constants.INSTALL_KEY_FAV_TYPES, inst.favTypes);
			}
		}
					
		
		callback.callback(inst.favTypes);
	}

	public static void saveFavForUser(int typeId, ICallback<Boolean> iCallback) {
		final UserMgr inst = getInstance();
		final ShallowUser u = getUserSync();
		
		iCallback.callback(inst.memeSvc.storeFavType(u.getId(), typeId));
	}
	
	public static ShallowUser getUserSync() {
		final AtomicReference<ShallowUser> u = new AtomicReference<ShallowUser>();
		
		getUser(new ICallback<ShallowUser>() {
			@Override
			public void callback(ShallowUser obj) {
				u.set(obj);
			}
		});
		
		return u.get();
	}

	public static void removeFavForUser(int typeId, ICallback<Boolean> iCallback) {
		final UserMgr inst = getInstance();
		final ShallowUser u = getUserSync();
		
		iCallback.callback(inst.memeSvc.removeFavType(u.getId(), typeId));
	}

}












