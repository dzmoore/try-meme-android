package com.eastapps.meme_gen_android.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.eastapps.meme_gen_android.BuildConfig;
import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.util.StringUtils;
import com.eastapps.util.Conca;

public class ResizableImageView extends ImageView {
	private static final String TAG = ResizableImageView.class.getSimpleName();
	
	private boolean needsInitialScale;

	public ResizableImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		needsInitialScale = true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    
		Drawable d = getDrawable();
		
		final float deviceWidth = getMeasuredWidth();
		final float deviceHeight = getMeasuredHeight();
		if (d != null && (deviceWidth > 0)) {
			
			final float imageWidth = d.getIntrinsicWidth();
			final float imageHeight = d.getIntrinsicHeight();
			
			// determine the shorter view length (width or height)
			// set the shorter length of the image to that of the view
			// scale the other length to the image ratio
			int newImageWidth = (int) Math.ceil(deviceWidth);
			int newImageHeight = (int) Math.ceil(deviceHeight);
			
			if (deviceHeight < deviceWidth) {
				newImageWidth = (int) Math.ceil(newImageHeight * (imageWidth / imageHeight));
			} else {
				newImageHeight = (int) Math.ceil(deviceWidth * (imageHeight / imageWidth));
			}
			
			if (BuildConfig.DEBUG) {
				Log.v(TAG, Conca.t("deviceWidth=", deviceWidth, "; deviceHeight=", deviceHeight,
					"; imageWidth=", imageWidth, "; imageHeight=", imageHeight, 
					"; newWidth=", newImageWidth, "; newHeight=", newImageHeight, ";"));
			}
			
			setMeasuredDimension(newImageWidth, newImageHeight);
			
		}
	}

}































