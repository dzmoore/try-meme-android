package com.eastapps.meme_gen_android.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
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
	
	
	private ViewPager memePager;

	public CreateMemeActivity() {
		super();

		isEditingTopText = new AtomicBoolean(false);
		isEditingBottomText = new AtomicBoolean(false);

		isTopTextSizing = new AtomicBoolean(false);
		isBottomTextSizing = new AtomicBoolean(false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_meme_layout);
		
		MemeService.initialize(this);
		memeService = MemeService.getInstance();		
		
		initTopTextEdit();

		initBottomTextEdit();

		initTopTextBtn();

		initConfigTopTextBtn();

		initBottomTextBtn();

		initConfigBottomTextBtn();
		
		initSaveBtn();

		new Thread(new Runnable() {
			@Override
			public void run() {
				memeViewData = memeService.createMemeViewData(6);//new MemeServerClient(CreateMemeActivity.this).createMemeViewData(9);
				
				setMemeViewData(memeViewData);
			}
		}).start();
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
					getMemeViewBottomTextView().setText(bottomTextEdit.getText().toString());
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
			
//			bottomSeekBar.setProgress((int) getMemeViewBottomTextView().getTextSize());
//
//			createSeekBarChangeListener(bottomSeekBar, getMemeViewBottomTextView());
		}
	}

	private void initTopTextEdit() {
		if (topTextEdit == null) {
			topTextEdit = new EditText(this);
			topTextEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					getMemeViewTopTextView().setText(
							topTextEdit.getText().toString());
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
			final LinearLayout parentLayout, 
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
		startEditing(v, getConfigTopTextBtn(), topTextEdit,
				getTopTextLinearLayout(), isEditingTopText);
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

		final TextView topTxtView = getMemeViewTopTextView();
		final TextView bottomTxtView = getMemeViewBottomTextView();

		final ImageView imgView = (ImageView) findViewById(R.id.image_view);
		
		memePager = (ViewPager)findViewById(R.id.meme_view_pager);
		
		Typeface robotoBoldCondensedTypeFace = Typeface.createFromAsset(
			getAssets(), 
			"fonts/Roboto-BoldCondensed.ttf"
		);

		setFieldsInMemeView(
			topTxtView, 
			bottomTxtView, 
			imgView, 
			memePager,
			robotoBoldCondensedTypeFace
		);
	}

	private void setFieldsInMemeView(final TextView topTxtView,
			final TextView bottomTxtView, final ImageView imgView,
			final ViewPager memePager, Typeface robotoBoldCondensedTypeFace) 
	{
//		topTxtView.setTypeface(robotoBoldCondensedTypeFace);
//		topTxtView.setTextSize(
//			getResources().getInteger(
//				R.integer.initialFontSize
//		));
//
//		bottomTxtView.setTypeface(robotoBoldCondensedTypeFace);
//		bottomTxtView.setTextSize(
//			getResources().getInteger(
//				R.integer.initialFontSize
//		));
//
//		topTxtView.setText(memeViewData.getMeme().getTopText());
//		bottomTxtView.setText(memeViewData.getMeme().getBottomText());
//		imgView.setImageBitmap(memeViewData.getBackground());
		
//		topTextEdit.setText(topTxtView.getText());
//		bottomTextEdit.setText(bottomTxtView.getText());
		
		final CreateMemePagerAdapter pagerAdapter = new CreateMemePagerAdapter();
		pagerAdapter.setMemes(createSampleMemeViewDataList());
		
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
			= new ArrayList<MemeViewData>(memeViewData.getSampleMemes().size());
		
		final Bitmap bgBm = memeViewData.getBackground();
		
		final MemeViewData editableMeme = new MemeViewData();
		final ShallowMeme meme = new ShallowMeme();
		editableMeme.setMeme(meme);
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
