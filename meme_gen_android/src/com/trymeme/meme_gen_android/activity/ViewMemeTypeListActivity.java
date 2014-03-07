package com.trymeme.meme_gen_android.activity;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import com.trymeme.meme_gen_android.BuildConfig;
import com.trymeme.meme_gen_android.R;
import com.trymeme.meme_gen_android.domain.MemeListItemData;
import com.trymeme.meme_gen_android.mgr.AdMgr;
import com.trymeme.meme_gen_android.mgr.CacheMgr;
import com.trymeme.meme_gen_android.mgr.ICallback;
import com.trymeme.meme_gen_android.mgr.MemeTypeFavSaveRemoveHandler;
import com.trymeme.meme_gen_android.mgr.UserMgr;
import com.trymeme.meme_gen_android.service.IMemeService;
import com.trymeme.meme_gen_android.service.impl.MemeService;
import com.trymeme.meme_gen_android.util.Constants;
import com.trymeme.meme_gen_android.util.TaskRunner;
import com.trymeme.meme_gen_android.widget.adapter.MemeListAdapter;
import com.trymeme.meme_gen_android.widget.fragment.MemeListFilterBarFragment;
import com.trymeme.meme_gen_android.widget.fragment.MemeListFragment;
import com.eastapps.meme_gen_server.domain.User;
import com.eastapps.mgs.model.Meme;
import com.eastapps.mgs.model.MemeBackground;

public class ViewMemeTypeListActivity extends FragmentActivity {
	private List<MemeListItemData> items;
	private MemeListAdapter listAdapter;
	private IMemeService memeService;
	private AtomicBoolean isLoadingList = new AtomicBoolean(false);
	private AtomicBoolean isViewSet = new AtomicBoolean(false);
	private FilterType currentViewType;
	private AlertDialog eulaAlertDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		currentViewType = FilterType.UNINITIALIZED;
		
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState != null && savedInstanceState.containsKey("eulaAlertDialogShowing")) {
			if (savedInstanceState.getBoolean("eulaAlertDialogShowing")) {
				createEulaAlertDialog(new AtomicBoolean(false));
				eulaAlertDialog.show();
			}
		}
		
		initActivity();
	}
	
	@Override
	protected void onResume() {
		final ICallback<Map<String, Object>> callback = createFilterBarBtnClickListener();
		final FragmentManager fragMgr = getSupportFragmentManager();
		final MemeListFilterBarFragment filterBarFrag = (MemeListFilterBarFragment) fragMgr.findFragmentById(R.id.meme_list_filter_bar_fragment);
		filterBarFrag.setFilterBtnClickedCallback(callback);
		super.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.type_list_view_menu, menu);
		final MenuItem refreshMenuItem = (MenuItem)menu.findItem(R.id.refresh_menu_item);
		refreshMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if (getResources().getBoolean(R.bool.debug_toasts_enabled)) {
					Toast.makeText(
						ViewMemeTypeListActivity.this, 
						"refresh clicked", 
						Toast.LENGTH_SHORT
					).show();
				}
				
				CacheMgr.getInstance().clearCache();
				if (currentViewType == null 
					|| currentViewType == FilterType.UNINITIALIZED) 
				{
					loadPopularTypes();
					
				} else {
					final Map<String, Object> params = new HashMap<String, Object>();
					params.put(Constants.KEY_FILTER_TYPE_ENUM, currentViewType);
					handleFilterBtnClicked(params);
				}
				
				return true;
			}
		});
		
		final MenuItem myMemesMenuItem = (MenuItem)menu.findItem(R.id.my_memes_menu_item);
		myMemesMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if (getResources().getBoolean(R.bool.debug_toasts_enabled)) {
					Toast.makeText(
						ViewMemeTypeListActivity.this, 
						"my memes clicked", 
						Toast.LENGTH_SHORT
					).show();
				}
				
				final Intent intent = new Intent(ViewMemeTypeListActivity.this, MemeTypeSearchResultsActivity.class);
				intent.putExtra(Constants.KEY_MY_MEMES, "");
				
				startActivity(intent);
				
				return true;
			}
		});
		
		return true;
	}
	
	private void initActivity() {
		if (!UserMgr.hasUserAcceptedEula()) {
			final AtomicBoolean doWaitLoop = new AtomicBoolean(false);
			createEulaAlertDialog(doWaitLoop);

            // show it
            eulaAlertDialog.show();
		}
		
		if (getResources().getBoolean(R.bool.debug_toasts_enabled)) {
			final Toast loadingToast = Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT);
			loadingToast.show();
		}
	
		if (isViewSet.compareAndSet(false, true)) {
			setContentView(R.layout.meme_list_layout);
		}
		
		memeService = MemeService.getInstance();
		
		memeService.setConnectionExceptionCallback(new ICallback<Exception>() {
			@Override
			public void callback(Exception obj) {
				if (obj instanceof UnknownHostException) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(
								ViewMemeTypeListActivity.this, 
								"Unable to connect to server.", 
								Toast.LENGTH_LONG
							).show();
						}
					});
				}
			}
		});
		
		items = Collections.emptyList();
		
		initFilterBar();
		
//		TaskRunner.runAsync(new Runnable() {
//			@Override
//			public void run() {
//				AdMgr.getInstance().initAd(ViewMemeTypeListActivity.this, R.id.advertising_banner_view);
//			}
//		});
	}

	private void createEulaAlertDialog(final AtomicBoolean doWaitLoop) {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
		    .setMessage(getString(R.string.eulaDialogMessage))
		    .setCancelable(false)
		    .setPositiveButton(getString(R.string.eulaDialogAccept),new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog,int id) {
		        	UserMgr.setUserAcceptedEula(true);
					synchronized (ViewMemeTypeListActivity.this) {
						doWaitLoop.set(false);
						ViewMemeTypeListActivity.this.notify();
					}
					
					eulaAlertDialog = null;
		        }
		      })
		    .setNegativeButton(getString(R.string.eulaDialogDecline),new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog,int id) {
		        	UserMgr.setUserAcceptedEula(false);
		        	synchronized (ViewMemeTypeListActivity.this) {
						doWaitLoop.set(false);
						ViewMemeTypeListActivity.this.notify();
					}
		        	dialog.cancel();
		        	ViewMemeTypeListActivity.this.finish();
		        	
		        	eulaAlertDialog = null;
		        }
		});
		
		eulaAlertDialog = alertDialogBuilder.create();
	}
	
	private void initFilterBar() {
		if (isLoadingList.compareAndSet(false, true)) {
			try {
				loadPopularTypes();
				
			} finally {
				isLoadingList.set(false);
			}
		}
		
		final ICallback<Map<String, Object>> callback = createFilterBarBtnClickListener();
		final FragmentManager fragMgr = getSupportFragmentManager();
		final MemeListFilterBarFragment filterBarFrag = (MemeListFilterBarFragment) fragMgr.findFragmentById(R.id.meme_list_filter_bar_fragment);
		filterBarFrag.setFilterBtnClickedCallback(callback);
	}

	private ICallback<Map<String, Object>> createFilterBarBtnClickListener() {
		return new ICallback<Map<String,Object>>() {
			@Override
			public void callback(Map<String, Object> params) {
				if (isLoadingList.compareAndSet(false, true)) {
					try {
						handleFilterBtnClicked(params);
					} finally {
						isLoadingList.set(false);
					}
				}
			}

		};
	}
	
	private void handleFilterBtnForBtnId(final int btnId) {
		switch (btnId) {
			case R.id.popular_btn:
				loadPopularTypes();
				break;
				
			case R.id.all_btn:
				loadAllTypes();
				break;
				
			case R.id.fav_btn:
				handleFavBtnClick();
				break;
				
			case R.id.search_btn:
				handleSearchBtnClick();
				break;
				
			default:
				break;
		}
	}
	
	private void handleFilterBtnForEnum(final FilterType type) {
		switch (type) {
			case POPULAR:
				loadPopularTypes();
				break;
				
			case ALL:
				loadAllTypes();
				break;
				
			case FAVORITE:
				handleFavBtnClick();
				break;
				
			case SEARCH:
				handleSearchBtnClick();
				break;
				
			default:
				break;
		}
	}

	private void handleFilterBtnClicked(Map<String, Object> params) {
		if (params == null) {
			if (BuildConfig.DEBUG) {
				Log.e(getClass().getSimpleName(), "error filtering types; params null");
			}
			
			return;
		}
		
		if (params.containsKey(Constants.KEY_FILTER_BTN_ID_CLICKED)) {
			final int btnIdClicked = (Integer) params.get(Constants.KEY_FILTER_BTN_ID_CLICKED);
			handleFilterBtnForBtnId(btnIdClicked);
		
		} else if (params.containsKey(Constants.KEY_FILTER_TYPE_ENUM)) {
			final FilterType filterType = (FilterType) params.get(Constants.KEY_FILTER_TYPE_ENUM);
			handleFilterBtnForEnum(filterType);
		}
		
	}

	private void handleFavBtnClick() {
		TaskRunner.runAsync(new Runnable() {
			@Override
			public void run() {
				final List<MemeBackground> types = memeService.getFavoriteBackgrounds();
				
				if (types != null && types.size() > 0) {
					loadFavTypes(); 
					
				} else {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(
								ViewMemeTypeListActivity.this, 
								R.string.no_favorites_msg, 
								Toast.LENGTH_SHORT
							).show(); 
						}
					});
				}
				
				
			}
		});
		
		
	}
	
	private void loadAllTypes() {
		TaskRunner.runAsync(new Runnable() {
			@Override
			public void run() {
				items = memeService.getAllMemeTypesListData();
				initMemeList();
				currentViewType = FilterType.ALL;
			}
		});
	}

	private void handleSearchBtnClick() {
		onSearchRequested();
	}

	private void loadFavTypes() {
		TaskRunner.runAsync(new Runnable() {
			@Override
			public void run() {
				items = memeService.getAllFavMemeTypesListData();
				initMemeList();
				currentViewType = FilterType.FAVORITE;
			}
		});
	}

	private void loadPopularTypes() {
		TaskRunner.runAsync(new Runnable() {
			@Override
			public void run() {
				items = memeService.getAllPopularTypesListData();
				initMemeList();
				currentViewType = FilterType.POPULAR;
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
				
				intent.putExtra(Constants.KEY_MEME_BACKGROUND, item.getMemeBackground());
				
				startActivity(intent);
			}
		});
		
		final FragmentManager fragMgr = getSupportFragmentManager();
		final MemeListFragment memeListFrag = (MemeListFragment) fragMgr.findFragmentByTag("list_frag");
		
		memeListFrag.setListAdapter(listAdapter);
	}

	private void initMemeList() {
		TaskRunner.runAsync(new Runnable() {
			@Override
			public void run() {
				final List<MemeBackground> favTypes = memeService.getFavoriteBackgrounds();
				
				for (final MemeBackground eaFavType : favTypes) {
					for (final MemeListItemData eaListItem : items) {
						if (eaFavType.getId() == eaListItem.getMemeBackground().getId()) {
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
								
							if (getResources().getBoolean(R.bool.debug_toasts_enabled)) {
								Toast.makeText(
									ViewMemeTypeListActivity.this, 
									"Loading Complete",
									Toast.LENGTH_SHORT
								).show();
							}
						}
							
				});
				
				CacheMgr.getInstance().storeCacheToFile();
			}
		});
		
		new Thread(new Runnable() {
			@Override
			public void run() {
			}
		}).start();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		CacheMgr.getInstance().storeCacheToFile();
		
		outState.putBoolean("eulaAlertDialogShowing", eulaAlertDialog != null ? eulaAlertDialog.isShowing() : false);
		
		super.onSaveInstanceState(outState);
	}
	
	private void handleHeartBtnClicked(final MemeListItemData item, final ImageButton heartImgBtn) {
		new MemeTypeFavSaveRemoveHandler(this).handle(item, heartImgBtn);
	}

	
}

































