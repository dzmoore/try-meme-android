package com.eastapps.meme_gen_android.mgr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.eastapps.meme_gen_android.service.impl.MemeService;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.meme_gen_server.domain.ShallowUser;
import com.google.gson.Gson;

public class UserMgr {
	protected static final String TAG = UserMgr.class.getSimpleName();

	private static UserMgr instance;

	private ShallowUser user;
	private MemeService memeSvc;
	private Context context;
	private Map<String, String> installMap;

	private UserMgr(final Context context) {
		super();

		this.context = context;

		MemeService.initialize(context);
		memeSvc = MemeService.getInstance();
	}

	public static synchronized void initialize(final Context context) {
		instance = new UserMgr(context);
	}

	private static UserMgr getInstance() {
		return instance;
	}

	public static synchronized void getUser(
			final ICallback<ShallowUser> setterCallback) {
		final UserMgr inst = getInstance();

		
		if (inst.user == null) {
			final ShallowUser user =
				new Gson().fromJson(
					inst.getInstallMapLazily().get(Constants.INSTALL_KEY_USER),
					ShallowUser.class
			);
			
			inst.user = user;
		}
		
		setterCallback.callback(inst.user);
	}
	
	private synchronized Map<String, String> getInstallMapLazily() {
		if (installMap == null) {
			handleInitializeInstallMap();
		}
		
		return installMap;
	}

	private void handleInitializeInstallMap() {
		final File installation = new File(context.getFilesDir(), Constants.INSTALL_FILE);
		
		/* 
		 * if install file exists, 
		 * load the installMap<String, String> and
		 * then initialize the respective UserMgr fields 
		 * from the installMap<..>
		 */
		if (installation.exists()) {
			readMapFromInstallFile(installation);
			
			loadFieldsFromMap();

		/* no install file exists;
		 * initialize all fields and then 
		 * write each into the installMap<..>;
		 * then, write the installMap<..>
		 * to the install file
		 */
		} else {
			initFieldsIntoMap(installation);
			
			writeMapIntoInstallFile(installation);		
		}
	}

	private void initFieldsIntoMap(final File installation) {
		// no install file, so a new user must be created;
		initUser(installation);
	}

	private void initUser(final File installation) {
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

	private void writeMapIntoInstallFile(final File installation) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(installation);
			
			createNewInstallMapAndAddValues();
			
			// write the map to the install file
			out.write(new Gson().toJson(installMap).getBytes());
			out.flush();
			
		} catch (Exception e) {
			Log.e(TAG, "err occurred while writing install file", e);
			
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) { }
			}
		}
	}

	private void createNewInstallMapAndAddValues() {
		// create the install map and add the user object
		if (installMap == null) {
			this.installMap = new HashMap<String, String>();
		
			addValuesToInstallMap();
		}
	}

	private void readMapFromInstallFile(final File installation) {
		RandomAccessFile raf = null;
		byte[] bytes = new byte[0];
		
		try {
			// open file and read into 'bytes' array
			raf = new RandomAccessFile(installation, "r");
			bytes = new byte[(int) installation.length()];
			raf.readFully(bytes);
			
		} catch (Exception e) {
			Log.e(TAG, "err while attempting to read install file", e);
			
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (Exception e) { }
			}
		} 
		
		if (bytes != null && bytes.length > 0) {
			// get the user from the install map
			final String jsonInstallMap = new String(bytes);
			
			this.installMap = (Map<String, String>)new Gson().fromJson(jsonInstallMap, Map.class);
			
		}
	}

	private void loadFieldsFromMap() {
		if (this.installMap != null) {
			if (this.installMap.containsKey(Constants.INSTALL_KEY_USER)) {
				final Object userObj = installMap.get(Constants.INSTALL_KEY_USER);
				
				final ShallowUser shUserObj = new Gson().fromJson(String.valueOf(userObj), ShallowUser.class);
				
				// set the reference to 'user' field
				this.user = shUserObj;
			}
		}
	}

	private void addValuesToInstallMap() {
		if (user != null) {
			installMap.put(Constants.INSTALL_KEY_USER, new Gson().toJson(user));
		}
	}

	public static void getFavMemeTypes(
			final ICallback<List<ShallowMemeType>> callback) {
		getUser(new ICallback<ShallowUser>() {
			@Override
			public void callback(ShallowUser user) {
				List<ShallowMemeType> types = new ArrayList<ShallowMemeType>(0);

				try {
					types = getInstance().memeSvc.getFavMemeTypesForUser(user
							.getId());

				} catch (Exception e) {
					Log.e(TAG, "err", e);
				}

				callback.callback(types);
			}
		});
	}

}
