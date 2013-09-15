package com.eastapps.mgs.model;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Transactional
@Configurable
@RooIntegrationTest(entity = MemeUserFavorite.class)
public class MemeUserFavoriteIntegrationTest extends AbstractIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    MemeUserFavoriteDataOnDemand dod;

	@Test
    public void testCountMemeUserFavorites() {
        Assert.assertNotNull("Data on demand for 'MemeUserFavorite' failed to initialize correctly", dod.getRandomMemeUserFavorite());
        long count = MemeUserFavorite.countMemeUserFavorites();
        Assert.assertTrue("Counter for 'MemeUserFavorite' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindMemeUserFavorite() {
        MemeUserFavorite obj = dod.getRandomMemeUserFavorite();
        Assert.assertNotNull("Data on demand for 'MemeUserFavorite' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MemeUserFavorite' failed to provide an identifier", id);
        obj = MemeUserFavorite.findMemeUserFavorite(id);
        Assert.assertNotNull("Find method for 'MemeUserFavorite' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'MemeUserFavorite' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllMemeUserFavorites() {
        Assert.assertNotNull("Data on demand for 'MemeUserFavorite' failed to initialize correctly", dod.getRandomMemeUserFavorite());
        long count = MemeUserFavorite.countMemeUserFavorites();
        Assert.assertTrue("Too expensive to perform a find all test for 'MemeUserFavorite', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<MemeUserFavorite> result = MemeUserFavorite.findAllMemeUserFavorites();
        Assert.assertNotNull("Find all method for 'MemeUserFavorite' illegally returned null", result);
        Assert.assertTrue("Find all method for 'MemeUserFavorite' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindMemeUserFavoriteEntries() {
        Assert.assertNotNull("Data on demand for 'MemeUserFavorite' failed to initialize correctly", dod.getRandomMemeUserFavorite());
        long count = MemeUserFavorite.countMemeUserFavorites();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<MemeUserFavorite> result = MemeUserFavorite.findMemeUserFavoriteEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'MemeUserFavorite' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'MemeUserFavorite' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        MemeUserFavorite obj = dod.getRandomMemeUserFavorite();
        Assert.assertNotNull("Data on demand for 'MemeUserFavorite' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MemeUserFavorite' failed to provide an identifier", id);
        obj = MemeUserFavorite.findMemeUserFavorite(id);
        Assert.assertNotNull("Find method for 'MemeUserFavorite' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyMemeUserFavorite(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'MemeUserFavorite' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        MemeUserFavorite obj = dod.getRandomMemeUserFavorite();
        Assert.assertNotNull("Data on demand for 'MemeUserFavorite' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MemeUserFavorite' failed to provide an identifier", id);
        obj = MemeUserFavorite.findMemeUserFavorite(id);
        boolean modified =  dod.modifyMemeUserFavorite(obj);
        Integer currentVersion = obj.getVersion();
        MemeUserFavorite merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'MemeUserFavorite' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'MemeUserFavorite' failed to initialize correctly", dod.getRandomMemeUserFavorite());
        MemeUserFavorite obj = dod.getNewTransientMemeUserFavorite(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'MemeUserFavorite' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'MemeUserFavorite' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'MemeUserFavorite' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        MemeUserFavorite obj = dod.getRandomMemeUserFavorite();
        Assert.assertNotNull("Data on demand for 'MemeUserFavorite' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MemeUserFavorite' failed to provide an identifier", id);
        obj = MemeUserFavorite.findMemeUserFavorite(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'MemeUserFavorite' with identifier '" + id + "'", MemeUserFavorite.findMemeUserFavorite(id));
    }
}
