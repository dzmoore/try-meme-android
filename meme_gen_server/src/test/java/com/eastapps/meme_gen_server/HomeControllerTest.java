package com.eastapps.meme_gen_server;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eastapps.meme_gen_server.domain.DeviceInfo;
import com.eastapps.meme_gen_server.domain.IntResult;
import com.eastapps.meme_gen_server.domain.LvMemeType;
import com.eastapps.meme_gen_server.domain.Meme;
import com.eastapps.meme_gen_server.domain.MemeBackground;
import com.eastapps.meme_gen_server.domain.MemeText;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.meme_gen_server.domain.User;
import com.eastapps.meme_gen_server.service.MemeService;
import com.eastapps.meme_gen_server.util.Util;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	Constants.ROOT_CONTEXT,
	Constants.SERVLET_CONTEXT
})
public class HomeControllerTest {
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private String memeImagesRootDir;
	
	@Resource 
	private ApplicationContext appContext;
	
	@Autowired
	private MemeService memeService;
	
	private static final Logger logger = Logger.getLogger(HomeControllerTest.class);
	private HomeController homeCtrllr;
	
	@Before
	public void setUp() throws Exception {
		homeCtrllr = new HomeController(memeService);
	}

	@After
	public void tearDown() throws Exception {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}

	@Test
	public void testGetMemeJson() {
		final int id = 1;
		final ShallowMeme shMeme = homeCtrllr.getMeme(id);
		
		TestCase.assertNotNull(shMeme);
		TestCase.assertTrue(shMeme.getId() == id);
		TestCase.assertTrue(shMeme.getBackgroundFk() > 0);
		
	}

	@Test
	public void testStoreMeme() {
		final Session sesh = sessionFactory.openSession();
	
		final User user = createUser(sesh);
		final MemeBackground bg = createMemeBackground(new MemeBackground(), sesh);
		final LvMemeType memeType = createMemeType(new LvMemeType(), sesh);

		final Meme m = new Meme();
		m.setLvMemeType(memeType);
		m.setMemeBackground(bg);
		
		final String topText = "toptext";
		m.getMemeTexts().add(new MemeText(m, topText, com.eastapps.meme_gen_server.constants.Constants.TOP, 26));
		
		final String bottomText = "bottomtext";
		m.getMemeTexts().add(new MemeText(m, bottomText, com.eastapps.meme_gen_server.constants.Constants.BOTTOM, 26));
		m.setUser(user);
		
		final HomeController controller = homeCtrllr;

		final IntResult result = controller.storeMeme(new ShallowMeme(m));
		Assert.assertNotNull(result);
		Assert.assertTrue(result.getResult() > com.eastapps.meme_gen_server.constants.Constants.INVALID);
		m.setId(result.getResult());
		
		sesh.beginTransaction();
		final Query qry = sesh.createQuery("from Meme where id = :id");
		qry.setInteger("id", m.getId());
		
		@SuppressWarnings("unchecked")
		final List<Meme> memes = qry.list();
		
		Assert.assertNotNull(memes);
		Assert.assertTrue(memes.size() == 1);
		
		final Meme storedMeme = memes.get(0);
		Assert.assertNotNull(storedMeme);
		Assert.assertTrue(storedMeme.getId().equals(m.getId()));
		
		Assert.assertNotNull(storedMeme.getMemeTexts());
		Assert.assertTrue(storedMeme.getMemeTexts().size() == 2);
		
		for (MemeText eaTxt : storedMeme.getMemeTexts()) {
			Assert.assertTrue(StringUtils.equals(eaTxt.getText(), topText) || 
				StringUtils.equals(eaTxt.getText(), bottomText));
			
			Assert.assertTrue(eaTxt.getId() > 0);
		}
	}


	private MemeBackground createMemeBackground(final MemeBackground bg, final Session sesh) {
		bg.setActive(true);
		bg.setPath(memeImagesRootDir);

		// store meme_background
		sesh.beginTransaction();
		bg.setId(Util.getId(sesh.save(bg)));
		Util.commit(sesh);
		
		return bg;
	}

	private LvMemeType createMemeType(final LvMemeType memeType, final Session sesh) {
		memeType.setActive(true);
		memeType.setDescr("description");
		
		// store new lv_meme_type
		sesh.beginTransaction();
		memeType.setId(Util.getId(sesh.save(memeType)));
		Util.commit(sesh);
		
		return memeType;
	}

	private User createUser(final Session sesh) {
		sesh.beginTransaction();
		// store new user
		final User testUser = new User();
		testUser.setActive(true);
		testUser.setPassword("password");
		testUser.setSalt("salty");
		testUser.setUsername(StringUtils.substring(String.valueOf(System.currentTimeMillis()), 0, 40));
		testUser.setId(Util.getId(sesh.save(testUser)));
		Util.commit(sesh);

		// create device info
		sesh.beginTransaction();
		final HashSet<DeviceInfo> deviceInfos = new HashSet<DeviceInfo>();
		final DeviceInfo devInfo = new DeviceInfo();
		devInfo.setUniqueId("ABCXYZ unique device id ABCXYZ");
		devInfo.setUser(testUser);
		devInfo.setId(Util.getId(sesh.save(devInfo)));
		Util.commit(sesh);

		// link device info to user
		sesh.beginTransaction();
		testUser.setDeviceInfos(deviceInfos);
		sesh.update(testUser);
		Util.commit(sesh);
		
		return testUser;
	}
	
	@Test
	public void testGetSamples() {
		final int memeTypeId = 1;
		
		HomeController cntrller = homeCtrllr;
		
		ShallowMeme[] samples = cntrller.getSampleMemes(memeTypeId);
		
		TestCase.assertNotNull(samples);
		TestCase.assertTrue(samples.length > 0);
		
		for (ShallowMeme ea : samples) {
			TestCase.assertNotNull(ea);
			TestCase.assertTrue(ea.getMemeTypeId() == memeTypeId);
		}
	}
	
	@Test
	public void testSaveFavType() {
		final int userId = 1;
		final int typeId = 1;
		
		final boolean success = homeCtrllr.storeFavType(userId, typeId);
		TestCase.assertTrue(success);
		
		TestCase.assertTrue(doesUserHasFavTypeId(userId, typeId));
		
	}

	private boolean doesUserHasFavTypeId(final int userId, final int typeId) {
		final ShallowMemeType[] favs = homeCtrllr.getFavTypesForUserId(userId);
		boolean favTypeFound = false;
		for (final ShallowMemeType ea : favs) {
			if (ea.getTypeId() == typeId) {
				favTypeFound = true;
				break;
			}
		}
		
		return favTypeFound;
	}
	
	@Test
	public void testRemoveFavType() {
		final int userId = 1;
		final int typeId = 1;
		
		TestCase.assertTrue(homeCtrllr.storeFavType(userId, typeId));
		
		final boolean success = homeCtrllr.removeFavType(userId, typeId);
		TestCase.assertTrue(success);
		
		TestCase.assertFalse(doesUserHasFavTypeId(userId, typeId));
	}
	
	@Test
	public void testGetPopularTypes() {
		final ShallowMemeType[] popTypes = homeCtrllr.getPopularTypes();
		TestCase.assertNotNull(popTypes);
		TestCase.assertTrue(popTypes.length > 0);
	}
	
	@Test 
	public void testGetTypesForSearch() {
		final String searchTerm = "world";
		final ShallowMemeType[] searchResults = homeCtrllr.getTypesForSearch(searchTerm);
		TestCase.assertNotNull(searchResults);
		TestCase.assertTrue(searchResults.length > 0);
	}

}


























