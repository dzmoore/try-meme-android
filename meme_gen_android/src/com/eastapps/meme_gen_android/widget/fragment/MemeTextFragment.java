package com.eastapps.meme_gen_android.widget.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.widget.OutlineTextView;

@SuppressLint("NewApi")
public class MemeTextFragment extends android.support.v4.app.Fragment {
	private String text;
	private OutlineTextView oTextView;
	private float textSize;
	
	public MemeTextFragment(final String text) {
		super();
	
		this.text = text;
		this.textSize = 26;
	}
	
	public MemeTextFragment() {
		this(Constants.EMPTY_STRING);
	}
	
	public MemeTextFragment(final Context context, final AttributeSet attrSet) {
		this();
	}
	
	public void setText(final String text) {
		this.text = text;
		
		if (oTextView != null && getActivity() != null) {
			getActivity().runOnUiThread(
				new Runnable() {
					@Override
					public void run() {
						oTextView.setText(MemeTextFragment.this.text);
					}
			});
		}
	}
	
	public String getText() {
		return this.text;
	}
	
	public void setTextSize(final float size) {
		this.textSize = size;
		
		if (oTextView != null) {
			getActivity().runOnUiThread(
				new Runnable() {
					@Override
					public void run() {
						oTextView.setTextSize(MemeTextFragment.this.textSize);
					}
				});
		}
	}
	
	@Override
	public View onCreateView(
			LayoutInflater inflater, 
			ViewGroup container,
			Bundle savedInstanceState) 
	{
		oTextView = new OutlineTextView(getActivity());
		oTextView.setText(text);
		oTextView.overrideShadowRadius(true, 7);
		oTextView.setTextSize(textSize);
		
//		if (container != null) {
//			container.addView(oTextView);
//		}
		
//		oTextView.setLayoutParams(new TableLayout.LayoutParams(
//			ViewGroup.LayoutParams.WRAP_CONTENT,
//			ViewGroup.LayoutParams.WRAP_CONTENT, 
//			1f
//		));
		
		return oTextView;
	}

	public float getTextSize() {
		return textSize;
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
