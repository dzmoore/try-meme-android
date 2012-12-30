package com.eastapps.meme_gen_android.activity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.domain.MemeListItemData;
import com.eastapps.meme_gen_android.service.impl.MemeService;
import com.eastapps.meme_gen_android.widget.adapter.MemeListAdapter;
import com.eastapps.meme_gen_android.widget.fragment.MemeListFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class ViewMemeTypeListActivity extends FragmentActivity {
	private List<MemeListItemData> items;
	private MemeListAdapter listAdapter;
	private MemeService memeService;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MemeService.initialize(this);
		memeService = MemeService.getInstance();	
		
		setContentView(R.layout.meme_list);
		
		items = Collections.emptyList();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				initListItems();
				
			}
		}).start();
	}

	private void initListItems() {
		items = memeService.getAllMemeTypesListData();
		
		runOnUiThread(
			new Runnable() {
				@Override
				public void run() {
					initListAdapter();
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
}

































