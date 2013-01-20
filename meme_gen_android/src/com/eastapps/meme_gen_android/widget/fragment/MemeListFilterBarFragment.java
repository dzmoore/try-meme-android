package com.eastapps.meme_gen_android.widget.fragment;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.activity.FilterType;
import com.eastapps.meme_gen_android.mgr.ICallback;
import com.eastapps.meme_gen_android.util.Constants;

public class MemeListFilterBarFragment extends Fragment {
	private static final String TAG = MemeListFilterBarFragment.class.getSimpleName();
	
	private ICallback<Map<String, Object>> filterBtnClickedCallback;

	public MemeListFilterBarFragment() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(
		LayoutInflater inflater, 
		ViewGroup container,
		Bundle savedInstanceState) 
	{
		final View inflated = inflater.inflate(R.layout.meme_list_filter_bar_layout, container);
		
		setClickListener(inflated, R.id.popular_btn);
		setClickListener(inflated, R.id.all_btn);
		setClickListener(inflated, R.id.fav_btn);
		setClickListener(inflated, R.id.search_btn);
		
		
		return inflated;
	}

	private View getBtnFromView(final View inflated, final int btnId) {
		return inflated.findViewById(btnId);
	}
	
	private void setClickListener(final View v, final int btnId) {
		final View btn = getBtnFromView(v, btnId);
		btn.setOnClickListener(createClickListener(v, btn, btnId));
	}

	private OnClickListener createClickListener(
			final View inflated,
			final View popBtn, 
			final int btnId) 
	{
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (filterBtnClickedCallback != null) {
					final HashMap<String, Object> params = new HashMap<String, Object>();
					params.put(Constants.KEY_GUI_OBJECT, popBtn);
					params.put(Constants.KEY_FILTER_BTN_ID_CLICKED, btnId);
					params.put(Constants.KEY_FILTER_BAR_VIEW, inflated);
					
					filterBtnClickedCallback.callback(params);
				}
			}
		};
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public void setFilterBtnClickedCallback(ICallback<Map<String, Object>> filterBtnClickedCallback) {
		this.filterBtnClickedCallback = filterBtnClickedCallback;
	}
}
