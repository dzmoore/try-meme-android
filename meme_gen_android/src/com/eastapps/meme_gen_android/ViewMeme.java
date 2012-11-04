package com.eastapps.meme_gen_android;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastapps.meme_gen_android.json.JSONException;
import com.eastapps.meme_gen_android.json.JSONObject;
import com.eastapps.meme_gen_android.web.MemeServerClient;
import com.example.meme_gen_android.R;

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
        final MemeServerClient serverClient = new MemeServerClient(this);
        
        imgView.setImageBitmap(serverClient.getBackground(1));
        
        final JSONObject memeJsonObj = serverClient.getTexts(1);
        try {
			topTxtView.setText(memeJsonObj.getString("topText"));
	        bottomTxtView.setText(memeJsonObj.getString("bottomText"));
	        
		} catch (JSONException e) {
			Log.e(TAG, "error occurred", e);
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_meme, menu);
        return true;
    }

}
