package com.eastapps.meme_gen_android.domain;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.eastapps.meme_gen_android.util.Utils;
import com.eastapps.meme_gen_android.widget.TagMgr;
import com.eastapps.mgs.model.MemeBackground;

public class MemeListItemData implements Identifiable, Parcelable {
	private transient Bitmap thumb;
	private int id;
	private MemeBackground memeBackground;
	private boolean isFavorite;
	private byte[] thumbBytes;
	
	public MemeListItemData() {
		super();
		id = TagMgr.getNextMemeListItemId();
		
		thumb = null;
		memeBackground = new MemeBackground();
		isFavorite = false;
	}
	
	public void setId(final int id) {
		this.id = id;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeParcelable(thumb, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeSerializable(memeBackground);
	}
	
	private static MemeListItemData createFromParcel(final Parcel src) {
		final MemeListItemData memeListItemData = new MemeListItemData();
		memeListItemData.id = src.readInt();
		memeListItemData.thumb = src.readParcelable(Bitmap.class.getClassLoader());
		memeListItemData.memeBackground = (MemeBackground) src.readSerializable();
		
		return memeListItemData;
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

	public synchronized Bitmap getThumb() {
		if (thumb == null && thumbBytes != null && thumbBytes.length > 0) {
			thumb = Utils.getBitmapFromBytes(thumbBytes);
		}
		
		return thumb;
	}

	public MemeBackground getMemeBackground() {
		return memeBackground;
	}

	public void setMemeBackground(MemeBackground memeType) {
		this.memeBackground = memeType;
	}

	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	public byte[] getThumbBytes() {
		return thumbBytes;
	}

	public void setThumbBytes(byte[] thumbBytes) {
		this.thumbBytes = thumbBytes;
	}


}
