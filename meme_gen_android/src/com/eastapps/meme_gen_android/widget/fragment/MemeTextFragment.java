package com.eastapps.meme_gen_android.widget.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.widget.OutlineTextView;

@SuppressLint("NewApi")
public class MemeTextFragment extends Fragment {
	private String text;
	
	public MemeTextFragment(final String text) {
		super();
	
		this.text = text;
	}
	
	public MemeTextFragment() {
		this(Constants.EMPTY_STRING);
	}
	
	@Override
	public View onCreateView(
			LayoutInflater inflater, 
			ViewGroup container,
			Bundle savedInstanceState) 
	{
		final OutlineTextView oTextView = new OutlineTextView(getActivity());
		oTextView.setText(text);
		oTextView.overrideShadowRadius(true, 7);
//		oTextView.setLayoutParams(new TableLayout.LayoutParams(
//			ViewGroup.LayoutParams.WRAP_CONTENT,
//			ViewGroup.LayoutParams.WRAP_CONTENT, 
//			1f
//		));
		
		return oTextView;
	}
	
	
//	<com.eastapps.meme_gen_android.widget.OutlineTextView
//    android:id="@+id/top_text_textview"
//    android:layout_width="wrap_content"
//    android:layout_height="wrap_content"
//    android:layout_alignLeft="@id/image_view"
//    android:layout_alignRight="@id/image_view"
//    android:layout_alignTop="@id/image_view"
//    android:gravity="center_horizontal"
//    android:paddingLeft="@dimen/text_padding"
//    android:paddingRight="@dimen/text_padding"
//    android:shadowRadius="7"
//    android:text="DUMMY TOP TEXT" />


}
