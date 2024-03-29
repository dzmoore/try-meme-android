package com.trymeme.meme_gen_android.mgr;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.widget.ImageButton;
import android.widget.Toast;

import com.trymeme.meme_gen_android.R;
import com.trymeme.meme_gen_android.domain.MemeListItemData;
import com.trymeme.meme_gen_android.service.impl.MemeService;
import com.trymeme.meme_gen_android.util.TaskRunner;
import com.trymeme.util.Conca;

public class MemeTypeFavSaveRemoveHandler {
	private Activity activity;
	
	public MemeTypeFavSaveRemoveHandler(final Activity activity) {
		super();
		this.activity = activity;
	}
	
	public void handle(final MemeListItemData item,
			final ImageButton heartImgBtn) {
		final ICallback<Map<String, String>> c = new ICallback<Map<String,String>>() {
			@Override
			public void callback(final Map<String, String> obj) {
				if (activity.getResources().getBoolean(R.bool.debug_toasts_enabled)) {
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(
								activity,
								Conca.t(obj.get("action"), " fav type: ", obj.get("success")),
								Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		};
		
		if (heartImgBtn != null) {
			activity.runOnUiThread(
				new Runnable() {
					public void run() {
						if (item.isFavorite()) {
                            heartImgBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.unselected_fav_icon_v2));
                            
						} else {
                            heartImgBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.fav_icon_v2));
						}
						
						TaskRunner.runAsync(new Runnable() {
							@Override
							public void run() {
								if (item.isFavorite()) {
									final Map<String, String> params = new HashMap<String, String>();
									params.put("action", "Removed");
									
									final boolean success = MemeService.getInstance().removeFavType(
										UserMgr.getUserId(),
										item.getMemeBackground().getId()
									);
									
									item.setFavorite(false);
									
									if (heartImgBtn != null) {
										activity.runOnUiThread(
											new Runnable() {
												public void run() {
													heartImgBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.unselected_fav_icon_v2));
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
										item.getMemeBackground().getId()
									);
									
									item.setFavorite(true);
									
									
									
									params.put("success", String.valueOf(success));
									
									c.callback(params);
								}
							}
						});
					}
				}
			);
			
			
		}
		
		
	}
}
