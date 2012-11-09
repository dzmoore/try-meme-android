package com.eastapps.meme_gen_android;

import com.eastapps.meme_gen_android.domain.MemeViewData;
import com.eastapps.meme_gen_android.http.WebClient;
import com.eastapps.meme_gen_android.web.MemeServerClient;
import com.example.meme_gen_android.R;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CreateMemeActivity extends Activity {
    private MemeViewData memeViewData;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_meme_layout);
    
        new Thread(new Runnable() {
			@Override
			public void run() {
				setMemeViewData(new MemeServerClient(CreateMemeActivity.this).createMemeViewData(1));
			}
		}).start();
        
        final Button topTextBtn = (Button) findViewById(R.id.edit_top_text_btn);
        topTextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleTopTextClick(v);
			}
		});
    }
    
    protected void handleTopTextClick(final View v) {
    	final LayoutParams originalTopTextBtnLayoutParams = new LayoutParams(v.getLayoutParams());
		v.setVisibility(View.GONE);
		
		final LinearLayout topTextLinearLayout = (LinearLayout)findViewById(R.id.top_text_linear);
		final EditText editText = new EditText(this);
		editText.setLayoutParams(originalTopTextBtnLayoutParams);
		
		topTextLinearLayout.addView(editText, 0);
		
		final ImageButton imgBtn = (ImageButton)findViewById(R.id.config_top_text_btn);
		imgBtn.getLayoutParams().width = 100;
    }

    
	public void setMemeViewData(final MemeViewData mvd) {
    	this.memeViewData = mvd;
    	
    	runOnUiThread(new Runnable() {
			@Override
			public void run() {
				doSetMemeViewData();
			}
		});
    }

	private void doSetMemeViewData() {
		if (memeViewData == null) {
			return;
		}
		
		final TextView topTxtView = (TextView)findViewById(R.id.top_text_textview);
        final TextView bottomTxtView = (TextView)findViewById(R.id.bottom_text_textview);
        
        Typeface robotoBoldCondensedTypeFace = Typeface.createFromAsset(getAssets(), "fonts/Roboto-BoldCondensed.ttf");
        
        topTxtView.setTypeface(robotoBoldCondensedTypeFace);
        topTxtView.setTextSize(26);
        
        bottomTxtView.setTypeface(robotoBoldCondensedTypeFace);
        bottomTxtView.setTextSize(26);
        
        final ImageView imgView = (ImageView) findViewById(R.id.image_view);
		topTxtView.setText(memeViewData.getTopText());
        bottomTxtView.setText(memeViewData.getBottomText());
        imgView.setImageBitmap(memeViewData.getBackground());
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_create_meme, menu);
        return true;
    }
}
