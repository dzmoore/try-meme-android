package com.eastapps.meme_gen_android.widget.fragment;

import com.eastapps.meme_gen_android.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MemeListFilterBarFragment extends Fragment {
	private static final String TAG = MemeListFilterBarFragment.class.getSimpleName();

	public MemeListFilterBarFragment() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.meme_list_filter_bar_layout, container);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}
