package com.eastapps.meme_gen_android.test;


public class MemeServiceTest {//extends AndroidTestCase  {
//	private MemeService memeSvc;
//	
//	@Override
//	public void setUp() {
//		MemeService.initialize(getContext());
//		memeSvc = MemeService.getInstance();
//		
//		CacheMgr.initialize(getContext());
//	}
//	
//	@Override
//	public void tearDown() {
//	}
//	
//	public void testStoreMeme() {
//		final Meme meme = new wMeme();
//		meme.setBackgroundFk(1);
//		meme.setBottomText("bottom text test");
//		meme.setTopText("top text test");
//		meme.setTopTextFontSize(26);
//		meme.setBottomTextFontSize(26);
//		meme.setMemeTypeId(1);
//		meme.setUserId(1);
//		final int resultId = memeSvc.storeMeme(meme);
//		
//		assertTrue(resultId > 0);
//	}
//	
//	public void testGetMeme() {
//		final int id = 1;
//		ShallowMeme shMeme = memeSvc.getMeme(id);
//		assertNotNull(shMeme);
//		assertTrue(shMeme.getId() == id);
//		assertTrue(StringUtils.isNotBlank(shMeme.getTopText()));
//	}
//	
//	public void testGetSampleMemes() {
//		final int id = 1;
//		final List<ShallowMeme> samples = memeSvc.getSampleMemes(id);
//		
//		assertNotNull(samples);
//		assertTrue(samples.size() > 0);
//		
//		for (final ShallowMeme eaSamp : samples) {
//			assertNotNull(eaSamp);
//			assertTrue(eaSamp.getMemeTypeId() == id);
//			assertTrue(eaSamp.getId() > 0);
//		}
//	}
//	
//	public void testGetMemeTypes() {
//		final int id = 1;
//		final List<MemeListItemData> types = memeSvc.getAllMemeTypesListData();
//		assertNotNull(types);
//		assertTrue(types.size() > 0);
//		
//		assertTrue(types.get(0).getThumb() != null);
//	}
//
//	public void testGetUserForId() {
//		final int id = 1;
//		final ShallowUser u = memeSvc.getUser(id);
//		
//		assertNotNull(u);
//		assertTrue(u.getId() == id);
//		assertTrue(StringUtils.isNotBlank(u.getUsername()));
//	}
//	
//	public void testGetNewInstallKey() {
//		final String newInstallKey = memeSvc.getNewInstallKey();
//		
//		assertTrue(StringUtils.isNotBlank(newInstallKey));
//	}
//	
//	public void testStoreNewUser() {
//		final String newInstallKey = memeSvc.getNewInstallKey();
//		assertTrue(StringUtils.isNotBlank(newInstallKey));
//		
//		final ShallowUser user = new ShallowUser();
//		user.setUsername(newInstallKey);
//		user.setInstallKey(newInstallKey);
//		
//		final int userId = memeSvc.storeNewUser(user);
//		assertTrue(userId > 0);
//	}
//	
//	public void testGetFavMemes() {
//		final int typeId = 1;
//		
//		final int userId = createNewUser();
//		
//		assertTrue(memeSvc.storeFavType(userId, typeId));
//		final List<ShallowMemeType> results = memeSvc.getFavMemeTypesForUser(userId);
//		
//		assertNotNull(results);
//		assertTrue(results.size() > 0);
//		assertTrue(StringUtils.isNotBlank(results.get(0).getTypeDescr()));
//	}
//
//	private int createNewUser() {
//		final String newInstallKey = memeSvc.getNewInstallKey();
//		assertTrue(StringUtils.isNotBlank(newInstallKey));
//		
//		final ShallowUser user = new ShallowUser();
//		user.setUsername(newInstallKey);
//		user.setInstallKey(newInstallKey);
//		
//		final int userId = memeSvc.storeNewUser(user);
//		assertTrue(userId > 0);
//		return userId;
//	}
//	
//	public void testStoreFavType() {
//		final int typeId = 1;
//		
//		final int userId = createNewUser();
//		
//		final boolean saveSuccess = memeSvc.storeFavType(userId, typeId);
//		
//		assertTrue(saveSuccess);
//		
//		final List<ShallowMemeType> favTypes = memeSvc.getFavMemeTypesForUser(userId);
//		
//		assertNotNull(favTypes);
//		assertTrue(favTypes.size() == 1);
//		assertTrue(favTypes.get(0).getTypeId() == typeId);
//	}
//	
//	public void testRemoveFavType() {
//		final int typeId = 1;
//
//		final int userId = createNewUser();
//
//		final boolean saveSuccess = memeSvc.storeFavType(userId, typeId);
//
//		assertTrue(saveSuccess);
//
//		final boolean removeSuccess = memeSvc.removeFavType(userId, typeId);
//		assertTrue(removeSuccess);
//	}
//	
//	public void testGetPopularTypes() {
//		final List<ShallowMemeType> popTypes = memeSvc.getPopularTypes();
//		assertNotNull(popTypes);
//		assertTrue(popTypes.size() > 0);
//	}
//	
//	public void testGetTypesForSearch() {
//		final String searchTerm = "world";
//		
//		final List<ShallowMemeType> types = memeSvc.getTypesForSearch(searchTerm);
//		
//		assertNotNull(types);
//		assertTrue(types.size() > 0);
//		
//	}
}





















