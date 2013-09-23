package com.eastapps.meme_gen_android.test;

import java.util.List;

import junit.framework.TestCase;
import android.graphics.Bitmap;
import android.test.AndroidTestCase;

import com.eastapps.meme_gen_android.mgr.CacheMgr;
import com.eastapps.meme_gen_android.web.IMemeServerClient;
import com.eastapps.meme_gen_android.web.MemeServerClientV2;
import com.eastapps.mgs.model.Meme;
import com.eastapps.mgs.model.MemeBackground;
import com.eastapps.mgs.model.MemeText;
import com.eastapps.mgs.model.MemeUser;
import com.eastapps.util.Conca;

public class MemeServerClientTest extends AndroidTestCase  {
	private static final String ADDR = "http://dylan-mint:8080/";

	private IMemeServerClient memeServerClient;
	
	@Override
	public void setUp() {
		MemeServerClientV2.initialize(getContext());
		memeServerClient = MemeServerClientV2.getInstance();
		
		CacheMgr.initialize(getContext());
	}
	
	@Override
	public void tearDown() {
	}
	
	public void testStoreMeme() {
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
		
		final Long id = memeServerClient.storeMeme(meme);
		
		TestCase.assertTrue(id > 0);
	}
	
	public void testGetBackground() {
		final Meme meme = new Meme();
		final MemeBackground memeBackground = new MemeBackground();
		memeBackground.setActive(true);
		memeBackground.setDescription("test background");
		memeBackground.setFilePath("College_Freshman.jpg");
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
		
		final Long id = memeServerClient.storeMeme(meme);
		
		TestCase.assertTrue(id > 0);
		
		final Bitmap bgBitmap = memeServerClient.getBackground(memeBackground.getFilePath());
		TestCase.assertNotNull(bgBitmap);
	}
	
	public void testGetPopularBackgrounds() {
		final List<MemeBackground> memeBackgrounds = memeServerClient.getPopularMemeBackgrounds("test");
		TestCase.assertNotNull("popular memebackgrounds null", memeBackgrounds);
		TestCase.assertTrue("Popular meme backgrounds zero length", memeBackgrounds.size() > 0);
	}
	
	public void testStoreMemeUser() {
		final MemeUser memeUser = new MemeUser();
    	memeUser.setActive(true);
    	memeUser.setUsername("test");
    	memeUser.setPassword("password");
    	memeUser.setSalt("salt");
    	
    	final long id = memeServerClient.storeNewUser(memeUser);
    	TestCase.assertTrue("Create meme user: id is not greater than 0", id > 0);
	}

	public void testListMemeBackgrounds() {
		final List<MemeBackground> memeBackgrounds = memeServerClient.getAllMemeBackgrounds();
		
		TestCase.assertNotNull("get all meme backgrounds: null result", memeBackgrounds);
		TestCase.assertTrue("get all meme backgrounds: result list zero length", memeBackgrounds.size() > 0);
	}
	
	public void testListSampleMemesForBackgroundId() {
		final List<Meme> sampleMemes = memeServerClient.getSampleMemes(1);
		
		TestCase.assertNotNull("sample memes for background id: result is null", sampleMemes);
		TestCase.assertTrue("sample memes for backgroundid: result list is zero size", sampleMemes.size() > 0);
	}
	
	public void testFavoriteMemeBackgroundsForUserId() {
		final List<MemeBackground> favoriteMemeBackgrounds = memeServerClient.getFavMemeBackgroundsForUser(1);
		
		TestCase.assertNotNull("fav memes backgrounds for user id: result is null", favoriteMemeBackgrounds);
		TestCase.assertTrue("fav memes backgrounds for user id: result list is zero size", favoriteMemeBackgrounds.size() > 0);
	}
	
	public void testStoreFavoriteMemeBackground() {
		final boolean success = memeServerClient.storeFavMemeBackground(1, 1);
		
		TestCase.assertTrue("store fav meme background returned false", success);
	}
}



















