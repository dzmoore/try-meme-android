package com.trymeme.meme_gen_android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.trymeme.meme_gen_android.R;

public class ViewMemeActivity extends Activity {
    private static final String TAG = ViewMemeActivity.class.getSimpleName();
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.meme_view_layout);
        
    }
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_meme, menu);
        return true;
    }

}
