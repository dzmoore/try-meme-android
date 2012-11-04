package com.eastapps.meme_gen_server;

import java.io.File;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

public class HomeControllerTest {
    private SessionFactory sessionFactory;
    private String imgsRoot;
    
    @Ignore
    @Test
    public void testController() {
	HomeController controller = new HomeController(sessionFactory, imgsRoot);
	Model model = new ExtendedModelMap();
	Assert.assertEquals("home", controller.home(model));

	Object message = model.asMap().get("controllerMessage");
	Assert.assertEquals("This is the message from the controller!", message);

    }

    @Ignore
    @Test
    public void testHib() {
	final Session sesh = sessionFactory.openSession();
	sesh.beginTransaction();

	final List<?> users = sesh.createQuery("from User").list();
	TestCase.assertTrue(users.size() >= 1);
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
}
