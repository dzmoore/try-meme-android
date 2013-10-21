package com.eastapps.mgs.model;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.eastapps.mgs.util.TestUtils;

public class MemeUserIntegrationTest extends AbstractIntegrationTest {
	private static final Logger logger = Logger.getLogger(MemeIntegrationTest.class);

    @Test
    public void testMarkerMethod() {
    }
    
    @Test
    public void testCreateMemeUser() {
    	final MemeUser memeUser = new MemeUser();
    	memeUser.setActive(true);
    	memeUser.setUsername("test");
    	memeUser.setPassword("password");
    	memeUser.setSalt("salt");
    	
        final Long result = TestUtils.getJSONObject("/memeusers/create/json", memeUser, Long.class);
		
		TestCase.assertNotNull(result);
		TestCase.assertTrue(result > 0);
    }
}
