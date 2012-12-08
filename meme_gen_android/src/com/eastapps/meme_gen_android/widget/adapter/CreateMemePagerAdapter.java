package com.eastapps.meme_gen_android.widget.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.domain.MemeViewData;

public class CreateMemePagerAdapter extends PagerAdapter {
	private List<MemeViewData> memes;
	private List<View> memeViews;

	public CreateMemePagerAdapter() {
		super();

		setMemes(Collections.synchronizedList(new ArrayList<MemeViewData>(0)));
		memeViews = Collections.synchronizedList(new ArrayList<View>());
	}

	@Override
	public int getCount() {
		return memes.size();
	}

	public void setMemes(List<MemeViewData> memes) {
		this.memes = Collections.synchronizedList(memes);
	}
	
	public MemeViewData getMemeAt(final int position) {
		return memes.get(position);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		LayoutInflater inflater = (LayoutInflater) container
				.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View newView = null;
	
		if (container instanceof ViewPager) {
			final ViewPager viewPager = (ViewPager)container;
			newView = inflater.inflate(R.layout.meme_view_layout, null);
			
			final MemeViewData mvd = memes.get(position);
			
			((TextView)newView.findViewById(R.id.top_text_textview)).setText(mvd.getMeme().getTopText());
			((ImageView)newView.findViewById(R.id.image_view)).setImageBitmap(mvd.getBackground());

			viewPager.addView(newView, position);
		}


		return newView;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (container instanceof ViewPager) {
			((ViewPager)container).removeViewAt(position);
		}
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return compareViews(arg0, arg1);
	}
	
	private static boolean compareViews(View v, Object o) {
		boolean isEq = false;
		
		if (o instanceof View) {
			isEq = v == (View)o;
		}
		
		return isEq;
	}

	
	@Override
	public Parcelable saveState() {
		return super.saveState();
	}
	
	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
		super.restoreState(state, loader);
	}
	
	

}
