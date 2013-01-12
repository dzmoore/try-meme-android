package com.eastapps.meme_gen_server;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.eastapps.meme_gen_server.domain.Meme;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.meme_gen_server.domain.ShallowUser;
import com.eastapps.meme_gen_server.service.MemeService;

public class MemeServiceTest {
	private SessionFactory sessionFactory;
	private String imgsRoot;
	private MemeService memeSvc;
	private String thumbImgsRoot;
	private Long installKeyTimeoutMs;

	@Before
	public void setUp() throws Exception {
		final Resource rsrc = new FileSystemResource(new File("src/test/resources/test-context.xml"));
		BeanFactory fac = new XmlBeanFactory(rsrc);

		sessionFactory = (SessionFactory) fac.getBean("mySessionFactory");
		imgsRoot = String.valueOf(fac.getBean("memeImagesRootDir"));
		thumbImgsRoot = String.valueOf(fac.getBean("memeThumbImagesRootDir"));
		installKeyTimeoutMs = (Long) fac.getBean("installKeyTimeoutMs");
		
		memeSvc = new MemeService(sessionFactory, imgsRoot, thumbImgsRoot, installKeyTimeoutMs);
	}

	@After
	public void tearDown() {
		try {
    		if (sessionFactory != null) {
    			sessionFactory.close();
    		}
		} catch (Throwable e) {
			
		}
	}
	
	@Test
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
		
		TestCase.assertTrue(resultId > 0);
	}
	
	@Test
	public void testGetMeme() {
		sessionFactory.openSession();
		
		final int idToGet = 1;
		final Meme meme = memeSvc.getMeme(idToGet);
		TestCase.assertNotNull(meme);
		TestCase.assertTrue(meme.getId() == idToGet);
		
		sessionFactory.close();
	}
	
	@Test
	public void testGetMemeTypes() {
		final List<ShallowMemeType> types = memeSvc.getAllMemeTypes();
		
		TestCase.assertNotNull(types);
		TestCase.assertTrue(types.size() > 0);
		
		final Set<Integer> idSet = new HashSet<Integer>();
		for (final ShallowMemeType eaType : types) {
			TestCase.assertFalse(
				"id " + eaType.getTypeId() + " already exists in id list", 
				idSet.contains(eaType.getTypeId())
			);
			
			idSet.add(eaType.getTypeId());
		}
	}
	
	@Test
	public void testGetMemeTypeBackground() {
		final byte[] thumbBytes = memeSvc.getThumbForType(1);
		TestCase.assertNotNull(thumbBytes);
		TestCase.assertTrue(thumbBytes.length > 0);
	}
	
	
	@Test
	public void testStoreAndGetUserFavTypes() {
		final int userId = createNewUser();
		final int typeId = 1;
		
		TestCase.assertTrue(memeSvc.saveFavType(userId, typeId));
		
		final List<ShallowMemeType> types = memeSvc.getFavoriteMemeTypesForUser(userId);
		
		TestCase.assertNotNull(types);
		TestCase.assertTrue(types.size() > 0);
		
		TestCase.assertTrue(types.get(0).getTypeId() == typeId);
		TestCase.assertTrue(StringUtils.isNotBlank(types.get(0).getTypeDescr()));
	}
	
	@Test
	public void testGetUserForId() {
		final int userId = 1;
		final ShallowUser user = memeSvc.getUser(userId);
		
		TestCase.assertNotNull(user);
		TestCase.assertTrue(user.getId() == userId);
	}
	
	@Test
	public void testNewInstallKey() throws InterruptedException {
		final Set<String> installKeySet = Collections.synchronizedSet(new HashSet<String>());
		
		final int threadCount = 3;
		final int newKeysToGrabPerThread = 25;
		
		final List<Thread> threadList = new ArrayList<Thread>();
		for (int i = 0; i < threadCount; i++) {
			threadList.add(new Thread(
				new Runnable() {
					@Override
					public void run() {
						for (int j = 0; j < newKeysToGrabPerThread; j++) {
							TestCase.assertTrue(installKeySet.add(memeSvc.getNewInstallKey()));
						}
					}
				}
			));
		}
		
		for (final Thread ea : threadList) {
			ea.start();
		}
		
		for (final Thread ea : threadList) {
			ea.join();
		}
	}
	
	@Test
	public void testStoreUser() {
		final int newId = createNewUser();
		
		TestCase.assertTrue(newId > 0);
	}

	private int createNewUser() {
		final ShallowUser u = new ShallowUser();
		u.setInstallKey(memeSvc.getNewInstallKey());
		
		TestCase.assertTrue(StringUtils.isNotBlank(u.getInstallKey()));
		
		u.setUsername(u.getInstallKey());
		
		final int newId = memeSvc.storeNewUser(u);
		return newId;
	}
}




























