package com.eastapps.mgs.model;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.naming.java.javaURLContextFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class AbstractIntegrationTest {
	@BeforeClass
	public static void setUpClass() throws NamingException {
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, javaURLContextFactory.class.getName());
		System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
		InitialContext ic = new InitialContext();
		
		ic.createSubcontext("java:");
		ic.createSubcontext("java:comp");
		ic.createSubcontext("java:comp/env");
		ic.createSubcontext("java:comp/env/jdbc");	
		
		final BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:mysql://localhost:3306/mgsdbv1");
		dataSource.setUsername("root");
		dataSource.setPassword("password");
		
		ic.bind("java:comp/env/jdbc/mgsdb", dataSource);
	}
	
	@AfterClass
	public static void tearDownClass() throws NamingException {
		final InitialContext initialContext = new InitialContext();
		
		initialContext.destroySubcontext("java:comp/env/jdbc");
		initialContext.destroySubcontext("java:comp/env");
		initialContext.destroySubcontext("java:comp");
		initialContext.destroySubcontext("java:");
		
//		initialContext.unbind("java:comp/env/jdbc/mgsdb");
	}
}
