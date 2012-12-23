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
	
	private void loadBundle(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			return;
		}
		
		memeViewData = new MemeViewData();
		if (savedInstanceState.containsKey("memebg")) {
			final Bitmap bg = (Bitmap)savedInstanceState.getParcelable("memebg");
			memeViewData.setBackground(bg);
		}
		
		if (savedInstanceState.containsKey("samplememes")) {
			@SuppressWarnings("unchecked")
			final ArrayList<ShallowMeme> shMemes = (ArrayList<ShallowMeme>) savedInstanceState.getSerializable("samplememes");
			
			memeViewData.setSampleMemes(shMemes);
		}
		
		if (savedInstanceState.containsKey("loaded")) {
			isLoadComplete.set(savedInstanceState.getBoolean("loaded"));
		}
		
		if (savedInstanceState.containsKey("pageradapter")) {
			memePager.setAdapter((PagerAdapter) savedInstanceState.getParcelable("pageradapter"));
		}
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		if (memeViewData != null) {
			outState.putParcelable("memebg", memeViewData.getBackground());
			outState.putSerializable("samplememes", new ArrayList<ShallowMeme>(memeViewData.getSampleMemes()));
			outState.putBoolean("loaded", isLoadComplete.get());
			outState.putParcelable("pageradapter", getMemePagerAdapter());
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
		createSeekBarChangeListener(topSeekBar, getSelectedTopTextView());
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

		createSeekBarChangeListener(bottomSeekBar, getSelectedBottomTextView());
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
		final ShallowMeme shallowMeme = createShallowMemeFromUi();
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

	private ShallowMeme createShallowMemeFromUi() {
		final ShallowMeme sm = memeViewData.getMeme();
		
		sm.setTopText(getSelectedTopTextView().getText().toString());
				
		sm.setBottomText(getSelectedBottomTextView().getText().toString());
		
		sm.setTopTextFontSize(topSeekBar.getProgress());
		sm.setBottomTextFontSize(bottomSeekBar.getProgress());
		
		return sm;
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
						
//						getSelectedMeme().setBottomText(newBottomText);
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
						
//						getSelectedMeme().setTopText(newTopText);
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

	private void createSeekBarChangeListener(
			final SeekBar seekBar,
			final OutlineTextView textView) 
	{
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
				if (textView != null) {
//					if (textView == getSelectedTopTextView()) {
//						getSelectedTopTextView().setTextSize(progress);
//						
//					} else {
//						getSelectedBottomTextView().setTextSize(progress);
//					}
					
					textView.setTextSize(progress);
				}
			}
		});
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
//		final CreateMemePagerAdapter pagerAdapter = new CreateMemePagerAdapter();
//		pagerAdapter.setMemes(createSampleMemeViewDataList());
		
		final MemePagerFragmentAdapter pagerAdapter 
			= new MemePagerFragmentAdapter(getSupportFragmentManager());
		
		sampledList = createSampleMemeViewDataList();
		pagerAdapter.setMemes(sampledList);
		
		memePager.setAdapter(pagerAdapter);
		
//		pagerAdapter.loadIntoView(memePager);
		
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
//					loadCurrentlySelectedPage();
				}
			}
		});
	}
	
	private void loadCurrentlySelectedPage() {
		final int currentPageIndex = memePager.getCurrentItem();
		final MemePagerFragmentAdapter createMemePagerAdapter = getMemePagerAdapter();
		final ShallowMeme selectedMeme = createMemePagerAdapter.getMemeAt(currentPageIndex).getMeme();
		
		topSeekBar.setProgress(selectedMeme.getTopTextFontSize());
		bottomSeekBar.setProgress(selectedMeme.getBottomTextFontSize());
		
//		getSelectedMeme().setTopText(selectedMeme.getTopText()); 
//		getSelectedMeme().setBottomText(selectedMeme.getBottomText());
		
//		getTopMemeTextFragment().setText(selectedMeme.getTopText());
//		getBottomMemeTextFragment().setText(selectedMeme.getBottomText());
		

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
//		meme.setTopText("<usertop>");
//		meme.setBottomText("<userbottom>");
		
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
	
//	private MemeViewFragment getCurrentPage() {
//		final int currentPageIndex = memePager.getCurrentItem();
//		
//		return (MemeViewFragment) getMemePagerAdapter().getItem(currentPageIndex);
//	}
	
	private ShallowMeme getSelectedMeme() {
		final int current = memePager.getCurrentItem();
		
		ShallowMeme currentMeme = getMemePagerAdapter().getMemeAt(current).getMeme();
		
//		if (memeViewData != null) {
//			if (current == 0) {
//				currentMeme = memeViewData.getMeme();
//				
//			} else if (current > 0 && current < memeViewData.getSampleMemes().size() + 1) {
//				currentMeme = memeViewData.getSampleMemes().get(current - 1);
//			}
//			
//		}
		
		return currentMeme;
	}
	
//	private View getPageAt(final int pos) {
//		return memePager.getChildAt(pos);
//	}
	
//	private MemeTextFragment getBottomMemeTextFragment() {
//		return (MemeTextFragment)getSupportFragmentManager().findFragmentByTag("toptextfrag");
//	}
//
//	private MemeTextFragment getTopMemeTextFragment() {
//		return (MemeTextFragment)getSupportFragmentManager().findFragmentByTag("bottomtextfrag");
//	}
	
}
