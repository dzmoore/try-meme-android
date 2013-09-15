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
@RooIntegrationTest(entity = LvPopularityType.class)
public class LvPopularityTypeIntegrationTest extends AbstractIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    LvPopularityTypeDataOnDemand dod;

	@Test
    public void testCountLvPopularityTypes() {
        Assert.assertNotNull("Data on demand for 'LvPopularityType' failed to initialize correctly", dod.getRandomLvPopularityType());
        long count = LvPopularityType.countLvPopularityTypes();
        Assert.assertTrue("Counter for 'LvPopularityType' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindLvPopularityType() {
        LvPopularityType obj = dod.getRandomLvPopularityType();
        Assert.assertNotNull("Data on demand for 'LvPopularityType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LvPopularityType' failed to provide an identifier", id);
        obj = LvPopularityType.findLvPopularityType(id);
        Assert.assertNotNull("Find method for 'LvPopularityType' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'LvPopularityType' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllLvPopularityTypes() {
        Assert.assertNotNull("Data on demand for 'LvPopularityType' failed to initialize correctly", dod.getRandomLvPopularityType());
        long count = LvPopularityType.countLvPopularityTypes();
        Assert.assertTrue("Too expensive to perform a find all test for 'LvPopularityType', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<LvPopularityType> result = LvPopularityType.findAllLvPopularityTypes();
        Assert.assertNotNull("Find all method for 'LvPopularityType' illegally returned null", result);
        Assert.assertTrue("Find all method for 'LvPopularityType' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindLvPopularityTypeEntries() {
        Assert.assertNotNull("Data on demand for 'LvPopularityType' failed to initialize correctly", dod.getRandomLvPopularityType());
        long count = LvPopularityType.countLvPopularityTypes();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<LvPopularityType> result = LvPopularityType.findLvPopularityTypeEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'LvPopularityType' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'LvPopularityType' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        LvPopularityType obj = dod.getRandomLvPopularityType();
        Assert.assertNotNull("Data on demand for 'LvPopularityType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LvPopularityType' failed to provide an identifier", id);
        obj = LvPopularityType.findLvPopularityType(id);
        Assert.assertNotNull("Find method for 'LvPopularityType' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyLvPopularityType(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'LvPopularityType' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        LvPopularityType obj = dod.getRandomLvPopularityType();
        Assert.assertNotNull("Data on demand for 'LvPopularityType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LvPopularityType' failed to provide an identifier", id);
        obj = LvPopularityType.findLvPopularityType(id);
        boolean modified =  dod.modifyLvPopularityType(obj);
        Integer currentVersion = obj.getVersion();
        LvPopularityType merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'LvPopularityType' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'LvPopularityType' failed to initialize correctly", dod.getRandomLvPopularityType());
        LvPopularityType obj = dod.getNewTransientLvPopularityType(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'LvPopularityType' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'LvPopularityType' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'LvPopularityType' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        LvPopularityType obj = dod.getRandomLvPopularityType();
        Assert.assertNotNull("Data on demand for 'LvPopularityType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LvPopularityType' failed to provide an identifier", id);
        obj = LvPopularityType.findLvPopularityType(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'LvPopularityType' with identifier '" + id + "'", LvPopularityType.findLvPopularityType(id));
    }
}
