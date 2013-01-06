package com.eastapps.meme_gen_android.domain;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.widget.TagMgr;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;

public class MemeListItemData implements Identifiable, Parcelable{
	private Bitmap thumb;
	private int id;
	private ShallowMemeType memeType;
	private boolean isFavorite;
	
	public MemeListItemData() {
		super();
		id = TagMgr.getNextMemeListItemId();
		
		thumb = null;
		memeType = new ShallowMemeType();
		isFavorite = false;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeParcelable(thumb, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeSerializable(memeType);
	}
	
	private static MemeListItemData createFromParcel(final Parcel src) {
		final MemeListItemData mlid = new MemeListItemData();
		mlid.id = src.readInt();
		mlid.thumb = src.readParcelable(Bitmap.class.getClassLoader());
		mlid.memeType = (ShallowMemeType) src.readSerializable();
		
		return mlid;
	}
	
	public static final Parcelable.Creator<MemeListItemData> CREATOR =
		new Creator<MemeListItemData>() {
			@Override
			public MemeListItemData[] newArray(int size) {
				return new MemeListItemData[size];
			}
			
			@Override
			public MemeListItemData createFromParcel(Parcel source) {
				return MemeListItemData.createFromParcel(source);
			}
		};

	@Override
	public int getId() {
		return id;
	}

	public Bitmap getThumb() {
		return thumb;
	}

	public void setThumb(Bitmap thumb) {
		this.thumb = thumb;
	}

	public ShallowMemeType getMemeType() {
		return memeType;
	}

	public void setMemeType(ShallowMemeType memeType) {
		this.memeType = memeType;
	}

	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

}
