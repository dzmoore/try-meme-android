package com.eastapps.meme_gen_android.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.domain.MemeListItemData;
import com.eastapps.meme_gen_android.mgr.CacheMgr;
import com.eastapps.meme_gen_android.mgr.ICallback;
import com.eastapps.meme_gen_android.mgr.UserMgr;
import com.eastapps.meme_gen_android.service.impl.MemeService;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.widget.adapter.MemeListAdapter;
import com.eastapps.meme_gen_android.widget.fragment.MemeListFragment;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.util.Conca;

public class ViewMemeTypeListActivity extends FragmentActivity {
	private List<MemeListItemData> items;
	private MemeListAdapter listAdapter;
	private MemeService memeService;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		new File(getFilesDir(), Constants.INSTALL_FILE).delete();		
	
		MemeService.initialize(this);
		memeService = MemeService.getInstance();	
		
		CacheMgr.initialize(this);
		
		setContentView(R.layout.meme_list);
		
		items = Collections.emptyList();
		
		final Toast loadingToast = Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT);
		loadingToast.show();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				initListItems();
			}
			
		}).start();
	}

	private void initListItems() {
		UserMgr.initialize(this);
		UserMgr.getFavMemeTypes(true, new ICallback<List<ShallowMemeType>>() {
			@Override
			public void callback(List<ShallowMemeType> favTypes) {
				handleGetFavMemeTypes(favTypes);
			}
		});
	}

	private void initListAdapter() {
		listAdapter = new MemeListAdapter(
			ViewMemeTypeListActivity.this, 
			R.layout.meme_list_item_layout, 
			items
		);
		
		listAdapter.setHeartBtnClickCallback(new ICallback<Map<String,Object>>() {
			@Override
			public void callback(Map<String, Object> obj) {
				final ImageButton heartBtn = (ImageButton) obj.get(Constants.KEY_HEART_BTN);
				final MemeListItemData item = (MemeListItemData) obj.get(Constants.KEY_MEME_TYPE_LIST_ITEM);
				
				handleHeartBtnClicked(item, heartBtn);
			}
		});
		
		listAdapter.setListItemClickCallback(new ICallback<Map<String,Object>>() {
			@Override
			public void callback(Map<String, Object> obj) {
				final MemeListItemData item = (MemeListItemData) obj.get(Constants.KEY_MEME_TYPE_LIST_ITEM);
				
				final Intent intent = new Intent(ViewMemeTypeListActivity.this, CreateMemeActivity.class);
				
				final Bundle bundle = new Bundle();
				intent.putExtra(Constants.KEY_MEME_TYPE, item.getMemeType());
				
				startActivity(intent);
			}
		});
		
		final FragmentManager fragMgr = getSupportFragmentManager();
		final MemeListFragment memeListFrag = (MemeListFragment) fragMgr.findFragmentById(R.id.meme_list_fragment);
		
		memeListFrag.setListAdapter(listAdapter);
	}

	private void handleGetFavMemeTypes(List<ShallowMemeType> favTypes) {
		initItemList();
		
		for (final ShallowMemeType eaFavType : favTypes) {
			for (final MemeListItemData eaListItem : items) {
				if (eaFavType.getTypeId() == eaListItem.getMemeType().getTypeId()) {
					eaListItem.setFavorite(true);
					break;
				}
			}
		}
		
		runOnUiThread(
			new Runnable() {
				@Override
				public void run() {
					initListAdapter();
					
					Toast.makeText(
						ViewMemeTypeListActivity.this, 
						"Loading Complete",
						Toast.LENGTH_SHORT
					).show();
				}

		});
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				CacheMgr.getInstance().storeCacheToFile();
			}
		}).start();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		CacheMgr.getInstance().storeCacheToFile();
		
		super.onSaveInstanceState(outState);
	}
	
	@SuppressWarnings("unchecked")
	private void initItemList() {
		if (CacheMgr.getInstance().containsKey(Constants.INSTALL_KEY_ALL_TYPE_LIST_DATA)) {
			items = (List<MemeListItemData>)CacheMgr.getInstance().getFromCache(
				Constants.INSTALL_KEY_ALL_TYPE_LIST_DATA, 
				List.class);
			
		} else {
			items = memeService.getAllMemeTypesListData();
			CacheMgr.getInstance().addToCache(Constants.INSTALL_KEY_ALL_TYPE_LIST_DATA, new ArrayList<MemeListItemData>(items));
		}
		
	}

	private void handleHeartBtnClicked(final MemeListItemData item, final ImageButton heartImgBtn) {
		final ICallback<Map<String, String>> c = new ICallback<Map<String,String>>() {
			@Override
			public void callback(final Map<String, String> obj) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(
							ViewMemeTypeListActivity.this,
							Conca.t(obj.get("action"), " fav type: ", obj.get("success")),
							Toast.LENGTH_SHORT).show();
					}
				});
			}
		};
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (item.isFavorite()) {
					final Map<String, String> params = new HashMap<String, String>();
					params.put("action", "Removed");
					
					final boolean success = MemeService.getInstance().removeFavType(
						UserMgr.getUserSync().getId(),
						item.getMemeType().getTypeId()
					);
					
					item.setFavorite(false);
					
					if (heartImgBtn != null) {
						runOnUiThread(
							new Runnable() {
								public void run() {
									heartImgBtn.setImageDrawable(getResources().getDrawable(R.drawable.heart_unchecked));
								}
							}
						);
					}
					
					params.put("success", String.valueOf(success));
					
					c.callback(params);
					
				} else {
					final Map<String, String> params = new HashMap<String, String>();
					params.put("action", "Stored");
					
					final boolean success = MemeService.getInstance().storeFavType(
						UserMgr.getUserSync().getId(),
						item.getMemeType().getTypeId()
					);
					
					item.setFavorite(true);
					
					if (heartImgBtn != null) {
						runOnUiThread(
							new Runnable() {
								public void run() {
									heartImgBtn.setImageDrawable(getResources().getDrawable(R.drawable.icon_heart));
								}
							}
						);
					}
					
					params.put("success", String.valueOf(success));
					
					c.callback(params);
				}
			}
		}).start();
	}
}

































