package com.eastapps.meme_gen_android.domain;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.widget.TagMgr;

public class MemeListItemData implements Identifiable, Parcelable{
	private Bitmap thumb;
	private String memeTypeDesc;
	private int memeTypeId;
	private int id;
	
	public MemeListItemData() {
		super();
		id = TagMgr.getNextMemeListItemId();
		
		thumb = null;
		memeTypeDesc = Constants.EMPTY_STRING;
		memeTypeId = Constants.INVALID;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeParcelable(thumb, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(memeTypeDesc);
		dest.writeInt(memeTypeId);
	}
	
	private static MemeListItemData createFromParcel(final Parcel src) {
		final MemeListItemData mlid = new MemeListItemData();
		mlid.id = src.readInt();
		mlid.thumb = src.readParcelable(Bitmap.class.getClassLoader());
		mlid.memeTypeDesc = src.readString();
		mlid.memeTypeId = src.readInt();
		
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

	public String getMemeTypeDesc() {
		return memeTypeDesc;
	}

	public void setMemeTypeDesc(String memeTypeDesc) {
		this.memeTypeDesc = memeTypeDesc;
	}

	public int getMemeTypeId() {
		return memeTypeId;
	}

	public void setMemeTypeId(int memeTypeId) {
		this.memeTypeId = memeTypeId;
	}
	
}
