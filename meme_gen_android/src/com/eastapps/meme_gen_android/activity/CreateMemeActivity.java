package com.eastapps.meme_gen_android.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.domain.MemeViewData;
import com.eastapps.meme_gen_android.service.IMemeService;
import com.eastapps.meme_gen_android.service.impl.MemeService;
import com.eastapps.meme_gen_android.widget.OutlineTextView;
import com.eastapps.meme_gen_android.widget.adapter.CreateMemePagerAdapter;
import com.eastapps.meme_gen_server.domain.Meme;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.util.Conca;

public class CreateMemeActivity extends Activity {
	private static final String TAG = CreateMemeActivity.class.getSimpleName();

	private MemeViewData memeViewData;
	private AtomicBoolean isEditingTopText;
	private AtomicBoolean isEditingBottomText;
	private AtomicBoolean isTopTextSizing;
	private AtomicBoolean isBottomTextSizing;
	private EditText topTextEdit;
	private EditText bottomTextEdit;
	private SeekBar topSeekBar;
	private SeekBar bottomSeekBar;
	private IMemeService memeService;
	private ShallowMeme userMeme;
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
		
		if (!isLoadComplete.get()) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					memeViewData = memeService.createMemeViewData(1);//new MemeServerClient(CreateMemeActivity.this).createMemeViewData(9);
					
					setMemeViewData(memeViewData);
				}
			}).start();
			
		} else {
			setFieldsInMemeView();
			
			initGui();
		}
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
			outState.putParcelable("pageradapter", (CreateMemePagerAdapter)memePager.getAdapter());
		}
	}
	
	private void initGui() {
		initTopTextEdit();

		initBottomTextEdit();

		initTopTextBtn();

		initConfigTopTextBtn();

		initBottomTextBtn();

		initConfigBottomTextBtn();
		
		initSaveBtn();
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
		
		sm.setTopText(String.valueOf(getTopTextView().getText()));
		sm.setBottomText(String.valueOf(getBottomTextView().getText()));
		sm.setTopTextFontSize(topSeekBar.getProgress());
		sm.setBottomTextFontSize(bottomSeekBar.getProgress());
		
		return sm;
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

			bottomSeekBar = new SeekBar(this);
			bottomSeekBar.setLayoutParams(new TableLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT, 
				1f
			));
			
			getBottomTextLinearLayout().addView(bottomSeekBar, 0);
			
			bottomSeekBar.setVisibility(View.GONE);
			
			bottomSeekBar.setMax(getResources().getInteger(R.integer.maxFontSize)); 
			
			bottomSeekBar.setProgress((int) getMemeViewBottomTextView().getTextSize());

			createSeekBarChangeListener(bottomSeekBar, getMemeViewBottomTextView());
		}
	}

	private void initTopTextEdit() {
		if (topTextEdit == null) {
			topTextEdit = new EditText(this);
			topTextEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					final ShallowMeme meme = getMemePagerAdapter().getMemeAt(memePager.getCurrentItem()).getMeme();
					meme.setTopText(topTextEdit.getText().toString());
					
					getMemePagerAdapter().notifyDataSetChanged();
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});

			topTextEdit.setLayoutParams(new TableLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT, 1f));
			getTopTextLinearLayout().addView(topTextEdit, 0);
			topTextEdit.setVisibility(View.GONE);

			topSeekBar = new SeekBar(this);
			topSeekBar.setLayoutParams(new TableLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT, 1f));
			getTopTextLinearLayout().addView(topSeekBar, 0);
			topSeekBar.setVisibility(View.GONE);
			topSeekBar.setMax(getResources().getInteger(R.integer.maxFontSize));
			topSeekBar.setProgress((int) getMemeViewBottomTextView()
					.getTextSize());
			createSeekBarChangeListener(topSeekBar, getMemeViewTopTextView());

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
				textView.setTextSize(progress);
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
			getMemeViewBottomTextView(),
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
				v, getMemeViewTopTextView(), getTopTextBtn(),
				topTextEdit, isEditingTopText, isTopTextSizing, topSeekBar);
	}

	private LinearLayout getTopTextLinearLayout() {
		return (LinearLayout) findViewById(R.id.top_text_linear);
	}

	private LinearLayout getBottomTextLinearLayout() {
		return (LinearLayout) findViewById(R.id.bottom_text_linear);
	}

	private void handleConfigBtnClick(
			final View btnClicked,
			final TextView textViewToSet, 
			final Button textEditBtn,
			final EditText textEdit, 
			final AtomicBoolean isEditing,
			final AtomicBoolean isTextSizing, 
			final SeekBar seekBar) 
	{
		// if editing --> stop editing
		if (isEditing.get()) {
			final String newText = textEdit.getText().toString();
			// set new text in the meme view
			textViewToSet.setText(newText);

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
		final CreateMemePagerAdapter pagerAdapter = new CreateMemePagerAdapter();
		pagerAdapter.setMemes(createSampleMemeViewDataList());
		
		memePager.setAdapter(pagerAdapter);
		
		pagerAdapter.loadIntoView(memePager);
		
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
		final int currentPageIndex = memePager.getCurrentItem();
		final CreateMemePagerAdapter createMemePagerAdapter = getMemePagerAdapter();
		final ShallowMeme selectedMeme = createMemePagerAdapter.getMemeAt(currentPageIndex).getMeme();
		
		topSeekBar.setProgress(selectedMeme.getTopTextFontSize());
		bottomSeekBar.setProgress(selectedMeme.getBottomTextFontSize());
		
		getTopTextView().setText(selectedMeme.getTopText());
		getBottomTextView().setText(selectedMeme.getBottomText());
		

	}

	private CreateMemePagerAdapter getMemePagerAdapter() {
		return (CreateMemePagerAdapter)memePager.getAdapter();
	}
	


	private List<MemeViewData> createSampleMemeViewDataList() {
		final List<MemeViewData> shMemes 
			= new ArrayList<MemeViewData>(memeViewData.getSampleMemes().size()+1);
		
		final Bitmap bgBm = memeViewData.getBackground();
		
		final MemeViewData editableMeme = new MemeViewData();
		final ShallowMeme meme = new ShallowMeme();
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
	
	private View getCurrentPage() {
		final int currentPageIndex = memePager.getCurrentItem();
		final View currentPage = memePager.getChildAt(currentPageIndex);
		return currentPage; 
	}
	
	private View getPageAt(final int pos) {
		return memePager.getChildAt(pos);
	}
	
	private OutlineTextView getMemeViewBottomTextView() {
		return (OutlineTextView)getCurrentPage().findViewById(R.id.bottom_text_textview);
	}

	private OutlineTextView getMemeViewTopTextView() {
		return (OutlineTextView)getCurrentPage().findViewById(R.id.top_text_textview);
	}
	
	private TextView getTopTextView() {
		return (TextView)getCurrentPage().findViewById(R.id.top_text_textview);
	}
	
	private TextView getBottomTextView() {
		return (TextView)getCurrentPage().findViewById(R.id.bottom_text_textview);
	}
	
}
