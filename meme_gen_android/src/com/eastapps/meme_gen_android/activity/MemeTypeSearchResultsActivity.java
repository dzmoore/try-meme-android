package com.eastapps.meme_gen_android.activity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.SearchManager;
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
import com.eastapps.meme_gen_android.mgr.Ini;
import com.eastapps.meme_gen_android.mgr.UserMgr;
import com.eastapps.meme_gen_android.service.impl.MemeService;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.widget.adapter.MemeListAdapter;
import com.eastapps.meme_gen_android.widget.fragment.MemeListFragment;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.util.Conca;

public class MemeTypeSearchResultsActivity extends FragmentActivity {
	private List<MemeListItemData> items;
	private MemeListAdapter listAdapter;
	private MemeService memeService;
	private UserMgr userMgr;
	private AtomicBoolean isLoadingList = new AtomicBoolean(false);
	private AtomicBoolean isSearchAction = new AtomicBoolean(false);
	private AtomicBoolean isViewSet = new AtomicBoolean(false);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			isSearchAction.set(true);
			
			initActivity();
			
			final String query = intent.getStringExtra(SearchManager.QUERY);
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					processSearch(query);
				}
			}).start();
			
		} else {
			isSearchAction.set(false);
			
			initActivity();
		}
	}
	
	private void initActivity() {
		if (isViewSet.compareAndSet(false, true)) {
			setContentView(R.layout.meme_type_search_list_layout);
		}
	
		Ini.t(this);
		
		memeService = MemeService.getInstance();	
		
		items = Collections.emptyList();
	}
	
	private void initMemeList() {
		final List<ShallowMemeType> favTypes = UserMgr.getFavMemeTypes(true);
		
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
						MemeTypeSearchResultsActivity.this, 
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
	
	private void handleHeartBtnClicked(final MemeListItemData item, final ImageButton heartImgBtn) {
		final ICallback<Map<String, String>> c = new ICallback<Map<String,String>>() {
			@Override
			public void callback(final Map<String, String> obj) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(
							MemeTypeSearchResultsActivity.this,
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
						UserMgr.getUser().getId(),
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
						UserMgr.getUser().getId(),
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
	
	private void initListAdapter() {
		listAdapter = new MemeListAdapter(
			MemeTypeSearchResultsActivity.this, 
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
				
				final Intent intent = new Intent(MemeTypeSearchResultsActivity.this, CreateMemeActivity.class);
				
				intent.putExtra(Constants.KEY_MEME_TYPE, item.getMemeType());
				
				startActivity(intent);
			}
		});
		
		final FragmentManager fragMgr = getSupportFragmentManager();
		final MemeListFragment memeListFrag = (MemeListFragment) fragMgr.findFragmentById(R.id.meme_type_search_list_fragment);
		
		memeListFrag.setListAdapter(listAdapter);
		
		isLoadingList.set(false);
	}

	private void processSearch(final String query) {
		final List<MemeListItemData> results = memeService.getAllTypesForSearch(query);
		
		if (results != null && results.size() > 0) {
			items = results;
		}
			
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
//				initActivity();
				initMemeList();
			}
		});
	}

	@Override
	public boolean onSearchRequested() {
		isSearchAction.set(true);
		
		return super.onSearchRequested();
	}

}
