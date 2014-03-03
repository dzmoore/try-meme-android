package com.trymeme.meme_gen_android.activity;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.trymeme.meme_gen_android.R;
import com.trymeme.meme_gen_android.domain.MemeListItemData;
import com.trymeme.meme_gen_android.mgr.CacheMgr;
import com.trymeme.meme_gen_android.mgr.ICallback;
import com.trymeme.meme_gen_android.mgr.MemeTypeFavSaveRemoveHandler;
import com.trymeme.meme_gen_android.mgr.UserMgr;
import com.trymeme.meme_gen_android.service.impl.MemeService;
import com.trymeme.meme_gen_android.util.Constants;
import com.trymeme.meme_gen_android.util.TaskRunner;
import com.trymeme.meme_gen_android.widget.adapter.MemeListAdapter;
import com.trymeme.meme_gen_android.widget.fragment.MemeListFragment;
import com.trymeme.util.Conca;
import com.eastapps.mgs.model.Meme;
import com.eastapps.mgs.model.MemeBackground;

public class MemeTypeSearchResultsActivity extends FragmentActivity {
	private List<MemeListItemData> items;
	private MemeListAdapter listAdapter;
	private MemeService memeService;
	private AtomicBoolean isLoadingList = new AtomicBoolean(false);
	private AtomicBoolean isSearchAction = new AtomicBoolean(false);
	private AtomicBoolean isViewSet = new AtomicBoolean(false);
	private Map<Long,ArrayList<Meme>> backgroundIdToMemesMap;

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

		backgroundIdToMemesMap = new HashMap<Long, ArrayList<Meme>>();
		final Intent intent = getIntent();
		if (intent.hasExtra(Constants.KEY_MEMES)) {
			@SuppressWarnings("unchecked")
			final List<Meme> memes = (List<Meme>)intent.getSerializableExtra(Constants.KEY_MEMES);
			loadAndInit(memes);

		} else if (intent.hasExtra(Constants.KEY_MY_MEMES)) {
			TaskRunner.runAsync(new Runnable() {
				@Override
				public void run() {
					final List<Meme> myMemes = MemeService.getInstance().getMemesForUser(UserMgr.getUserId());
					loadAndInit(myMemes);

				}
			});

		} else {
			items = Collections.emptyList();
		}
	}

	private void loadAndInit(final List<Meme> memes) {
		final Map<Long,MemeBackground> backgroundMap = new HashMap<Long,MemeBackground>();
		for (final Meme eaMeme : memes) {
			final Long bgId = eaMeme.getMemeBackground().getId();
			if (!backgroundMap.containsKey(bgId)) {
				backgroundMap.put(bgId, eaMeme.getMemeBackground());

				backgroundIdToMemesMap.put(bgId, new ArrayList<Meme>());
			}

			backgroundIdToMemesMap.get(bgId).add(eaMeme);
		}

		items = new ArrayList<MemeListItemData>();

		for (final MemeBackground eaMemeBackground : backgroundMap.values()) {
			final MemeListItemData eaListItem = new MemeListItemData();
			eaListItem.setMemeBackground(eaMemeBackground);
			eaListItem.setThumbBytes(memeService.getThumbBytes(eaMemeBackground.getFilePath()));
			items.add(eaListItem);
		}

		initMemeList();
	}

	private void initMemeList() {
		final List<MemeBackground> favTypes = memeService.getFavoriteBackgrounds();

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
				if (isSearchAction.get()) {
					final MemeListItemData item = (MemeListItemData) obj.get(Constants.KEY_MEME_TYPE_LIST_ITEM);

					final Intent intent = new Intent(MemeTypeSearchResultsActivity.this, CreateMemeActivity.class);

					intent.putExtra(Constants.KEY_MEME_BACKGROUND, item.getMemeBackground());

					startActivity(intent);

				} else {
					final MemeListItemData item = (MemeListItemData) obj.get(Constants.KEY_MEME_TYPE_LIST_ITEM);
					final ArrayList<Meme> memes = backgroundIdToMemesMap.get(item.getMemeBackground().getId());

					final Intent intent = new Intent(MemeTypeSearchResultsActivity.this, CreateMemeActivity.class);
					intent.putExtra(Constants.KEY_MEMES, memes);

					startActivity(intent);
				}
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
