package com.eastapps.meme_gen_server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eastapps.meme_gen_server.domain.Meme;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.meme_gen_server.domain.User;

/**
 * Sample controller for going to the home page with a message
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

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
	model.addAttribute("controllerMessage", "This is the message from the controller!");
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

		    final User eaUser = (User) eaResult;

		    sb.append(eaUser.getUsername());

		    isNotFirst = true;
		}
	    }

	    model.addAttribute("controllerMessage", sb.toString());
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

    @RequestMapping(value = "/meme_data/{id}/background", method = RequestMethod.GET)
    @ResponseBody
    public byte[] getMemeBackground(@PathVariable("id") final String backgroundId) throws IOException {
	final Session session = sessionFactory.openSession();

	try {
	    session.beginTransaction();
	    
	    final Query qry = session.createQuery("");
	    qry.setInteger(1, Integer.parseInt(backgroundId));
	    
	    final File img = new File("/home/dylan/workspace/meme_gen/meme_gen_server/docs/imgs/tmimitw.jpg");

	} catch (Exception e) {
	} finally {
	    if (session != null) {
		session.close();
	    }
	}
	return getBytesFromFile(null);
    }

    @RequestMapping(value = { "/meme_data/{id}/json" }, method = RequestMethod.GET)
    @ResponseBody
    public ShallowMeme getMeme(@RequestParam("id") final int memeId) {
	return new ShallowMeme(new Meme());
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
	InputStream is = null;
	byte[] bytes = new byte[0];

	try {
	    is = new FileInputStream(file);

	    // Get the size of the file
	    long length = file.length();

	    if (length > Integer.MAX_VALUE) {
		// File is too large
		throw new IOException("File is too large");
	    }

	    // Create the byte array to hold the data
	    bytes = new byte[(int) length];

	    // Read in the bytes
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
		offset += numRead;
	    }

	    // Ensure all the bytes have been read in
	    if (offset < bytes.length) {
		throw new IOException("Could not completely read file " + file.getName());
	    }

	} finally {
	    if (is != null) {
		// Close the input stream and return bytes
		is.close();
	    }
	}

	return bytes;
    }
}
