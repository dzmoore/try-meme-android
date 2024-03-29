package com.trymeme.meme_gen_android.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eastapps.mgs.model.Meme;
import com.eastapps.mgs.model.MemeBackground;
import com.eastapps.mgs.model.MemeText;
import com.trymeme.meme_gen_android.BuildConfig;
import com.trymeme.meme_gen_android.R;
import com.trymeme.meme_gen_android.domain.MemeViewData;
import com.trymeme.meme_gen_android.mgr.ICallback;
import com.trymeme.meme_gen_android.service.IMemeService;
import com.trymeme.meme_gen_android.service.impl.MemeService;
import com.trymeme.meme_gen_android.util.Constants;
import com.trymeme.meme_gen_android.util.StringUtils;
import com.trymeme.meme_gen_android.util.TaskRunner;
import com.trymeme.meme_gen_android.widget.OutlineTextView;
import com.trymeme.meme_gen_android.widget.TagMgr;
import com.trymeme.meme_gen_android.widget.adapter.MemePagerFragmentAdapter;
import com.trymeme.util.Conca;

public class CreateMemeActivity extends FragmentActivity {
	private static final String TAG = CreateMemeActivity.class.getSimpleName();

	protected static final float SEEK_BAR_MULTIPLIER = 1.6f;

	protected static final float SEEK_BAR_SCALAR = 12;
	


	private MemeViewData memeViewData;
	private List<MemeViewData> sampleMemesList;
	private AtomicBoolean isEditingTopText;
	private AtomicBoolean isEditingBottomText;
	private AtomicBoolean isTopTextSizing;
	private AtomicBoolean isBottomTextSizing;
	private EditText topTextEdit;
	private EditText bottomTextEdit;
	private SeekBar topSeekBar;
	private SeekBar bottomSeekBar;
	private IMemeService memeService;
	private AtomicBoolean isLoadComplete;
	private ProgressDialog loadingDialog;
	
	private double initialFontSize;
	
	
	private ViewPager memePager;

	public CreateMemeActivity() {
		super();

		isEditingTopText = new AtomicBoolean(false);
		isEditingBottomText = new AtomicBoolean(false);

		isTopTextSizing = new AtomicBoolean(false);
		isBottomTextSizing = new AtomicBoolean(false);
		
		isLoadComplete = new AtomicBoolean(false);
	}
	
	private void initLoadingDialog() {
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setIndeterminate(true);
		loadingDialog.setMessage("Loading Background and Samples...");
	}
	
	@Override
	protected void onResume() {
		initLoadingDialog();
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_meme_layout);
		
		memeService = MemeService.getInstance();
		memeService.setConnectionExceptionCallback(new ICallback<Exception>() {
			@Override
			public void callback(Exception obj) {
				if (obj instanceof UnknownHostException) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(
								CreateMemeActivity.this, 
								"Unable to connect to server.", 
								Toast.LENGTH_LONG
							).show();
						}
					});
				}
			}
		});
		
		initLoadingDialog();
		
		memePager = (ViewPager)findViewById(R.id.meme_view_pager);
		
		initialFontSize = getResources().getInteger(R.integer.initialFontSize);
		
		final MemeBackground memeBg = (MemeBackground) getIntent().getSerializableExtra(Constants.KEY_MEME_BACKGROUND);
		
		@SuppressWarnings("unchecked")
		final List<Meme> memes = getIntent().hasExtra(Constants.KEY_MEMES) ?
			(List<Meme>)getIntent().getSerializableExtra(Constants.KEY_MEMES) :
			null;
		
		loadBundle(savedInstanceState);
		
		if (isLoaded()) {
			setFieldsInMemeView();
			
			initGui();
			
		} else {
			TaskRunner.runAsync(new Runnable() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							loadingDialog.show();
						}
					});
					
					try {
                        if (memes == null) {
                            memeViewData = memeService.createMemeViewData(memeBg);
                            
                        } else {
                            memeViewData = memeService.createMemeViewData(memes);
                        }
                        
                        setMemeViewData(memeViewData);
                        
					} finally {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.hide();
                            }
                        });
					}
				}
			});
		}
		
//		TaskRunner.runAsync(new Runnable() {
//			@Override
//			public void run() {
//				AdMgr.getInstance().initAd(CreateMemeActivity.this, R.id.create_view_advertising_banner_view);
//			}
//		});
	}
	
	
	
	private boolean isLoaded() {
		return isLoadComplete.get();
	}
	
	@SuppressWarnings("unchecked")
	private void loadBundle(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			return;
		}
		
		memeViewData = new MemeViewData();
		
		final String memeBgKey = getString(R.string.bundleconst_memebg);
		if (savedInstanceState.containsKey(memeBgKey)) {
			final Bitmap bg = (Bitmap)savedInstanceState.getParcelable(memeBgKey);
			memeViewData.setBackground(bg);
		}
		
		String sampleMemesKey = getString(R.string.bundleconst_samplememes);
		if (savedInstanceState.containsKey(sampleMemesKey)) {
			final List<Meme> shMemes = (ArrayList<Meme>) savedInstanceState.getSerializable(sampleMemesKey);
			
			memeViewData.setSampleMemes(shMemes);
		}
		
		final String loadedKey = getString(R.string.bundleconst_loaded);
		if (savedInstanceState.containsKey(loadedKey)) {
			isLoadComplete.set(savedInstanceState.getBoolean(loadedKey));
		}
		
		final String pagerAdapterKey = getString(R.string.bundleconst_pageradapter);
		if (savedInstanceState.containsKey(pagerAdapterKey)) {
			memePager.setAdapter((PagerAdapter) savedInstanceState.getParcelable(pagerAdapterKey));
		}
		
		final String sampledListKey = getString(R.string.bundleconst_sampledlist);
		if (savedInstanceState.containsKey(sampledListKey)) {
			this.sampleMemesList = (ArrayList<MemeViewData>) savedInstanceState.getSerializable(sampledListKey);
			
			final MemePagerFragmentAdapter pagerAdapter = new MemePagerFragmentAdapter(getSupportFragmentManager());
			memePager.setAdapter(pagerAdapter);
			
			pagerAdapter.setMemes(sampleMemesList);
		}
		
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		if (memeViewData != null) {
			outState.putParcelable(getString(R.string.bundleconst_memebg), memeViewData.getBackground());
			outState.putSerializable(getString(R.string.bundleconst_samplememes), new ArrayList<Meme>(memeViewData.getSampleMemes()));
			outState.putBoolean(getString(R.string.bundleconst_loaded), isLoadComplete.get());
			outState.putSerializable(getString(R.string.bundleconst_sampledlist), new ArrayList<MemeViewData>(sampleMemesList));
		}
	}
	
	private void initGui() {
		initTopTextEdit();
		
		initTopSeekbar();

		initBottomTextEdit();
		
		initBottomSeekbar();

		initTopTextBtn();

		initConfigTopTextBtn();

		initBottomTextBtn();

		initConfigBottomTextBtn();
		
		initSaveBtn();
		
		initShareBtn();
	}

	private void initShareBtn() {
		final Button shareBtn = (Button) findViewById(R.id.share_btn);
		shareBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						doHandleShareBtnClick(v);
					}


				
				}).start();
			}
		});
	}

	private void doHandleShareBtnClick(View v) {
		final View memeView = (View)memePager.findViewWithTag(TagMgr.getMemeViewLayoutTag(getSelectedMemeViewId()));
		
		Bitmap b = Bitmap.createBitmap(memeView.getWidth(), memeView.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		memeView.draw(c);
		
		boolean isExternalStorageAvailable = false;
		boolean isExternalStorageWriteable = false;
		
		final String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    isExternalStorageAvailable = isExternalStorageWriteable = true;
		    
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    isExternalStorageAvailable = true;
		    isExternalStorageWriteable = false;
		    
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    isExternalStorageAvailable = isExternalStorageWriteable = false;
		}
		
		if (!isExternalStorageWriteable) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(
						CreateMemeActivity.this,
						"Unable to write to storage",
						Toast.LENGTH_SHORT
					).show();
				}
			});
			
			return;
		}
		
		try {
			final File eastAppsDir =
			new File(
				Conca.t(
					Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath(),
					File.separator, "eastapps_memes"));
			
			if (!eastAppsDir.exists()) {
				eastAppsDir.mkdirs();
			}
			
			final File imgFile = new File(
				eastAppsDir, 
				TagMgr.getMemeFileName()
			);
			
			FileOutputStream fos = new FileOutputStream(imgFile);
			
			b.compress(CompressFormat.JPEG, 90, fos);
			fos.flush();
			fos.close();
		
			Intent intent = new Intent(Intent.ACTION_SEND);
			
			intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imgFile));
			intent.setType("image/jpg");
			
			startActivity(Intent.createChooser(intent,"MMS Meme"));
					
		} catch (final IOException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "err", e);
			}
			
			runOnUiThread(
				new Runnable() {
					@Override
					public void run() {
						Toast.makeText(
							CreateMemeActivity.this,
							Conca.t(
								"There was a problem storing the meme",
								StringUtils.isNotBlank(e.getMessage())
									? ": " + e.getMessage()
									: ""
							),
							Toast.LENGTH_SHORT
						).show();
					}
			});
		}
	}

	private void initTopSeekbar() {
		topSeekBar = new SeekBar(this);
		topSeekBar.setLayoutParams(new TableLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT, 1f));
		
		getTopTextLinearLayout().addView(topSeekBar, 0);
		
		topSeekBar.setVisibility(View.GONE);
		topSeekBar.setMax(getResources().getInteger(R.integer.maxFontSize));
		topSeekBar.setProgress(getSelectedMeme().getTopText().getFontSize().intValue());
		
		createSeekBarChangeListener(true);
	}

	private void initBottomSeekbar() {
		bottomSeekBar = new SeekBar(this);
		
		bottomSeekBar.setLayoutParams(new TableLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT, 
			1f
		));
		
		getBottomTextLinearLayout().addView(bottomSeekBar, 0);
		
		bottomSeekBar.setVisibility(View.GONE);
		bottomSeekBar.setMax(getResources().getInteger(R.integer.maxFontSize)); 
		bottomSeekBar.setProgress(getSelectedMeme().getBottomText().getFontSize().intValue());

		createSeekBarChangeListener(false);
	}

	private void initSaveBtn() {
		final Button saveBtn = (Button) findViewById(R.id.save_btn);
		saveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						doHandleSaveBtnClick(v);
					}

				
				}).start();
			}
		});
	}

	private void doHandleSaveBtnClick(final View v) {
		final Meme meme = getSelectedMeme();
		
		TaskRunner.runAsync(new Runnable() {
			@Override
			public void run() {
				final long id = memeService.storeMeme(meme);
				
				final StringBuilder sb = new StringBuilder();
				if (id > 0) {
					sb.append(getString(R.string.memeStoredSuccessMessage));
					
				} else {
					sb.append(getString(R.string.memeStoredFailMessage));
				}
				
				runOnUiThread(
					new Runnable() {
						@Override
						public void run() {
							Toast.makeText(CreateMemeActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
						}
					}
				);
				
			}
		});
	}

	private OutlineTextView getSelectedBottomTextView() {
		return (OutlineTextView)memePager.findViewWithTag(TagMgr.getTextViewTag(getSelectedMemeViewId(), false));
	}

	private OutlineTextView getSelectedTopTextView() {
		return (OutlineTextView)memePager.findViewWithTag(TagMgr.getTextViewTag(getSelectedMemeViewId(), true));
	}

	private int getSelectedMemeViewId() {
		final int selected = memePager.getCurrentItem();
		
		final int selectedId = sampleMemesList == null ? Constants.INVALID : sampleMemesList.get(selected).getId();
		
		return selectedId;
	}

	private void initConfigBottomTextBtn() {
		getConfigBottomTextBtn().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleBottomTextConfigBtnClick(v);
			}
		});
	}

	private void initBottomTextBtn() {
		getBottomTextBtn().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleBottomTextBtnClick(v);
			}
		});
	}

	private void initConfigTopTextBtn() {
		getConfigTopTextBtn().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleTopTextConfigBtnClick(v);
			}
		});
	}

	private void initTopTextBtn() {
		getTopTextBtn().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleTopTextBtnClick(v);
			}
		});
	}

	private void initBottomTextEdit() {
		if (bottomTextEdit == null) {
			bottomTextEdit = new EditText(this);

			bottomTextEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					final TextView selectBottomTextView = getSelectedBottomTextView();
					if (selectBottomTextView != null) {
						final String newBottomText = bottomTextEdit.getText().toString();
						
						selectBottomTextView.setText(newBottomText);
						
						if (bottomSeekBar != null) {
							setBottomTextViewTextSize(bottomSeekBar.getProgress());
						}
						
						getSelectedMeme().getBottomText().setText(newBottomText);
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

				@Override
				public void afterTextChanged(Editable s) { }
			});

			bottomTextEdit.setLayoutParams(new TableLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT, 
				1f
			));
			
			getBottomTextLinearLayout().addView(bottomTextEdit, 0);
			bottomTextEdit.setVisibility(View.GONE);
			bottomTextEdit.setText(getSelectedMeme().getBottomText().getText());

			setBottomTextViewTextSize(getSelectedMeme().getBottomText().getFontSize().intValue());
		}
	}

	private void initTopTextEdit() {
		if (topTextEdit == null) {
			topTextEdit = new EditText(this);
			topTextEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(
					final CharSequence s, 
					final int start,
					final int before, 
					final int count) 
				{
					final TextView selectedTopTextView = getSelectedTopTextView();
					if (selectedTopTextView != null) {
						final String newTopText = topTextEdit.getText().toString();
						
						selectedTopTextView.setText(newTopText);
						
						if (topSeekBar != null) {
							setTopTextViewTextSize(topSeekBar.getProgress());
						}
						
						getSelectedMeme().getTopText().setText(newTopText);
					}
				} 

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) { /* blank */ }
			});

			topTextEdit.setLayoutParams(new TableLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT, 1f));
			
			getTopTextLinearLayout().addView(topTextEdit, 0);
			topTextEdit.setVisibility(View.GONE);
			topTextEdit.setText(getSelectedMeme().getTopText().getText());

			
			setTopTextViewTextSize(getSelectedMeme().getTopText().getFontSize().intValue());
		}
	}

	private void createSeekBarChangeListener(final boolean isTop) {
		final SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(
					final SeekBar seekBar, 
					final int progress,
					final boolean fromUser) 
			{	
				if (isTop) {
					setTopTextViewTextSize(progress);
					getSelectedMeme().getTopText().setFontSize((double) progress);
				
				} else {
					setBottomTextViewTextSize(progress);
					getSelectedMeme().getBottomText().setFontSize((double) progress);
				}
			}
		};
		
		if (isTop) {
			topSeekBar.setOnSeekBarChangeListener(listener);
		
		} else {
			bottomSeekBar.setOnSeekBarChangeListener(listener);
		}
	}

	protected void handleBottomTextBtnClick(View v) {
		startEditing(
			v, 
			getConfigBottomTextBtn(), 
			bottomTextEdit,
			isEditingBottomText
		);
}

	private ImageButton getConfigBottomTextBtn() {
		return (ImageButton) findViewById(R.id.config_bottom_text_btn);
	}

	private Button getTopTextBtn() {
		return (Button) findViewById(R.id.edit_top_text_btn);
	}

	protected void handleBottomTextConfigBtnClick(View v) {
		handleConfigBtnClick(
			v, 
			getBottomTextBtn(), 
			bottomTextEdit, 
			isEditingBottomText,
			isBottomTextSizing, 
			bottomSeekBar
		);
	}

	private Button getBottomTextBtn() {
		return (Button) findViewById(R.id.edit_bottom_text_btn);
	}

	protected void handleTopTextConfigBtnClick(View v) {
		handleConfigBtnClick(
			v, 
			getTopTextBtn(),
			topTextEdit, 
			isEditingTopText, 
			isTopTextSizing, 
			topSeekBar
		);
	}

	private LinearLayout getTopTextLinearLayout() {
		return (LinearLayout) findViewById(R.id.top_text_linear);
	}

	private LinearLayout getBottomTextLinearLayout() {
		return (LinearLayout) findViewById(R.id.bottom_text_linear);
	}

	private void handleConfigBtnClick(
			final View btnClicked,
			final Button textEditBtn,
			final EditText textEdit, 
			final AtomicBoolean isEditing,
			final AtomicBoolean isTextSizing, 
			final SeekBar seekBar) 
	{
		// if editing --> stop editing
		if (isEditing.get()) {
			final String newText = textEdit.getText().toString();
		
			// change back to the 'config' icon
			setBtnToConfigImg(btnClicked);

			// hide the text edit field
			textEdit.setVisibility(View.GONE);

			// show the text edit btn
			textEditBtn.setVisibility(View.VISIBLE);

			// set this editing to false
			isEditing.set(false);

		// handle text sizing config
		} else {
			if (isTextSizing.get()) {
				textEditBtn.setVisibility(View.VISIBLE);
				textEdit.setVisibility(View.GONE);
				seekBar.setVisibility(View.GONE);
				
				setBtnToConfigImg(btnClicked);
				
				isTextSizing.set(false);

			} else {
				textEditBtn.setVisibility(View.GONE);
				textEdit.setVisibility(View.GONE);
				seekBar.setVisibility(View.VISIBLE);
				
				setBtnToFinishImg(btnClicked);
				
				isTextSizing.set(true);
			}
		}
	}

	private void setBtnToFinishImg(View btnClicked) {
		if (btnClicked instanceof ImageButton) {
			setBtnToFinishImg((ImageButton)btnClicked);
		}
	}

	private void setBtnToConfigImg(final View btnClicked) {
		if (btnClicked instanceof ImageButton) {
			final ImageButton configImgBtn = (ImageButton) btnClicked;
			configImgBtn.setImageDrawable(
				getResources().getDrawable(R.drawable.config_icon)
			);
		}
	}

	private void startEditing(
			final View btnClicked,
			final ImageButton configBtn, 
			final EditText textEdit,
			final AtomicBoolean isEditing) 
	{
		if (!isEditing.get()) {
			btnClicked.setVisibility(View.GONE);
			
			textEdit.setVisibility(View.VISIBLE);
	
			// set the config btn's image to
			// the 'finish' icon
			setBtnToFinishImg(configBtn);
	
			// set this editing flag to true
			isEditing.set(true);
		}
	}

	private void setBtnToFinishImg(final ImageButton configBtn) {
		configBtn.setImageDrawable(
			getResources().getDrawable(R.drawable.finish_icon)
		);
	}

	protected void handleTopTextBtnClick(final View v) {
		startEditing(v, getConfigTopTextBtn(), topTextEdit, isEditingTopText);
	}

	private ImageButton getConfigTopTextBtn() {
		return (ImageButton) findViewById(R.id.config_top_text_btn);
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

//		Typeface robotoBoldCondensedTypeFace = Typeface.createFromAsset(
//			getAssets(), 
//			"fonts/Roboto-BoldCondensed.ttf"
//		);

		setFieldsInMemeView();
		
		initGui();
		
		isLoadComplete.set(true);
	}

	private void setFieldsInMemeView() {
		final MemePagerFragmentAdapter pagerAdapter 
			= new MemePagerFragmentAdapter(getSupportFragmentManager());
		
		sampleMemesList = createSampleMemeViewDataList();
		pagerAdapter.setMemes(sampleMemesList);
		
		memePager.setAdapter(pagerAdapter);
		
		memePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				loadCurrentlySelectedPage();
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
//				if (state == ViewPager.SCROLL_STATE_SETTLING) {
//					loadCurrentlySelectedPage();
//				}
			}
		});
	}
	
	
	private void loadCurrentlySelectedPage() {
		final Meme selectedMeme = getSelectedMeme();
		
		final MemeText topText = selectedMeme.getTopText();
		if (topText == null) {
			selectedMeme.setTopText(createNewMemeText());
		}
		
		final int topTextFontSize = topText.getFontSize().intValue();
		topSeekBar.setProgress(topTextFontSize);
		setTopTextViewTextSize(topTextFontSize); 
		topTextEdit.setText(topText.getText());
		
		final MemeText bottomText = selectedMeme.getBottomText();
		if (bottomText == null) {
			selectedMeme.setBottomText(createNewMemeText());
		}
		
		final int bottomTextFontSize = bottomText.getFontSize().intValue();
		bottomSeekBar.setProgress(bottomTextFontSize);
		setBottomTextViewTextSize(bottomTextFontSize);
		bottomTextEdit.setText(bottomText.getText());
	}

	private MemeText createNewMemeText() {
		final MemeText newMemeText = new MemeText();
		newMemeText.setFontSize(initialFontSize);
		newMemeText.setText("");
		return newMemeText;
	}



	private float calculateTextSize(final int progress) {
		final int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		
		final TypedValue multiplierTypedValue = new TypedValue();
		getResources().getValue(R.dimen.memeTextViewTextMultiplier, multiplierTypedValue,  true);
		final float multiplier = multiplierTypedValue.getFloat();
		
		final TypedValue constantTypedValue = new TypedValue();
		getResources().getValue(R.dimen.memeTextViewTextConstant, constantTypedValue,  true);
		final float constant = constantTypedValue.getFloat();
		
		final float multiplierHeight = multiplier * screenHeight;
		
		return 
			(
				progress 
				* multiplierHeight
			) 
			+ 
			(
				constantTypedValue.getFloat()
				* multiplierHeight
			);
	}

	private MemePagerFragmentAdapter getMemePagerAdapter() {
		return (MemePagerFragmentAdapter)memePager.getAdapter();
	}
	

	private List<MemeViewData> createSampleMemeViewDataList() {
		final List<MemeViewData> emptyAndSampleMemeList 
			= new ArrayList<MemeViewData>(memeViewData.getSampleMemes().size()+1);
		
		final Bitmap bgBm = memeViewData.getBackground();
		
		final MemeViewData editableMeme = new MemeViewData();
		final Meme meme = memeViewData.getMeme();
		if (meme.getTopText() == null) {
			meme.setTopText(createNewMemeText());
		}
		
		if (meme.getBottomText() == null) {
			meme.setBottomText(createNewMemeText());
		}
		
		editableMeme.setMeme(meme);
		editableMeme.setBackground(bgBm);
		
		emptyAndSampleMemeList.add(editableMeme);
		
		for (final Meme eaMeme : memeViewData.getSampleMemes()) {
			final MemeViewData mvd = new MemeViewData();
			
			mvd.setBackground(bgBm);
			mvd.setMeme(eaMeme);
			
			emptyAndSampleMemeList.add(mvd);
		}
		
		
		return emptyAndSampleMemeList;
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.activity_create_meme, menu);
//		return true;
//	}
	
	
	private Meme getSelectedMeme() {
		final int current = memePager.getCurrentItem();
		
		final Meme currentMeme = getMemePagerAdapter().getMemeAt(current).getMeme();
		
		if (currentMeme.getMemeBackground() == null) {
			currentMeme.setMemeBackground(memeViewData.getMeme().getMemeBackground());
		}
		
		return currentMeme;
	}
	
	private void setTopTextViewTextSize(final int progress) {
		final OutlineTextView topTextView = getSelectedTopTextView();
		
		if (topTextView != null) {
			topTextView.setTextSize(calculateTextSize(progress));
		}
	}
	
	private void setBottomTextViewTextSize(final int progress) {
		final OutlineTextView bottomTextView = getSelectedBottomTextView();
		
		if (bottomTextView != null) {
			bottomTextView.setTextSize(calculateTextSize(progress));
		}
	}
	
}
























