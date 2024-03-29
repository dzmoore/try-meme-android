package com.trymeme.meme_gen_android.widget;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.trymeme.meme_gen_android.BuildConfig;
import com.trymeme.meme_gen_android.util.Constants;
import com.trymeme.meme_gen_android.util.StringUtils;
import com.trymeme.util.Conca;

public class OutlineTextView extends TextView {
	private TextPaint textPaint;
	private TextPaint textPaintOutline;
	private String text;
	private int ascent;
	private float borderSize;
	private int borderColor;
	private int color;
	private float spacingMult;
	private float spacingAdd;
	private boolean includePad;
	private boolean shouldOverrideShadowRadiusAttr;
	private int overrideShadowRadius;
	private float textSize;
	
	public OutlineTextView(Context context) {
		super(context);
		
		init();
		
		setTextColor(getResources().getColor(R.color.white));
		setShadowLayer(10, 0, 0, getResources().getColor(R.color.black));	
		
		initPaint();
	}
	
	public OutlineTextView(Context context, AttributeSet attrs) {// here
		super(context, attrs);
		
		init();
		
		setTextColor(getResources().getColor(R.color.white));
		
		if (BuildConfig.DEBUG) {
			for (int i = 0; i < attrs.getAttributeCount(); i++) {
				Log.d(
					getClass().getSimpleName(), 
					Conca.t(
						"attr:[",
						attrs.getAttributeName(i),
						"]:[",
						attrs.getAttributeValue(i),
						"]"
					)
				);
			}
		}
		
		setShadowLayer(getShadowRadius(attrs), 0, 0, getResources().getColor(R.color.black));
		
		initPaint();
	}
	
	public OutlineTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		init();
		
		setTextColor(getResources().getColor(R.color.white));
		setShadowLayer(getShadowRadius(attrs), 0, 0, getResources().getColor(R.color.black));
		
		initPaint();
	}
	
	private void init() {
		shouldOverrideShadowRadiusAttr = false;
		overrideShadowRadius = 0;
		
		text = Constants.EMPTY_STRING;
		ascent = 0;
		spacingMult = 1f;
		spacingAdd = 0;
		includePad = true;
		
	}
	
	public void overrideShadowRadius(boolean override, int optionalRadius) {
		shouldOverrideShadowRadiusAttr = override;
		
		if (override) {
			this.overrideShadowRadius = optionalRadius;
		}
	}
	
	private float getShadowRadius(final AttributeSet attrs) {
		float shadowRadius = 0f;
		if (shouldOverrideShadowRadiusAttr) {
			shadowRadius = overrideShadowRadius;
			
		} else {
			
			for (int i = 0; i < attrs.getAttributeCount(); i++) {
				if (StringUtils.equalsIgnoreCase(attrs.getAttributeName(i), "shadowRadius")) {
					shadowRadius = attrs.getAttributeFloatValue(i, 0f);
					break;
				}
			}
		}
		
		return shadowRadius;
	}

	

	private void initPaint() {
		textPaint = new TextPaint();
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(getTextSize());
		textPaint.setColor(color);
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setTypeface(getTypeface());

		textPaintOutline = new TextPaint();
		textPaintOutline.setAntiAlias(true);
		textPaintOutline.setTextSize(getTextSize());
		textPaintOutline.setColor(borderColor);
		textPaintOutline.setStyle(Paint.Style.STROKE);
		textPaintOutline.setTypeface(getTypeface());
		textPaintOutline.setStrokeWidth(borderSize);
	}
	
	@Override
	public void setText(CharSequence text, BufferType type) {
		this.text = text.toString();
		
		super.setText(text, type);
		
		invalidate();
		requestLayout();
	}
	
	@Override
	public CharSequence getText() {
		return this.text;
	}
	
	public void setTextSize(float size) {
		super.setTextSize(size);
		
		this.textSize = size;
		
		invalidate();
		requestLayout();
		initPaint();
	}
	
	@Override
	public float getTextSize() {
		return this.textSize;
	}
	

	@Override
	public void setTextColor(int color) {
		super.setTextColor(color);
		this.color = color;
		invalidate();
		requestLayout();
		
		initPaint();
	}

	@Override
	public void setShadowLayer(float radius, float dx, float dy, int color) {
		if (BuildConfig.DEBUG) {
			Log.d(getClass().getSimpleName(), "setshadowlayer", new Exception("stack trace only"));
		}
		
		borderSize = radius;
		borderColor = color;
		invalidate();
		requestLayout();
		initPaint();
	}

	public void setTypeface(Typeface tf, int style) {
		super.setTypeface(tf, style);
		invalidate();
		requestLayout();
		initPaint();
	}

	public void setTypeface(Typeface tf) {
		super.setTypeface(tf);
		invalidate();
		requestLayout();
		initPaint();
	}
	
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Layout layout = new StaticLayout(this.text, textPaintOutline, getWidth(), Layout.Alignment.ALIGN_CENTER, spacingMult, spacingAdd, includePad);
		layout.draw(canvas);
		layout = new StaticLayout(this.text, textPaint, getWidth(), Layout.Alignment.ALIGN_CENTER, spacingMult, spacingAdd, includePad);
		layout.draw(canvas);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Layout layout = new StaticLayout(getText(), textPaintOutline, measureWidth(widthMeasureSpec), Layout.Alignment.ALIGN_CENTER, spacingMult, spacingAdd, includePad);
		int ex = (int) (borderSize * 2 + 1);
		setMeasuredDimension(measureWidth(widthMeasureSpec) + ex, measureHeight(heightMeasureSpec) * layout.getLineCount() + ex);
	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = (int) textPaintOutline.measureText(text) + getPaddingLeft() + getPaddingRight();
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}

		return result;
	}

	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		ascent = (int) textPaintOutline.ascent();
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = (int) (-ascent + textPaintOutline.descent()) + getPaddingTop() + getPaddingBottom();
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}


}