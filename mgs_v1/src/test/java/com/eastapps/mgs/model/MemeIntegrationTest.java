package com.eastapps.mgs.model;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.eastapps.mgs.util.TestUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Transactional
@RooIntegrationTest(entity = Meme.class)
public class MemeIntegrationTest extends AbstractIntegrationTest {

	private static final Logger logger = Logger.getLogger(MemeIntegrationTest.class);

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    MemeDataOnDemand dod;

	@Test
    public void testCountMemes() {
        Assert.assertNotNull("Data on demand for 'Meme' failed to initialize correctly", dod.getRandomMeme());
        long count = Meme.countMemes();
        Assert.assertTrue("Counter for 'Meme' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindMeme() {
        Meme obj = dod.getRandomMeme();
        Assert.assertNotNull("Data on demand for 'Meme' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Meme' failed to provide an identifier", id);
        obj = Meme.findMeme(id);
        Assert.assertNotNull("Find method for 'Meme' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Meme' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllMemes() {
        Assert.assertNotNull("Data on demand for 'Meme' failed to initialize correctly", dod.getRandomMeme());
        long count = Meme.countMemes();
        Assert.assertTrue("Too expensive to perform a find all test for 'Meme', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Meme> result = Meme.findAllMemes();
        Assert.assertNotNull("Find all method for 'Meme' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Meme' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindMemeEntries() {
        Assert.assertNotNull("Data on demand for 'Meme' failed to initialize correctly", dod.getRandomMeme());
        long count = Meme.countMemes();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Meme> result = Meme.findMemeEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Meme' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Meme' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Meme obj = dod.getRandomMeme();
        Assert.assertNotNull("Data on demand for 'Meme' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Meme' failed to provide an identifier", id);
        obj = Meme.findMeme(id);
        Assert.assertNotNull("Find method for 'Meme' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyMeme(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Meme' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        Meme obj = dod.getRandomMeme();
        Assert.assertNotNull("Data on demand for 'Meme' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Meme' failed to provide an identifier", id);
        obj = Meme.findMeme(id);
        boolean modified =  dod.modifyMeme(obj);
        Integer currentVersion = obj.getVersion();
        Meme merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Meme' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'Meme' failed to initialize correctly", dod.getRandomMeme());
        Meme obj = dod.getNewTransientMeme(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Meme' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Meme' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'Meme' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        Meme obj = dod.getRandomMeme();
        Assert.assertNotNull("Data on demand for 'Meme' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Meme' failed to provide an identifier", id);
        obj = Meme.findMeme(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Meme' with identifier '" + id + "'", Meme.findMeme(id));
    }
	
	@Test
	public void testCreateMemeJson() {
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
		
		final Long result = TestUtils.getJSONObject("/memes/create/json", meme, Long.class);
		
		TestCase.assertNotNull(result);
		TestCase.assertTrue(result > 0);
	}
	
	@Test
	public void testGetMemesForUser() {
		final MemeUser user = new MemeUser();
		user.setId(1L);
	
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
		
		meme.setCreatedByUser(user);
		
		final Long result = TestUtils.getJSONObject("/memes/create/json", meme, Long.class);
		
		TestCase.assertNotNull(result);
		TestCase.assertTrue(result > 0);
		
		@SuppressWarnings("unchecked")
		final List<Meme> memes = (List<Meme>) TestUtils.getJSONList(
			"/memes/get_for_user/json/1/10/1", 
			new TypeToken<Collection<MemeBackground>>(){}.getType()
		);
		
		TestCase.assertNotNull("memes returned for user is null", memes);
		TestCase.assertTrue("memes for user zero length", memes.size() > 0);
	}
	
//	@Test
//	public void testCreateSampleMeme() {
//		testCreateMemeJson();
//		
//		final Meme meme = Meme.findMeme(1L);
//		final SampleMeme sampleMeme = new SampleMeme();
//		sampleMeme.setBackground(meme.getMemeBackground());
//		sampleMeme.setSampleMeme(meme);
//		
//		TestUtils.getJSONObject("/samplememes/create/json", , responseType)
//	}
	

}




























