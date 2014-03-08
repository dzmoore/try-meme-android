package com.trymeme.meme_gen_android.mgr;

import com.trymeme.meme_gen_android.R;
import com.trymeme.meme_gen_android.domain.MemeListItemData;
import com.eastapps.mgs.model.MemeBackground;
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
		item.setMemeBackground(kryo.readObject(input, MemeBackground.class));
				
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
		kryo.writeObject(output, item.getMemeBackground());
		
		if (item.getThumbBytes() != null && item.getThumbBytes().length > 0) {
			output.writeInt(item.getThumbBytes().length);
			output.write(item.getThumbBytes());
		}
	}

}















