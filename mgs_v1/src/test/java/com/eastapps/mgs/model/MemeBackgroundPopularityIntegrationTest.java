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

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Transactional
@RooIntegrationTest(entity = MemeBackgroundPopularity.class)
public class MemeBackgroundPopularityIntegrationTest extends AbstractIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    MemeBackgroundPopularityDataOnDemand dod;

	@Test
    public void testCountMemeBackgroundPopularitys() {
        Assert.assertNotNull("Data on demand for 'MemeBackgroundPopularity' failed to initialize correctly", dod.getRandomMemeBackgroundPopularity());
        long count = MemeBackgroundPopularity.countMemeBackgroundPopularitys();
        Assert.assertTrue("Counter for 'MemeBackgroundPopularity' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindMemeBackgroundPopularity() {
        MemeBackgroundPopularity obj = dod.getRandomMemeBackgroundPopularity();
        Assert.assertNotNull("Data on demand for 'MemeBackgroundPopularity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MemeBackgroundPopularity' failed to provide an identifier", id);
        obj = MemeBackgroundPopularity.findMemeBackgroundPopularity(id);
        Assert.assertNotNull("Find method for 'MemeBackgroundPopularity' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'MemeBackgroundPopularity' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllMemeBackgroundPopularitys() {
        Assert.assertNotNull("Data on demand for 'MemeBackgroundPopularity' failed to initialize correctly", dod.getRandomMemeBackgroundPopularity());
        long count = MemeBackgroundPopularity.countMemeBackgroundPopularitys();
        Assert.assertTrue("Too expensive to perform a find all test for 'MemeBackgroundPopularity', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<MemeBackgroundPopularity> result = MemeBackgroundPopularity.findAllMemeBackgroundPopularitys();
        Assert.assertNotNull("Find all method for 'MemeBackgroundPopularity' illegally returned null", result);
        Assert.assertTrue("Find all method for 'MemeBackgroundPopularity' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindMemeBackgroundPopularityEntries() {
        Assert.assertNotNull("Data on demand for 'MemeBackgroundPopularity' failed to initialize correctly", dod.getRandomMemeBackgroundPopularity());
        long count = MemeBackgroundPopularity.countMemeBackgroundPopularitys();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<MemeBackgroundPopularity> result = MemeBackgroundPopularity.findMemeBackgroundPopularityEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'MemeBackgroundPopularity' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'MemeBackgroundPopularity' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        MemeBackgroundPopularity obj = dod.getRandomMemeBackgroundPopularity();
        Assert.assertNotNull("Data on demand for 'MemeBackgroundPopularity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MemeBackgroundPopularity' failed to provide an identifier", id);
        obj = MemeBackgroundPopularity.findMemeBackgroundPopularity(id);
        Assert.assertNotNull("Find method for 'MemeBackgroundPopularity' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyMemeBackgroundPopularity(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'MemeBackgroundPopularity' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        MemeBackgroundPopularity obj = dod.getRandomMemeBackgroundPopularity();
        Assert.assertNotNull("Data on demand for 'MemeBackgroundPopularity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MemeBackgroundPopularity' failed to provide an identifier", id);
        obj = MemeBackgroundPopularity.findMemeBackgroundPopularity(id);
        boolean modified =  dod.modifyMemeBackgroundPopularity(obj);
        Integer currentVersion = obj.getVersion();
        MemeBackgroundPopularity merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'MemeBackgroundPopularity' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'MemeBackgroundPopularity' failed to initialize correctly", dod.getRandomMemeBackgroundPopularity());
        MemeBackgroundPopularity obj = dod.getNewTransientMemeBackgroundPopularity(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'MemeBackgroundPopularity' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'MemeBackgroundPopularity' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'MemeBackgroundPopularity' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        MemeBackgroundPopularity obj = dod.getRandomMemeBackgroundPopularity();
        Assert.assertNotNull("Data on demand for 'MemeBackgroundPopularity' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MemeBackgroundPopularity' failed to provide an identifier", id);
        obj = MemeBackgroundPopularity.findMemeBackgroundPopularity(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'MemeBackgroundPopularity' with identifier '" + id + "'", MemeBackgroundPopularity.findMemeBackgroundPopularity(id));
    }
}
