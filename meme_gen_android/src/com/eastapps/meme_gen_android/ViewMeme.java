package com.eastapps.meme_gen_android;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.example.meme_gen_android.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.ViewTreeObserver;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewMeme extends Activity {
    private static final String TAG = ViewMeme.class.getSimpleName();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.meme_view);
        
        final TextView topTxtView = (TextView)findViewById(R.id.top_text_textview);
        final TextView bottomTxtView = (TextView)findViewById(R.id.bottom_text_textview);
        
        Typeface robotoBoldCondensedTypeFace = Typeface.createFromAsset(getAssets(), "fonts/Roboto-BoldCondensed.ttf");
        
        topTxtView.setTypeface(robotoBoldCondensedTypeFace);
        topTxtView.setTextSize(26);
        
        bottomTxtView.setTypeface(robotoBoldCondensedTypeFace);
        bottomTxtView.setTextSize(26);
        
        final ImageView imgView = (ImageView) findViewById(R.id.image_view);
        try {
			URL url = new URL("http://192.168.1.17:8080/meme_gen_server/spring/meme_data/2/background");
			URLConnection connection = url.openConnection();
			connection.setUseCaches(true);
			Bitmap response = BitmapFactory.decodeStream(connection.getInputStream());
			
			if (response != null) {
				imgView.setImageBitmap(response);
			}
			
		} catch (Exception e) {
			Log.e(TAG, "error occurred", e);
		}
    }

    private void addViewTreeObserverToImgView() {
        final ImageView imgView = (ImageView) findViewById(R.id.image_view);
        final TextView topTxtView = (TextView)findViewById(R.id.top_text_textview);
        
        final ViewTreeObserver viewTreeObserver = imgView.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                handlePreDrawImgView(imgView, topTxtView);
                
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_meme, menu);
        return true;
    }

    private void handlePreDrawImgView(final ImageView imgView, final TextView topTxtView) {
        if (true) return;
        
        final int[] locOnScreen = new int[2];
        imgView.getLocationOnScreen(locOnScreen);
        
        final int width = imgView.getMeasuredWidth();
        final int height = imgView.getMeasuredHeight();
        
        final MarginLayoutParams marginParams = new MarginLayoutParams(imgView.getLayoutParams());
        marginParams.setMargins(
            locOnScreen[0], 
            locOnScreen[1] + topTxtView.getHeight(),
            getWindow().getAttributes().width - (locOnScreen[0] + width), 
            0
            );
        
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
        topTxtView.setLayoutParams(layoutParams);
    }
}
