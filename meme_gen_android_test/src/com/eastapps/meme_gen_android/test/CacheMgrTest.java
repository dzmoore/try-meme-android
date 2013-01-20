package com.eastapps.meme_gen_android.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import android.test.AndroidTestCase;

import com.eastapps.meme_gen_android.mgr.CacheMgr;
import com.eastapps.meme_gen_android.service.impl.MemeService;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.meme_gen_server.domain.ShallowUser;

public class CacheMgrTest extends AndroidTestCase {
	@Override
	public void setUp() {
		clearInstallFile();
		CacheMgr.initialize(getContext());
		CacheMgr.getInstance().clearCache();
	}

	private void clearInstallFile() {
		final File installation = new File(getContext().getFilesDir(), Constants.INSTALL_FILE);

		if (installation.exists()) {
			installation.delete();
		}
	}

	@Override
	public void tearDown() {
	}

	public void testAddAndGetFromCache() {
		final int userId = 1;
		final ShallowUser u = new ShallowUser();
		u.setId(userId);

		final ShallowMemeType type1 = new ShallowMemeType();
		type1.setTypeId(1);
		
		final List<ShallowMemeType> favTypes = new ArrayList<ShallowMemeType>();
		favTypes.add(type1);

		CacheMgr instance = CacheMgr.getInstance();
		instance.addToCache(Constants.KEY_USER, u);
		instance.addToCache(Constants.KEY_FAV_TYPES, new ArrayList<ShallowMemeType>(favTypes));

		ShallowUser userFromCacheMgr =
			instance.getFromCache(
				Constants.KEY_USER, 
				ShallowUser.class);

		TestCase.assertNotNull(userFromCacheMgr);
		TestCase.assertTrue(userFromCacheMgr.getId() == userId);
		
		
		List<ShallowMemeType> typeFromCacheMgr = 
			instance.getFromCache(Constants.KEY_FAV_TYPES, List.class);

		TestCase.assertNotNull(typeFromCacheMgr);
		TestCase.assertTrue(typeFromCacheMgr.size() == 1);
		TestCase.assertTrue(typeFromCacheMgr.get(0).getTypeId() == type1.getTypeId());

		instance.storeCacheToFile();
		instance.refreshCacheFromFile();

		userFromCacheMgr =
			instance.getFromCache(
				Constants.KEY_USER, 
				ShallowUser.class);

		TestCase.assertNotNull(userFromCacheMgr);
		TestCase.assertTrue(userFromCacheMgr.getId() == userId);
		
		 typeFromCacheMgr = 
			instance.getFromCache(Constants.KEY_FAV_TYPES, List.class);

		TestCase.assertNotNull(typeFromCacheMgr);
		TestCase.assertTrue(typeFromCacheMgr.size() == 1);
		TestCase.assertTrue(typeFromCacheMgr.get(0).getTypeId() == type1.getTypeId());
	}

}




















