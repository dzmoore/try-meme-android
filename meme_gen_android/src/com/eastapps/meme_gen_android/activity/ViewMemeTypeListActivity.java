package com.eastapps.meme_gen_android.activity;

import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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

public class ViewMemeTypeListActivity extends FragmentActivity {
	private List<MemeListItemData> items;
	private MemeListAdapter listAdapter;
	private MemeService memeService;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
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
		
		final FragmentManager fragMgr = getSupportFragmentManager();
		final MemeListFragment memeListFrag = (MemeListFragment) fragMgr.findFragmentById(R.id.meme_list_fragment);
		
		memeListFrag.setListAdapter(listAdapter);
	}

	private void handleGetFavMemeTypes(List<ShallowMemeType> favTypes) {
		if (CacheMgr.getInstance().containsKey(Constants.INSTALL_KEY_ALL_TYPE_LIST_DATA)) {
			items = CacheMgr.getInstance().getListFromCache(Constants.INSTALL_KEY_ALL_TYPE_LIST_DATA, MemeListItemData.class);
			
		} else {
			items = memeService.getAllMemeTypesListData();
			CacheMgr.getInstance().addToCache(Constants.INSTALL_KEY_ALL_TYPE_LIST_DATA, items);
		}
		
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
				}

		});
	}
}

































