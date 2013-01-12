package com.eastapps.meme_gen_android.test;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import junit.framework.TestCase;

import android.test.AndroidTestCase;
import android.util.Log;

import com.eastapps.meme_gen_android.mgr.ICallback;
import com.eastapps.meme_gen_android.mgr.UserMgr;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.util.StringUtils;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.meme_gen_server.domain.ShallowUser;

public class UserMgrTest extends AndroidTestCase {
	@Override
	public void setUp() {
		UserMgr.initialize(getContext());
	}

	@Override
	public void tearDown() {
	}
	
	public void testGetUser() {
		final AtomicReference<ShallowUser> user = new AtomicReference<ShallowUser>(null);
		UserMgr.getUser(new ICallback<ShallowUser>() {
			@Override
			public void callback(ShallowUser obj) {
				assertNotNull(obj);
				assertTrue(obj.getId() > 0);
				assertTrue(StringUtils.isNotBlank(obj.getUsername()));
				assertTrue(StringUtils.isNotBlank(obj.getInstallKey()));
				
				user.set(obj);
			}
		});
		
		
		// ensure subsequent calls returns the same user
		UserMgr.getUser(new ICallback<ShallowUser>() {
			@Override
			public void callback(ShallowUser obj) {
				assertNotNull(obj);
				assertTrue(obj.getId() == user.get().getId());
				assertTrue(StringUtils.equals(obj.getInstallKey(), user.get().getInstallKey()));
				assertTrue(StringUtils.equals(obj.getUsername(), user.get().getUsername()));
				
				user.set(obj);
			}
		});
		
		Log.i(UserMgrTest.class.getSimpleName(), "user=" + user.get().toString());
	}
	
	public void testGetFavTypes() {
		clearInstallFile();
		
		final ShallowUser user = getOrCreateUser();
		
		doTestGetFavMemes(false);
	}

	private void doTestGetFavMemes(final boolean secondAttempt) {
		UserMgr.getFavMemeTypes(true, new ICallback<List<ShallowMemeType>>() {
			@Override
			public void callback(List<ShallowMemeType> obj) {
				if (obj == null) {
					if (secondAttempt) {
						TestCase.fail();
						
					} else {
    					final int typeId = 1;
    					doSaveFavType(typeId);
    					doTestGetFavMemes(true);
					}
				}
			}

			private void doSaveFavType(final int typeId) {
				UserMgr.saveFavForUser(
					typeId,
					new ICallback<Boolean>() {
						@Override
						public void callback(Boolean obj) {
							TestCase.assertNotNull(obj);
							TestCase.assertTrue(obj);
							
							
							
						}
					});
			}
		});
	}

	private ShallowUser getOrCreateUser() {
		final AtomicReference<ShallowUser> u = new AtomicReference<ShallowUser>();
		UserMgr.getUser(new ICallback<ShallowUser>() {
			@Override
			public void callback(ShallowUser obj) {
				u.set(obj);
			}
		});
		
		return u.get();
	}
	
	public void testSaveFavType() {
		final int typeId = 1;
		
		clearInstallFile();
		
		final ShallowUser u = getOrCreateUser();
		UserMgr.saveFavForUser(
			typeId,
			new ICallback<Boolean>() {
				@Override
				public void callback(Boolean obj) {
					TestCase.assertNotNull(obj);
					TestCase.assertTrue(obj);
				}
        	});
		
		UserMgr.getFavMemeTypes(true, new ICallback<List<ShallowMemeType>>() {
			@Override
			public void callback(List<ShallowMemeType> obj) {
				TestCase.assertNotNull(obj);
				TestCase.assertTrue(obj.size() == 1);
				
			}
		});
	}
	
	
	private void clearInstallFile() {
		final File installation = new File(getContext().getFilesDir(), Constants.INSTALL_FILE);
		
		if (installation.exists()) {
			installation.delete();
		}
	}
	
	public void testRemoveFavType() {
		final int typeId = 1;
		
		clearInstallFile();
		
		final ShallowUser u = getOrCreateUser();
		UserMgr.saveFavForUser(
			typeId,
			new ICallback<Boolean>() {
				@Override
				public void callback(Boolean obj) {
					TestCase.assertNotNull(obj);
					TestCase.assertTrue(obj);
				}
    	});
		
		UserMgr.getFavMemeTypes(true, new ICallback<List<ShallowMemeType>>() {
			@Override
			public void callback(List<ShallowMemeType> obj) {
				TestCase.assertNotNull(obj);
				TestCase.assertTrue(obj.size() == 1);
				
			}
		});
		
		UserMgr.removeFavForUser(
			typeId,
			new ICallback<Boolean>() {
				@Override
				public void callback(Boolean obj) {
					TestCase.assertNotNull(obj);
					TestCase.assertTrue(obj);
				}
		});
		
		UserMgr.getFavMemeTypes(true, new ICallback<List<ShallowMemeType>>() {
			@Override
			public void callback(List<ShallowMemeType> obj) {
				TestCase.assertNotNull(obj);
				TestCase.assertTrue(obj.size() == 0);
				
			}
		});
	}
}



















