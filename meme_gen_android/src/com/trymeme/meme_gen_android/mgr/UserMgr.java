package com.trymeme.meme_gen_android.mgr;

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.eastapps.mgs.model.MemeUser;
import com.trymeme.meme_gen_android.BuildConfig;
import com.trymeme.meme_gen_android.service.IMemeService;
import com.trymeme.meme_gen_android.service.impl.MemeService;
import com.trymeme.meme_gen_android.util.Constants;

public class UserMgr {
	protected static final String TAG = UserMgr.class.getSimpleName();

	private static UserMgr instance;
	private Context context;

	private MemeUser user;
	

	private UserMgr(final Context context) {
		super();
		this.context = context;
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
	
	public static Long getUserId() {
		final MemeUser user = getUser();
		
		return user == null ? -1 : user.getId();
	}
	
	public static boolean hasUserAcceptedEula() {
		boolean retAcceptedEula = false;
		
		final Boolean acceptedEula = CacheMgr.getInstance().getFromCache("acceptedEula", Boolean.class);
		
		if (acceptedEula == null || !acceptedEula) {
			retAcceptedEula = false;
		} else {
			retAcceptedEula = acceptedEula != null ? acceptedEula : false;
		}
		
		return retAcceptedEula;
	}
	
	public static void setUserAcceptedEula(final boolean accepted) {
		CacheMgr.getInstance().addToCache("acceptedEula", Boolean.valueOf(accepted));
		CacheMgr.getInstance().storeCacheToFile();
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
				cacheMgrInst.storeCacheToFile();
			}
		}
		
		return inst.user;
	}

}












