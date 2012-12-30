package com.eastapps.meme_gen_android.widget.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.domain.MemeListItemData;
import com.eastapps.meme_gen_android.widget.ResizableImageView;

public class MemeListAdapter extends ArrayAdapter<MemeListItemData> {
	private List<MemeListItemData> items;
	private LayoutInflater inflater;

	public MemeListAdapter(Context context, int resource,
			int textViewResourceId, List<MemeListItemData> objects) {
		super(context, resource, textViewResourceId, objects);

		init(objects);
	}

	public MemeListAdapter(Context context, int resource,
			int textViewResourceId, MemeListItemData[] objects) {
		super(context, resource, textViewResourceId, objects);

		init(objects);
	}

	public MemeListAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId);
		
		init(new ArrayList<MemeListItemData>());
	}

	public MemeListAdapter(Context context, int textViewResourceId,
			List<MemeListItemData> objects) {
		super(context, textViewResourceId, objects);
		
		init(objects);
	}


	public MemeListAdapter(Context context, int textViewResourceId,
			MemeListItemData[] objects) {
		super(context, textViewResourceId, objects);
		
		init(objects);
	}
	
	private void init() {
		if (items == null) {
			items = new ArrayList<MemeListItemData>();
		}
		
		
		inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	private void init(List<MemeListItemData> objects) {
		items = objects;
		
		init();
	}

	private void init(MemeListItemData[] objects) {
		items = new ArrayList<MemeListItemData>();
		Collections.addAll(items, objects);
		
		init();
	}

	public MemeListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final MemeListItemData item = items.get(position);
		
		if (convertView == null || convertView.getId() != R.layout.meme_list_item_layout) {
			convertView = inflater.inflate(R.layout.meme_list_item_layout, parent, false);
		}
		
		final ImageButton heartImgBtn = (ImageButton) convertView.findViewById(R.id.list_item_heart_img_btn);
		// TODO
		
		final ResizableImageView thumbImg = (ResizableImageView) convertView.findViewById(R.id.list_item_thumb_img_view);
		thumbImg.setImageBitmap(item.getThumb());
		
		final TextView descTxtView = (TextView) convertView.findViewById(R.id.list_item_text_view);
		descTxtView.setText(item.getMemeType().getTypeDescr());
		
		
		return convertView;
	}
	
}





























	
	
	
	

