package com.eastapps.meme_gen_android.test;

import java.util.List;

import com.eastapps.meme_gen_android.domain.MemeListItemData;
import com.eastapps.meme_gen_android.service.impl.MemeService;
import com.eastapps.meme_gen_android.util.StringUtils;
import com.eastapps.meme_gen_server.domain.ShallowMeme;

import android.test.AndroidTestCase;

public class MemeServiceTest extends AndroidTestCase  {
	private MemeService memeSvc;
	
	@Override
	public void setUp() {
		MemeService.initialize(getContext());
		memeSvc = MemeService.getInstance();
	}
	
	@Override
	public void tearDown() {
	}
	
	public void testStoreMeme() {
		ShallowMeme shallowMeme = new ShallowMeme();
		shallowMeme.setBackgroundFk(1);
		shallowMeme.setBottomText("bottom text test");
		shallowMeme.setTopText("top text test");
		shallowMeme.setTopTextFontSize(26);
		shallowMeme.setBottomTextFontSize(26);
		shallowMeme.setMemeTypeId(1);
		shallowMeme.setUserId(1);
		final int resultId = memeSvc.storeMeme(shallowMeme);
		
		assertTrue(resultId > 0);
	}
	
	public void testGetMeme() {
		final int id = 1;
		ShallowMeme shMeme = memeSvc.getMeme(id);
		assertNotNull(shMeme);
		assertTrue(shMeme.getId() == id);
		assertTrue(StringUtils.isNotBlank(shMeme.getTopText()));
	}
	
	public void testGetSampleMemes() {
		final int id = 1;
		final List<ShallowMeme> samples = memeSvc.getSampleMemes(id);
		
		assertNotNull(samples);
		assertTrue(samples.size() > 0);
		
		for (final ShallowMeme eaSamp : samples) {
			assertNotNull(eaSamp);
			assertTrue(eaSamp.getMemeTypeId() == id);
			assertTrue(eaSamp.getId() > 0);
		}
	}
	
	public void testGetMemeTypes() {
		final int id = 1;
		final List<MemeListItemData> types = memeSvc.getAllMemeTypesListData();
		assertNotNull(types);
		assertTrue(types.size() > 0);
		
		assertTrue(types.get(0).getThumb() != null);
	}

	
	public static void main(String[] args) {
		MemeServiceTest test = new MemeServiceTest() {
			protected void runTest() throws Throwable {
				testStoreMeme();
			};
		};
		test.run();
	}
}
