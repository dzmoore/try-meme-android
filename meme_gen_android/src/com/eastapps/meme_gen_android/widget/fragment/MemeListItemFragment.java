package com.eastapps.meme_gen_android.widget.fragment;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.domain.MemeViewData;
import com.eastapps.meme_gen_android.widget.TagMgr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MemeListItemFragment extends Fragment {
	private MemeViewData meme;
	
	public MemeListItemFragment(final MemeViewData meme) {
		super();
		
		this.meme = meme;
	}
	
	@Override
	public View onCreateView(
		final LayoutInflater inflater, 
		final ViewGroup container,
		final Bundle savedInstanceState) 
	{
		final View inflated = inflater.inflate(R.layout.meme_list_item_layout, container);
		
		final ImageView thumb = (ImageView) inflated.findViewById(R.id.list_item_thumb_img_view);
		thumb.setTag(TagMgr.getNextMemeListItemId());
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
