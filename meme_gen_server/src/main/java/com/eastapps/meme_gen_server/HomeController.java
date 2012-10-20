package com.eastapps.meme_gen_server;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.eastapps.meme_gen_server.domain.User;

/**
 * Sample controller for going to the home page with a message
 */
@Controller
public class HomeController {
	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	public HomeController(final SessionFactory sessionFactory) {
	    this.sessionFactory = sessionFactory;
    }

	/**
	 * Selects the home page and populates the model with a message
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		logger.info("Welcome home!");
		model.addAttribute("controllerMessage",
				"This is the message from the controller!");
		return "home";
	}
	
	@RequestMapping(value = "/hibtest", method = RequestMethod.GET)
	public String doHibTest(Model model) {
	    final Session session = sessionFactory.openSession();
	   
	    try {
    	    session.beginTransaction();
    	    
    	    final StringBuilder sb = new StringBuilder();
    	    final List<?> results = session.createQuery("from User").list();
    	    boolean isNotFirst = false;
    	    for (final Object eaResult : results) {
    	        if (eaResult instanceof User) {
    	            if (isNotFirst) {
    	                sb.append("<br/>");
    	            }
    	            
    	            final User eaUser = (User)eaResult;
    	            
    	            sb.append(eaUser.getUsername());
    	            
    	            isNotFirst = true;
    	        }
    	    }
    	    
    	    
    	    model.addAttribute(
    	        "controllerMessage",
    	        sb.toString()
            );
    	    if (session != null) {
	            if (session.getTransaction() != null) {
        	        session.getTransaction().commit();
	            }
    	    }
    	    
	    } finally {
	        if (session != null) {
        	    session.close();
	        }
	    }
	    
	    return "hibtest";
	}

}
