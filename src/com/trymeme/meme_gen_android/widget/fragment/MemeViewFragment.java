package com.trymeme.meme_gen_android.widget.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eastapps.mgs.model.Meme;
import com.trymeme.meme_gen_android.BuildConfig;
import com.trymeme.meme_gen_android.R;
import com.trymeme.meme_gen_android.domain.MemeViewData;
import com.trymeme.meme_gen_android.widget.OutlineTextView;
import com.trymeme.meme_gen_android.widget.ResizableImageView;
import com.trymeme.meme_gen_android.widget.TagMgr;

public class MemeViewFragment extends Fragment {
	private static final String TAG = MemeViewFragment.class.getSimpleName();
	
	private MemeViewData meme;
	private View inflatedMemeView;
	private OutlineTextView topTextView, bottomTextView;

//	public MemeViewFragment() {
//		super();
//		this(new MemeViewData());
//	}
	
//	public MemeViewFragment(final MemeViewData meme) {
//		setArguments(getArguments());
//		this.meme = meme;
//	}
	
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
				inflatedMemeView.setTag(TagMgr.getMemeViewLayoutTag(getMeme().getId()));
				
				ResizableImageView bgView = (ResizableImageView) inflatedMemeView.findViewById(R.id.image_view);
				bgView.setImageBitmap(getMeme().getBackground());
				
				topTextView = (OutlineTextView)inflatedMemeView.findViewById(R.id.top_text_view);
				topTextView.setTag(TagMgr.getTextViewTag(getMeme().getId(), true));
				topTextView.setText(getMeme().getMeme().getTopText().getText());
				topTextView.setTextSize((getMeme().getMeme().getBottomText().getFontSize()).floatValue());
				
				bottomTextView = (OutlineTextView)inflatedMemeView.findViewById(R.id.bottom_text_view);
				bottomTextView.setTag(TagMgr.getTextViewTag(getMeme().getId(), false));
				bottomTextView.setText(getMeme().getMeme().getBottomText().getText());
				topTextView.setTextSize((getMeme().getMeme().getBottomText().getFontSize()).floatValue());
				
			} catch (Exception e) {
				if (BuildConfig.DEBUG) {
					Log.e(TAG, "err", e);
				}
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
		
		if (getMeme() == null) {
			setMeme(new MemeViewData());
		}
		
		getMeme().setMeme((Meme) savedInstanceState.getSerializable("meme"));
		getMeme().setBackground((Bitmap) savedInstanceState.getParcelable(getActivity().getString(R.string.bundleconst_memebg)));
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		if (outState == null) {
			return;
		}
		
		outState.putSerializable("meme", getMeme().getMeme());
		outState.putParcelable(getActivity().getString(R.string.bundleconst_memebg), getMeme().getBackground());
	}

	public MemeViewData getMeme() {
		return meme;
	}

	public void setMeme(MemeViewData meme) {
		this.meme = meme;
	}
	
	
	
}













