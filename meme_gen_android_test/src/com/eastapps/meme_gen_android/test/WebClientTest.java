package com.eastapps.meme_gen_android.test;

import junit.framework.TestCase;
import android.test.AndroidTestCase;

import com.eastapps.meme_gen_android.http.WebClient;
import com.eastapps.mgs.model.Meme;
import com.eastapps.mgs.model.MemeBackground;
import com.eastapps.mgs.model.MemeText;
import com.eastapps.mgs.model.MemeUser;
import com.eastapps.util.Conca;

public class WebClientTest extends AndroidTestCase {
	private static final String ADDR = "http://dylan-mint:8080/";
	private static final int USER_ID = 1;
	private static final int LV_MEME_TYPE_ID = 1;
	private static final int MEME_BACKGROUND_ID = 1;
	
	private WebClient webClient;
	
	@Override
	public void setUp() {
		webClient = new WebClient();
	}
	
	@Override
	public void tearDown() {
		try {
			super.tearDown();
		} catch (Exception e) { }
	}
	
	
	public void testSendRequestAsJson() {
		final String storeAddress = Conca.t(ADDR, "/memes/create/json");
		
		final Meme meme = new Meme();
		final MemeBackground memeBackground = new MemeBackground();
		memeBackground.setActive(true);
		memeBackground.setDescription("test background");
		memeBackground.setFilePath("test.path");
		meme.setMemeBackground(memeBackground);
		
		final MemeText topText = new MemeText();
		topText.setFontSize(26.0);
		topText.setText("top text");
		
		meme.setTopText(topText);
		
		final MemeText bottomText = new MemeText();
		bottomText.setFontSize(26.0);
		bottomText.setText("bottom text");
		meme.setBottomText(bottomText);
		
		final MemeUser user = new MemeUser();
		user.setId(1L);
		meme.setCreatedByUser(user);
		
		final Long id = webClient.sendRequestAsJson(storeAddress, meme, Long.class);
		
		TestCase.assertTrue(id > 0);
		
	}
	
	public static void main(String[] args) {
		WebClientTest test = new WebClientTest() {
			protected void runTest() throws Throwable {
				testSendRequestAsJson();
			};
		};
		test.run();
	}
	
	
	
	
	
}
