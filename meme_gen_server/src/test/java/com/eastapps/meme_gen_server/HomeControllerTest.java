package com.eastapps.meme_gen_server;

import java.io.File;
import java.util.HashSet;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.eastapps.meme_gen_server.constants.Constants;
import com.eastapps.meme_gen_server.domain.DeviceInfo;
import com.eastapps.meme_gen_server.domain.IntResult;
import com.eastapps.meme_gen_server.domain.LvMemeType;
import com.eastapps.meme_gen_server.domain.Meme;
import com.eastapps.meme_gen_server.domain.MemeBackground;
import com.eastapps.meme_gen_server.domain.MemeText;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.meme_gen_server.domain.User;
import com.eastapps.meme_gen_server.util.Util;

public class HomeControllerTest {
	private SessionFactory sessionFactory;
	private String imgsRoot;
	private static final Logger logger = Logger.getLogger(HomeControllerTest.class);

	@Ignore
	@Test
	public void testController() {
		HomeController controller = new HomeController(sessionFactory, imgsRoot);
		Model model = new ExtendedModelMap();
		Assert.assertEquals("home", controller.home(model));

		Object message = model.asMap().get("controllerMessage");
		Assert.assertEquals("This is the message from the controller!", message);

	}

	@Before
	public void setUp() throws Exception {
		final Resource rsrc = new FileSystemResource(new File("src/test/resources/test-context.xml"));
		BeanFactory fac = new XmlBeanFactory(rsrc);

		sessionFactory = (SessionFactory) fac.getBean("mySessionFactory");
		imgsRoot = String.valueOf(fac.getBean("memeImagesRootDir"));
	}

	@After
	public void tearDown() throws Exception {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}

	@Ignore
	@Test
	public void testGetMemeJson() {
		final Session sesh = sessionFactory.openSession();
		sesh.beginTransaction();
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
		m.getMemeTexts().add(new MemeText(m, topText, Constants.TOP, 26));
		
		final String bottomText = "bottomtext";
		m.getMemeTexts().add(new MemeText(m, bottomText, Constants.BOTTOM, 26));
		m.setUser(user);
		
		final HomeController controller = new HomeController(sessionFactory, imgsRoot);

		final IntResult result = controller.storeMeme(new ShallowMeme(m));
		Assert.assertNotNull(result);
		Assert.assertTrue(result.getResult() > Constants.INVALID);
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

	private Meme addNewMeme(
		final String topText,
		final String bottomText,
		final MemeBackground bg,
		final LvMemeType memeType,
		final User user,
		final Session sesh,
		final StringBuilder overallResult)
	{
		final MemeText memeTextTopText = createMemeTopText(topText, sesh);

		final MemeText memeTextBottomText = createBottomText(bottomText, sesh);

		createMemeBackground(bg, sesh);
		
		createMemeType(memeType, sesh);
		
		createUser(sesh);
		
		final Meme meme = new Meme();
		memeTextTopText.setMeme(meme);
		memeTextBottomText.setMeme(meme);
		meme.setLvMemeType(memeType);
		meme.setMemeBackground(bg);
		
		meme.getMemeTexts().add(memeTextTopText);
		meme.getMemeTexts().add(memeTextBottomText);
		meme.setUser(user);
		
		sesh.beginTransaction();

		meme.setId(Util.getId(sesh.save(meme)));
		Util.commit(sesh);
		
		overallResult.append("meme_store_result=[");
		overallResult.append(meme.toString()).append("]");


//		final SampleMeme sample = new SampleMeme();
//		sample.setMeme(meme);
//
//		sesh.beginTransaction();
//		try {
//			sample.setId((Integer) sesh.save(sample));
//		} catch (Exception e) {
//			logger.error("err", e);
//		}
//		Util.commit(sesh);
		
		return meme;
	}

	private MemeText createMemeTopText(final String topText, final Session sesh) {
		sesh.beginTransaction();
		
		final MemeText memeTextTopText = new MemeText();
		memeTextTopText.setText(topText);
		memeTextTopText.setTextType(Constants.TOP);
		try {
    		// save top text
			memeTextTopText.setId(Util.getId(sesh.save(memeTextTopText)));
			
		} catch (Exception e) {
			logger.error("err", e);
		}
		Util.commit(sesh);
		return memeTextTopText;
	}

	private MemeText createBottomText(final String bottomText, final Session sesh) {
		// save bottom text
		sesh.beginTransaction();
		final MemeText memeTextBottomText = new MemeText();
		memeTextBottomText.setText(bottomText);
		memeTextBottomText.setTextType(Constants.BOTTOM);
		try {
			memeTextBottomText.setId(Util.getId(sesh.save(memeTextBottomText)));
			
		} catch (Exception e) {
			logger.error("err", e);
		}
		Util.commit(sesh);
		return memeTextBottomText;
	}

	private MemeBackground createMemeBackground(final MemeBackground bg, final Session sesh) {
		bg.setActive(true);
		bg.setPath(imgsRoot);

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
	

}
