package com.eastapps.meme_gen_android.activity;

import java.net.UnknownHostException;
import java.util.Collections;
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
import com.eastapps.meme_gen_android.mgr.MemeTypeFavSaveRemoveHandler;
import com.eastapps.meme_gen_android.mgr.UserMgr;
import com.eastapps.meme_gen_android.service.impl.MemeService;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.util.TaskRunner;
import com.eastapps.meme_gen_android.widget.adapter.MemeListAdapter;
import com.eastapps.meme_gen_android.widget.fragment.MemeListFragment;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.mgs.model.MemeBackground;

public class MemeTypeSearchResultsActivity extends FragmentActivity {
	private List<MemeListItemData> items;
	private MemeListAdapter listAdapter;
	private MemeService memeService;
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
	
		memeService = MemeService.getInstance();	
		memeService.setConnectionExceptionCallback(new ICallback<Exception>() {
			@Override
			public void callback(Exception obj) {
				if (obj instanceof UnknownHostException) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(
								MemeTypeSearchResultsActivity.this, 
								"Unable to connect to server.", 
								Toast.LENGTH_LONG
							).show();
						}
					});
				}
			}
		});
		
		items = Collections.emptyList();
	}
	
	private void initMemeList() {
		TaskRunner.runAsync(new Runnable() {
			@Override
			public void run() {
				final List<MemeBackground> favTypes = UserMgr.getFavMemeTypes(true);

				for (final MemeBackground eaFavType : favTypes) {
					for (final MemeListItemData eaListItem : items) {
						
						if (eaFavType.getId() == eaListItem.getMemeBackground().getId()) {
							eaListItem.setFavorite(true);
							break;
						}
					}
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						initListAdapter();

						if (getResources().getBoolean(R.bool.debug_toasts_enabled)) {
							Toast.makeText(MemeTypeSearchResultsActivity.this,
								"Loading Complete", 
								Toast.LENGTH_SHORT
							).show();
						}
					}
				});
				
				CacheMgr.getInstance().storeCacheToFile();
			}
		});
	}
	
	private void handleHeartBtnClicked(final MemeListItemData item, final ImageButton heartImgBtn) {
		new MemeTypeFavSaveRemoveHandler(this).handle(item, heartImgBtn);
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
				
				intent.putExtra(Constants.KEY_MEME_BACKGROUND, item.getMemeBackground());
				
				startActivity(intent);
			}
		});
		
		final FragmentManager fragMgr = getSupportFragmentManager();
		final MemeListFragment memeListFrag = (MemeListFragment) fragMgr.findFragmentById(R.id.meme_type_search_list_fragment);
		
		memeListFrag.setListAdapter(listAdapter);
		
		isLoadingList.set(false);
	}

	private void processSearch(final String query) {
		TaskRunner.runAsync(new Runnable() {
			@Override
			public void run() {
				final List<MemeListItemData> results = memeService.getAllTypesForSearch(query);
				
				if (results != null && results.size() > 0) {
					items = results;
				}
				
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						initMemeList();
					}
				});
			}
		});
	}

	@Override
	public boolean onSearchRequested() {
		isSearchAction.set(true);
		
		return super.onSearchRequested();
	}

}
