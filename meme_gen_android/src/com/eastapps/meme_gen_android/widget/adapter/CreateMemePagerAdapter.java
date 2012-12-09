package com.eastapps.meme_gen_android.widget.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
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
import com.eastapps.meme_gen_server.domain.ShallowMeme;

public class CreateMemePagerAdapter extends PagerAdapter implements Parcelable {
	private List<MemeViewData> memes;

	public CreateMemePagerAdapter() {
		super();

		setMemes(Collections.synchronizedList(new ArrayList<MemeViewData>(0)));
	}
	
	public CreateMemePagerAdapter(final Parcel source) {
		this();
		
		final int memeCount = source.readInt();
		
		for (int i = 0; i < memeCount; i++) {
			final MemeViewData memeViewData = new MemeViewData();
			memeViewData.setMeme((ShallowMeme)source.readSerializable());
			memeViewData.setBackground((Bitmap)source.readParcelable(Bitmap.class.getClassLoader()));
			memes.add(memeViewData);
		}
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
	
	public void loadIntoView(final ViewGroup container) {
		for (int i = 0; i < memes.size(); i++) {
			instantiateItem(container, i++);
		}
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
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(memes.size());
		for (final MemeViewData ea : this.memes) {
			dest.writeSerializable(ea.getMeme());
			dest.writeParcelable(ea.getBackground(), 0);
		}
	}
	
	public static final Parcelable.Creator<CreateMemePagerAdapter> CREATOR = new Parcelable.Creator<CreateMemePagerAdapter>() {
		@Override
		public CreateMemePagerAdapter[] newArray(int size) {
			return new CreateMemePagerAdapter[size];
		}
		
		@Override
		public CreateMemePagerAdapter createFromParcel(Parcel source) {
			final CreateMemePagerAdapter adapter = new CreateMemePagerAdapter();
			final int memeCount = source.readInt();
			
			for (int i = 0; i < memeCount; i++) {
				final MemeViewData memeViewData = new MemeViewData();
				memeViewData.setMeme((ShallowMeme)source.readSerializable());
				memeViewData.setBackground((Bitmap)source.readParcelable(Bitmap.class.getClassLoader()));
				adapter.memes.add(memeViewData);
			}
			
			return adapter;
		}
	};
	
	

}
