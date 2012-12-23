package com.eastapps.meme_gen_android.widget.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.eastapps.meme_gen_android.domain.MemeViewData;
import com.eastapps.meme_gen_android.widget.fragment.MemeViewFragment;

public class MemePagerFragmentAdapter extends FragmentStatePagerAdapter implements Parcelable {
	private List<MemeViewData> memes;
	private List<MemeViewFragment> frags;

	public MemePagerFragmentAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
		
		frags = new ArrayList<MemeViewFragment>();
	}

	@Override
	public int getCount() {
		return memes.size();
	}

	public void setMemes(List<MemeViewData> memes) {
		this.memes = Collections.synchronizedList(memes);
		
		notifyDataSetChanged();
	}
	
	private void reloadFragments() {
		final List<Integer> indexesToUpdate = new ArrayList<Integer>();
		int i = 0;
		for (final MemeViewFragment eaFrag : frags) {
			if (eaFrag != null) {
				indexesToUpdate.add(i);
			}
			i++;
		}
		
		for (final int index : indexesToUpdate) {
			frags.set(index, new MemeViewFragment(memes.get(index)));
		}
	}
	
	@Override
	public void notifyDataSetChanged() {
		reloadFragments();
		
		super.notifyDataSetChanged();
	}
	
	public MemeViewData getMemeAt(final int position) {
		return memes.get(position);
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(memes.size());
		
		synchronized (memes) {
			for (final MemeViewData ea : this.memes) {
				dest.writeSerializable(ea.getMeme());
				dest.writeParcelable(ea.getBackground(), 0);
			}
		}
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Fragment getItem(int position) {
		MemeViewFragment fragment = new MemeViewFragment();
		
		if (position < memes.size()) {
			if (position < frags.size() && frags.get(position) != null) {
				fragment = frags.get(position);
				
			} else {
				fragment = new MemeViewFragment(memes.get(position));
				frags.add(position, fragment);
			}
		}
		
		return fragment;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		
		if (frags.contains(object)) {
			frags.remove(object);
		}
	}
	
	
	
	
	

}
