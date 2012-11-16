package com.eastapps.meme_gen_android;

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.eastapps.meme_gen_android.domain.MemeViewData;
import com.eastapps.meme_gen_android.web.MemeServerClient;
import com.eastapps.meme_gen_android.widgets.OutlineTextView;
import com.eastapps.util.Conca;
import com.example.meme_gen_android.R;

public class CreateMemeActivity extends Activity {
	private static final String TAG = CreateMemeActivity.class.getSimpleName();
	
    private MemeViewData memeViewData;
    private AtomicBoolean isEditingTopText;
    private AtomicBoolean isEditingBottomText;
    private EditText topTextEdit;
    private EditText bottomTextEdit;
	private SeekBar topSeekBar;
	private SeekBar bottomSeekBar;
    
    public CreateMemeActivity() {
    	super();
    	
    	isEditingTopText = new AtomicBoolean(false);
    	isEditingBottomText = new AtomicBoolean(false);
    }
    
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_meme_layout);
		
        if (topTextEdit == null) {
        	topTextEdit = new EditText(this);
        	topTextEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					getMemeViewTopTextView().setText(topTextEdit.getText().toString());
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
				
				@Override
				public void afterTextChanged(Editable s) { }
			});
			
			topTextEdit.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
			getTopTextLinearLayout().addView(topTextEdit, 0);
			topTextEdit.setVisibility(View.GONE);
			
			topSeekBar = new SeekBar(this);
			topSeekBar.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
			getTopTextLinearLayout().addView(topSeekBar, 0);
			topSeekBar.setVisibility(View.GONE);
			topSeekBar.setMax(getResources().getInteger(R.integer.maxFontSize));
			topSeekBar.setProgress((int)getMemeViewBottomTextView().getTextSize());
			
			topSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					
				}
				
				@Override
				public void onProgressChanged(
					SeekBar seekBar, 
					int progress, 
					boolean fromUser) 
				{
					Log.d(TAG, Conca.t("top seek bar progress change={", progress, "} / {", seekBar.getMax(), "}"));
					getMemeViewTopTextView().setTextSize(seekBar.getProgress());
				}
			});
        }
        
        if (bottomTextEdit == null) {
        	bottomTextEdit = new EditText(this);
        	
        	bottomTextEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					getMemeViewBottomTextView().setText(bottomTextEdit.getText().toString());
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
				
				@Override
				public void afterTextChanged(Editable s) { }
			});
        	
        	bottomTextEdit.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
			getBottomTextLinearLayout().addView(bottomTextEdit, 0);
			bottomTextEdit.setVisibility(View.GONE);
			
			bottomSeekBar = new SeekBar(this);
			bottomSeekBar.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
			getBottomTextLinearLayout().addView(bottomSeekBar, 0);
			bottomSeekBar.setVisibility(View.GONE);
			bottomSeekBar.setMax(getResources().getInteger(R.integer.maxFontSize));
			bottomSeekBar.setProgress((int)getMemeViewBottomTextView().getTextSize());
        }
		
		
        new Thread(new Runnable() {
			@Override
			public void run() {
				memeViewData = new MemeServerClient(CreateMemeActivity.this).createMemeViewData(9);
			
	            setMemeViewData(memeViewData);
			}
		}).start();
        
        getTopTextBtn().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleTopTextBtnClick(v);
			}
		});
        
		getConfigTopTextBtn().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleTopTextConfigBtnClick(v);
			}
		});
		
		
		
		getBottomTextBtn().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleBottomTextBtnClick(v);
			}
		});	
		
		getConfigBottomTextBtn().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleBottomTextConfigBtnClick(v);
			}
		});
    }
	
	

	protected void handleBottomTextBtnClick(View v) {
		startEditing(
			v,
			getConfigBottomTextBtn(),
			bottomTextEdit,
			getBottomTextLinearLayout(),
			isEditingBottomText
		);
	}

	private ImageButton getConfigBottomTextBtn() {
		return (ImageButton)findViewById(R.id.config_bottom_text_btn);
	}

	private Button getTopTextBtn() {
		return (Button)findViewById(R.id.edit_top_text_btn);
	}
	
    protected void handleBottomTextConfigBtnClick(View v) {
    	handleConfigBtnClick(
    		v,
    		getMemeViewBottomTextView(),
    		getBottomTextBtn(),
    		bottomTextEdit,
    		isEditingBottomText
		);
	}
    
    private Button getBottomTextBtn() {
    	return (Button)findViewById(R.id.edit_bottom_text_btn);
    }

    
    protected void handleTopTextConfigBtnClick(View v) {
    	handleConfigBtnClick(
    		v,
    		getMemeViewTopTextView(),
    		getTopTextBtn(),
    		topTextEdit,
    		isEditingTopText
		);
	}

	private LinearLayout getTopTextLinearLayout() {
		return (LinearLayout)findViewById(R.id.top_text_linear);
	}
	
	private LinearLayout getBottomTextLinearLayout() {
		return (LinearLayout)findViewById(R.id.bottom_text_linear);
	}
    
    private void handleConfigBtnClick(
    		final View btnClicked, 
    		final TextView textViewToSet, 
    		final Button textEditBtn,
    		final EditText textEdit,
    		final AtomicBoolean isEditing) 
    {
    	// if editing --> stop editing
    	if (isEditing.get()) {
    		final String newText = textEdit.getText().toString();
    		// set new text in the meme view
			textViewToSet.setText(newText);
			
			// change back to the 'config' icon
			if (btnClicked instanceof ImageButton) {
				final ImageButton configImgBtn = (ImageButton)btnClicked;
				configImgBtn.setImageDrawable(getResources().getDrawable(R.drawable.config_icon));
			}
			
			// hide the text edit field
			textEdit.setVisibility(View.GONE);
			
			// show the text edit btn
			textEditBtn.setVisibility(View.VISIBLE);
			
			// set this editing to false
			isEditing.set(false);
    	
		// handle text config
    	} else {
    		final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
    		alertBuilder
	    		.setTitle("Change Text Size")
	    		.setCancelable(true)
	    		.setPositiveButton(
	    			"Change Font Size",
	    			new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					}
    			);
    		
    		final LayoutInflater inflater = getLayoutInflater();
    		alertBuilder.setView(inflater.inflate(R.layout.text_size_dialog_layout, null));
    		
    		final AlertDialog alertDialog = alertBuilder.create();
    		alertDialog.show();
    		
			textEditBtn.setVisibility(View.GONE);
			textEdit.setVisibility(View.GONE);
			
			topSeekBar.setVisibility(View.VISIBLE);
    	}
    }

	private void startEditing(
			final View btnClicked, 
			final ImageButton configBtn,
			final EditText textEdit,
			final LinearLayout parentLayout, 
			final AtomicBoolean isEditing) 
	{
		btnClicked.setVisibility(View.GONE);

		// determine if edit text has already been
		// added to the parent layout
		boolean parentContainsTextEdit = false;
		for (int i = 0; i < parentLayout.getChildCount(); i++) {
			if (parentLayout.getChildAt(i) == textEdit) {
				parentContainsTextEdit = true;
				break;
			}
		}
		
		// if it has already been added,
		// just set it visible
		if (parentContainsTextEdit) {
			textEdit.setVisibility(View.VISIBLE);
			
		// otherwise, set the layout params and add to
		// the layout
		} else {
			
		}
		
		// set the config btn's image to 
		// the 'finish' icon
		configBtn.setImageDrawable(getResources().getDrawable(R.drawable.finish_icon));
		
		// set this editing flag to true
		isEditing.set(true);
	}

	protected void handleTopTextBtnClick(final View v) {
		startEditing(
			v,
			getConfigTopTextBtn(),
			topTextEdit,
			getTopTextLinearLayout(),
			isEditingTopText
		);
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
        topTxtView.setTextSize(getResources().getInteger(R.integer.initialFontSize));
        
        bottomTxtView.setTypeface(robotoBoldCondensedTypeFace);
        bottomTxtView.setTextSize(getResources().getInteger(R.integer.initialFontSize));
        
        final ImageView imgView = (ImageView) findViewById(R.id.image_view);
		topTxtView.setText(memeViewData.getTopText());
        bottomTxtView.setText(memeViewData.getBottomText());
        imgView.setImageBitmap(memeViewData.getBackground());
        topTextEdit.setText(memeViewData.getTopText());
        bottomTextEdit.setText(memeViewData.getBottomText());
	}

	private OutlineTextView getMemeViewBottomTextView() {
		return (OutlineTextView)findViewById(R.id.bottom_text_textview);
	}

	private OutlineTextView getMemeViewTopTextView() {
		return (OutlineTextView)findViewById(R.id.top_text_textview);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_create_meme, menu);
        return true;
    }
}
