package com.eastapps.meme_gen_android.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.eastapps.meme_gen_android.widget.fragment.MemeListFilterBarFragment;
import com.eastapps.meme_gen_android.widget.fragment.MemeListFragment;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.util.Conca;

public class ViewMemeTypeListActivity extends FragmentActivity {
	private List<MemeListItemData> items;
	private MemeListAdapter listAdapter;
	private MemeService memeService;
	private UserMgr userMgr;
	private AtomicBoolean isLoadingList = new AtomicBoolean(false);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initActivity();
		
	}

	private void initActivity() {
		final Toast loadingToast = Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT);
		loadingToast.show();
		
		setContentView(R.layout.meme_list_layout);
		
		Ini.t(this);
		
		memeService = MemeService.getInstance();	
		
		items = Collections.emptyList();
		
		if (isLoadingList.compareAndSet(false, true)) {
			loadAllTypes();
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
				handleFilterBtnClicked(params);
			}

		};
	}

	private void handleFilterBtnClicked(Map<String, Object> params) {
		final int btnIdClicked = (Integer) params.get(Constants.KEY_FILTER_BTN_ID_CLICKED);
		
		switch (btnIdClicked) {
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

	private void handleFavBtnClick() {
		final List<ShallowMemeType> types = UserMgr.getFavMemeTypes(true);
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
	
	private void loadAllTypes() {
		items = memeService.getAllMemeTypesListData();
		initMemeList();
	}

	private void handleSearchBtnClick() {
		// TODO Auto-generated method stub
		
	}

	private void loadFavTypes() {
		items = memeService.getAllFavMemeTypesListData();
		initMemeList();
	}

	private void loadPopularTypes() {
		// TODO Auto-generated method stub
		
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
		if (CacheMgr.getInstance().containsKey(Constants.KEY_ALL_TYPE_LIST_DATA)) {
			items = (List<MemeListItemData>)CacheMgr.getInstance().getFromCache(
				Constants.KEY_ALL_TYPE_LIST_DATA, 
				List.class);
			
		} else {
			items = memeService.getAllMemeTypesListData();
			CacheMgr.getInstance().addToCache(Constants.KEY_ALL_TYPE_LIST_DATA, new ArrayList<MemeListItemData>(items));
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
}

































