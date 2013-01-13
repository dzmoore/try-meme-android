package com.eastapps.meme_gen_android.mgr;

import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

import com.eastapps.meme_gen_android.domain.MemeListItemData;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class MemeListDataSerializer extends Serializer<MemeListItemData>{
	@Override
	public MemeListItemData read(
		final Kryo kryo, 
		final Input input,
		final Class<MemeListItemData> clazz) 
	{
		final MemeListItemData item = new MemeListItemData();
		item.setId(input.readInt());
		item.setMemeType(kryo.readObject(input, ShallowMemeType.class));
				
		final int thumbBytesLength = input.readInt();
		item.setThumbBytes(input.readBytes(thumbBytesLength));
		
		return item;
	}

	@Override
	public void write(
		final Kryo kryo, 
		final Output output, 
		final MemeListItemData item) 
	{
		output.writeInt(item.getId());
		kryo.writeObject(output, item.getMemeType());
		
		if (item.getThumbBytes() != null && item.getThumbBytes().length > 0) {
			output.writeInt(item.getThumbBytes().length);
			output.write(item.getThumbBytes());
		}
	}

}















