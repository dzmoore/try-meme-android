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
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class CreateMemeActivity extends Activity {
    private MemeViewData memeViewData;
    private boolean isEditingTopText;
    private EditText topTextEdit;
    
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
        
        final Button topTextBtn = getTopTextBtn();
        topTextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleTopTextClick(v);
			}
		});
        
		final ImageButton topTextConfigBtn = getConfigTopTextBtn();
		topTextConfigBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleTopTextConfigBtnClick(v);
			}
		});
    }

	private Button getTopTextBtn() {
		return (Button)findViewById(R.id.edit_top_text_btn);
	}
    
    protected void handleTopTextConfigBtnClick(View v) {
    	if (isEditingTopText) {
    		final String newTopText = topTextEdit.getText().toString();
    		final TextView memeViewTopTextView = getMemeViewTopTextView();
    		memeViewTopTextView.setText(newTopText);
    		
    		final ImageButton configTopTextBtn = getConfigTopTextBtn();
    		configTopTextBtn.setImageDrawable(getResources().getDrawable(R.drawable.config_icon));
    		
    		topTextEdit.setVisibility(View.GONE);
    		getTopTextBtn().setVisibility(View.VISIBLE);
    	}
	}

	protected void handleTopTextClick(final View v) {
		v.setVisibility(View.GONE);
		
		final LinearLayout topTextLinearLayout = (LinearLayout)findViewById(R.id.top_text_linear);
		topTextEdit = new EditText(this);
		topTextEdit.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
		topTextLinearLayout.addView(topTextEdit, 0);
		
		final ImageButton topTextConfigBtn = getConfigTopTextBtn();
		topTextConfigBtn.setImageDrawable(getResources().getDrawable(R.drawable.finish_icon));
		
		isEditingTopText = true;
    }

	private ImageButton getConfigTopTextBtn() {
		return (ImageButton)findViewById(R.id.config_top_text_btn);
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
		
		final TextView topTxtView = getMemeViewTopTextView();
        final TextView bottomTxtView = getMemeViewBottomTextView();
        
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

	private TextView getMemeViewBottomTextView() {
		return (TextView)findViewById(R.id.bottom_text_textview);
	}

	private TextView getMemeViewTopTextView() {
		return (TextView)findViewById(R.id.top_text_textview);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_create_meme, menu);
        return true;
    }
}
