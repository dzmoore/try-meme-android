package com.eastapps.meme_gen_android.widget.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.domain.MemeViewData;
import com.eastapps.meme_gen_android.widget.OutlineTextView;
import com.eastapps.meme_gen_android.widget.ResizableImageView;
import com.eastapps.meme_gen_android.widget.TagMgr;
import com.eastapps.meme_gen_server.domain.ShallowMeme;

public class MemeViewFragment extends Fragment {
	private static final String TAG = MemeViewFragment.class.getSimpleName();
	
	private MemeViewData meme;
	private View inflatedMemeView;
	private OutlineTextView topTextView, bottomTextView;

	public MemeViewFragment() {
		this(new MemeViewData());
	}
	
	public MemeViewFragment(final MemeViewData meme) {
		super();
		
		this.meme = meme;
	}
	
	public View getMemeView() {
		return inflatedMemeView;
	}
	
	@Override
	public View onCreateView(
		final LayoutInflater inflater, 
		final ViewGroup container,
		final Bundle savedInstanceState) 
	{
//		if (inflatedMemeView == null) {
			try {
				inflatedMemeView = inflater.inflate(R.layout.meme_view_layout, container, false);
				
				ResizableImageView bgView = (ResizableImageView) inflatedMemeView.findViewById(R.id.image_view);
				bgView.setImageBitmap(meme.getBackground());
				
				topTextView = (OutlineTextView)inflatedMemeView.findViewById(R.id.top_text_view);
				topTextView.setTag(TagMgr.getTextViewTag(meme.getId(), true));
				topTextView.setText(meme.getMeme().getTopText());
				topTextView.setTextSize(meme.getMeme().getTopTextFontSize());
				
				bottomTextView = (OutlineTextView)inflatedMemeView.findViewById(R.id.bottom_text_view);
				bottomTextView.setTag(TagMgr.getTextViewTag(meme.getId(), false));
				bottomTextView.setText(meme.getMeme().getBottomText());
				topTextView.setTextSize(meme.getMeme().getBottomTextFontSize());
				
			} catch (Exception e) {
				Log.e(TAG, "err", e);
			}
//		}
		
		return inflatedMemeView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null) {
			return;
		}
		
		if (meme == null) {
			meme = new MemeViewData();
		}
		
		meme.setMeme((ShallowMeme) savedInstanceState.getSerializable("meme"));
		meme.setBackground((Bitmap) savedInstanceState.getParcelable(getActivity().getString(R.string.bundleconst_memebg)));
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		if (outState == null) {
			return;
		}
		
		outState.putSerializable("meme", meme.getMeme());
		outState.putParcelable(getActivity().getString(R.string.bundleconst_memebg), meme.getBackground());
	}
	
	
	
}













