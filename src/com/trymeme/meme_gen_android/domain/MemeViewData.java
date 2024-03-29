package com.trymeme.meme_gen_android.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.eastapps.mgs.model.Meme;
import com.trymeme.meme_gen_android.widget.TagMgr;

public class MemeViewData 
	implements Serializable, Identifiable, Parcelable {
	private static final long serialVersionUID = 7334702044762722957L;
	
	public static final Parcelable.Creator<MemeViewData> CREATOR =
		new Creator<MemeViewData>() {
			@Override
			public MemeViewData[] newArray(int size) {
				return new MemeViewData[size];
			}
			
			@Override
			public MemeViewData createFromParcel(Parcel source) {
				final MemeViewData memeViewData = new MemeViewData();
				memeViewData.setMeme((Meme) source.readSerializable());
				memeViewData.setBackground((Bitmap) source.readParcelable(null));
				memeViewData.setId(source.readInt());
				return memeViewData;
			}
		};
	
	private Meme meme;
	private List<Meme> sampleMemes;
	private Bitmap background;
	private int id;
	
	public MemeViewData() {
		super();
		background = null;
		meme = new Meme();
		sampleMemes = new ArrayList<Meme>(0);
		id = TagMgr.getNextMemeViewId();
	}
	
	public Bitmap getBackground() {
		return background;
	}
	public void setBackground(Bitmap background) {
		this.background = background;
	}

	public Meme getMeme() {
		return meme;
	}

	public void setMeme(Meme meme) {
		this.meme = meme;
	}

	public List<Meme> getSampleMemes() {
		return sampleMemes;
	}

	public void setSampleMemes(List<Meme> sampleMemes) {
		this.sampleMemes = sampleMemes;
	}

	@Override
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeSerializable(meme);
		dest.writeParcelable(background, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeInt(id);
	}
	
	
	
}
