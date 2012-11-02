package com.eastapps.meme_gen_android;

import com.example.meme_gen_android.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CreateMemeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meme);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_create_meme, menu);
        return true;
    }
}
