package com.eastapps.meme_gen_android.widget.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.eastapps.meme_gen_android.domain.MemeViewData;
import com.eastapps.meme_gen_server.domain.ShallowMeme;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MemePagerFragmentAdapter extends FragmentStatePagerAdapter implements Parcelable {
	private List<MemeViewData> memes;

	public MemePagerFragmentAdapter(FragmentManager fm) {
		super(fm);
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
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(memes.size());
		for (final MemeViewData ea : this.memes) {
			dest.writeSerializable(ea.getMeme());
			dest.writeParcelable(ea.getBackground(), 0);
		}
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
