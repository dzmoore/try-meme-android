package com.eastapps.meme_gen_server;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

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

	@Before
	public void setUp() throws Exception {
		final Resource rsrc = new FileSystemResource(new File("src/test/resources/test-context.xml"));
		BeanFactory fac = new XmlBeanFactory(rsrc);

		sessionFactory = (SessionFactory) fac.getBean("mySessionFactory");
		imgsRoot = String.valueOf(fac.getBean("memeImagesRootDir"));
		thumbImgsRoot = String.valueOf(fac.getBean("memeThumbImagesRootDir"));
		
		memeSvc = new MemeService(sessionFactory, imgsRoot, thumbImgsRoot);
	}

	@After
	public void tearDown() throws Exception {
		if (sessionFactory != null) {
			sessionFactory.close();
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
	public void testGetUserFavTypes() {
		final int userId = 1;
		final List<ShallowMemeType> types = memeSvc.getFavoriteMemeTypesForUser(userId);
		
		TestCase.assertNotNull(types);
		TestCase.assertTrue(types.size() > 0);
		
		TestCase.assertTrue(types.get(0).getTypeId() > 0);
	}
	
	@Test
	public void testGetUserForId() {
		final int userId = 1;
		final ShallowUser user = memeSvc.getUser(userId);
		
		TestCase.assertNotNull(user);
		TestCase.assertTrue(user.getId() == userId);
	}
}




























