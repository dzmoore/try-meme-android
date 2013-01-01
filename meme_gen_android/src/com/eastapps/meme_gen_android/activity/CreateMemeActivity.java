package com.eastapps.meme_gen_android.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
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

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.domain.MemeViewData;
import com.eastapps.meme_gen_android.service.IMemeService;
import com.eastapps.meme_gen_android.service.impl.MemeService;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_android.widget.OutlineTextView;
import com.eastapps.meme_gen_android.widget.TagMgr;
import com.eastapps.meme_gen_android.widget.adapter.MemePagerFragmentAdapter;
import com.eastapps.meme_gen_server.domain.ShallowMeme;

public class CreateMemeActivity extends FragmentActivity {
	private static final String TAG = CreateMemeActivity.class.getSimpleName();

	private MemeViewData memeViewData;
	private List<MemeViewData> sampledList;
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
	
	
	private ViewPager memePager;

	public CreateMemeActivity() {
		super();

		isEditingTopText = new AtomicBoolean(false);
		isEditingBottomText = new AtomicBoolean(false);

		isTopTextSizing = new AtomicBoolean(false);
		isBottomTextSizing = new AtomicBoolean(false);
		
		isLoadComplete = new AtomicBoolean(false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_meme_layout);
		
		MemeService.initialize(this);
		memeService = MemeService.getInstance();		
		
		memePager = (ViewPager)findViewById(R.id.meme_view_pager);
		
		loadBundle(savedInstanceState);
		
		if (isLoaded()) {
			setFieldsInMemeView();
			
			initGui();
			
		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					memeViewData = memeService.createMemeViewData(1);
					
					setMemeViewData(memeViewData);
				}
			}).start();
		}
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
			final ArrayList<ShallowMeme> shMemes = (ArrayList<ShallowMeme>) savedInstanceState.getSerializable(sampleMemesKey);
			
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
			this.sampledList = (ArrayList<MemeViewData>) savedInstanceState.getSerializable(sampledListKey);
			
			final MemePagerFragmentAdapter pagerAdapter = new MemePagerFragmentAdapter(getSupportFragmentManager());
			memePager.setAdapter(pagerAdapter);
			
			pagerAdapter.setMemes(sampledList);
		}
		
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		if (memeViewData != null) {
			outState.putParcelable(getString(R.string.bundleconst_memebg), memeViewData.getBackground());
			outState.putSerializable(getString(R.string.bundleconst_samplememes), new ArrayList<ShallowMeme>(memeViewData.getSampleMemes()));
			outState.putBoolean(getString(R.string.bundleconst_loaded), isLoadComplete.get());
			outState.putSerializable(getString(R.string.bundleconst_sampledlist), new ArrayList<MemeViewData>(sampledList));
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
	}

	private void initTopSeekbar() {
		topSeekBar = new SeekBar(this);
		topSeekBar.setLayoutParams(new TableLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT, 1f));
		
		getTopTextLinearLayout().addView(topSeekBar, 0);
		
		topSeekBar.setVisibility(View.GONE);
		topSeekBar.setMax(getResources().getInteger(R.integer.maxFontSize));
		topSeekBar.setProgress((int) getSelectedMeme().getTopTextFontSize());
		
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
		bottomSeekBar.setProgress((int) getSelectedMeme().getBottomTextFontSize());

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
		final ShallowMeme shallowMeme = getSelectedMeme();
		
		final int id = memeService.storeMeme(shallowMeme);
		
		final StringBuilder sb = new StringBuilder();
		if (id > 0) {
			sb
				.append("Store success! id={")
				.append(id)
				.append("}");
			
		} else {
			sb.append("Store fail!");
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

	private OutlineTextView getSelectedBottomTextView() {
		return (OutlineTextView)memePager.findViewWithTag(TagMgr.getTextViewTag(getSelectedMemeViewId(), false));
	}

	private OutlineTextView getSelectedTopTextView() {
		return (OutlineTextView)memePager.findViewWithTag(TagMgr.getTextViewTag(getSelectedMemeViewId(), true));
	}

	private int getSelectedMemeViewId() {
		final int selected = memePager.getCurrentItem();
		
		final int selectedId = sampledList == null ? Constants.INVALID : sampledList.get(selected).getId();
		
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
						
						getSelectedMeme().setBottomText(newBottomText);
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
			bottomTextEdit.setText(getSelectedMeme().getBottomText());

			
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
						
						getSelectedMeme().setTopText(newTopText);
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
			topTextEdit.setText(getSelectedMeme().getTopText());

			

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
					getSelectedTopTextView().setTextSize(progress);
					getSelectedMeme().setTopTextFontSize(progress);
				
				} else {
					getSelectedBottomTextView().setTextSize(progress);
					getSelectedMeme().setBottomTextFontSize(progress);
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
		
		sampledList = createSampleMemeViewDataList();
		pagerAdapter.setMemes(sampledList);
		
		memePager.setAdapter(pagerAdapter);
		
		memePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				if (state == ViewPager.SCROLL_STATE_IDLE) {
					loadCurrentlySelectedPage();
				}
			}
		});
	}
	
	private void loadCurrentlySelectedPage() {
		final ShallowMeme selectedMeme = getSelectedMeme();
		
		topSeekBar.setProgress(selectedMeme.getTopTextFontSize());
		bottomSeekBar.setProgress(selectedMeme.getBottomTextFontSize());
		
		topTextEdit.setText(selectedMeme.getTopText());
		bottomTextEdit.setText(selectedMeme.getBottomText());
	}

	private MemePagerFragmentAdapter getMemePagerAdapter() {
		return (MemePagerFragmentAdapter)memePager.getAdapter();
	}
	

	private List<MemeViewData> createSampleMemeViewDataList() {
		final List<MemeViewData> shMemes 
			= new ArrayList<MemeViewData>(memeViewData.getSampleMemes().size()+1);
		
		final Bitmap bgBm = memeViewData.getBackground();
		
		final MemeViewData editableMeme = new MemeViewData();
		final ShallowMeme meme = new ShallowMeme();
		meme.setTopTextFontSize(getResources().getInteger(R.integer.initialFontSize));
		meme.setBottomTextFontSize(getResources().getInteger(R.integer.initialFontSize));
		
		editableMeme.setMeme(meme);
		editableMeme.setBackground(bgBm);
		
		shMemes.add(editableMeme);
		
		for (final ShallowMeme eaShMeme : memeViewData.getSampleMemes()) {
			final MemeViewData mvd = new MemeViewData();
			
			mvd.setBackground(bgBm);
			mvd.setMeme(eaShMeme);
			
			shMemes.add(mvd);
		}
		
		
		return shMemes;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_create_meme, menu);
		return true;
	}
	
	
	private ShallowMeme getSelectedMeme() {
		final int current = memePager.getCurrentItem();
		
		final ShallowMeme currentMeme = getMemePagerAdapter().getMemeAt(current).getMeme();
		
		return currentMeme;
	}
	
}