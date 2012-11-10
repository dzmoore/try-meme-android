package com.eastapps.meme_gen_server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.ejb.criteria.expression.ConcatExpression;
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

import com.eastapps.meme_gen_server.domain.LvMemeType;
import com.eastapps.meme_gen_server.domain.Meme;
import com.eastapps.meme_gen_server.domain.MemeBackground;
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
	private String memeImagesRootDir;

	@Autowired
	public HomeController(final SessionFactory sessionFactory, final String memeStringImagesRootDir) {
		this.sessionFactory = sessionFactory;
		this.memeImagesRootDir = memeStringImagesRootDir;
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

	@RequestMapping(value="/add_meme_admin", method = RequestMethod.POST)
	public String addMemeAdmin(
		@RequestParam(value = "backgroundFileName") final String backgroundFileName,
		@RequestParam(value = "memeTitle") final String memeTitle,
		@RequestParam(value = "sample1TopText") final String sample1TopText,
		@RequestParam(value = "sample1BottomText") final String sample1BottomText,
		@RequestParam(value = "sample2TopText") final String sample2TopText,
		@RequestParam(value = "sample2BottomText") final String sample2BottomText,
		@RequestParam(value = "sample3TopText") final String sample3TopText,
		@RequestParam(value = "sample3BottomText") final String sample3BottomText,
		Model model)
	{

		logger.debug(StringUtils.join(new Object[] {
			"backgroundFileName=[", backgroundFileName, "] ",
			"memeTitle=[", memeTitle, "] ",
			"sample1TopText=[", sample1TopText, "] ",
			"sample1BottomText=[", sample1BottomText, "] ",
			"sample2TopText=[", sample2TopText, "] ",
			"sample2BottomText=[", sample1BottomText, "] ",
			"sample3TopText=[", sample1TopText, "] ", "] ",
			"sample3BottomText=[", sample1BottomText, "] "
		}));
		
		final Session session = sessionFactory.openSession();
		
		try {
			final StringBuilder overallResult = new StringBuilder();
			
			session.beginTransaction();

			final MemeBackground bg = new MemeBackground();
			bg.setPath(backgroundFileName);
			bg.setActive(true);
			
			final Serializable bgIdResult = session.save(bg);
			
			logger.debug(StringUtils.join(new Object[] {
				"bg store result=[", bgIdResult, "]"
			}));
			
			if (bgIdResult instanceof Integer) {
				bg.setId((Integer)bgIdResult);
				
				overallResult.append("background_store_result=["+bg.toString()+"]  ");
			}

			
			commit(session);
			
			session.beginTransaction();
			
			final LvMemeType memeType = new LvMemeType();
			memeType.setActive(true);
			memeType.setDescr(memeTitle);
			
			final Serializable memeTypeResult = session.save(memeType);
			
			logger.debug(StringUtils.join(new Object[] {
				"meme type store result=[", memeTypeResult, "]"
			}));	
			
			if (memeTypeResult instanceof Integer) {
				memeType.setId((Integer)memeTypeResult);
				
				overallResult.append("memeType_store_result=[" + memeType.toString() + "]");
			}
			
			commit(session);
			
			
			model.addAttribute("controllerMessage", overallResult.toString());

		} finally {
			if (session != null) {
				session.close();
			}
		}

		return "home";
	}

	private void commit(final Session session) {
		if (session != null) {
			if (session.getTransaction() != null) {
				session.getTransaction().commit();
			}
		}
	}

	@RequestMapping(value = "/add_meme_admin", method = RequestMethod.GET)
	public String addMemeAdmin() {
		return "add_meme_admin";
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
			commit(session);

		} finally {
			if (session != null) {
				session.close();
			}
		}

		return "hibtest";
	}

	@RequestMapping(value = "/meme_data/{id}/background", method = RequestMethod.GET)
	@ResponseBody
	public byte[] getMemeBackground(@PathVariable("id") final int backgroundId) throws IOException {
		final Session session = sessionFactory.openSession();
	
		byte[] resultBytes = new byte[0];
		try {
			session.beginTransaction();

			final Query qry = session.createQuery("from MemeBackground bg where bg.id = :bgId");
			//	    qry.setInteger("bgId", Integer.parseInt(backgroundId));
			qry.setInteger("bgId", backgroundId);
			final List<?> result = qry.list();

			MemeBackground bg = new MemeBackground();
			if (result != null && result.size() > 0) {
				final Object obj = result.get(0);

				if (obj instanceof MemeBackground) {
					bg = (MemeBackground)obj;
				}
			}


			//	    final File img = new File("/home/dylan/workspace/meme_gen/meme_gen_server/docs/imgs/tmimitw.jpg");
			final String imgPath = StringUtils.join(new Object[]{memeImagesRootDir, File.separator, bg.getPath()});
			final File img = new File(imgPath);
			resultBytes = getBytesFromFile(img);

		} catch (Exception e) {
			logger.error("error occurred while attempting to find bg img", e);

		} finally {
			if (session != null) {
				session.close();
			}
		}

		return resultBytes;
	}

	@RequestMapping(value = { "/meme_data/{id}/json" }, method = RequestMethod.GET)
	@ResponseBody
	public ShallowMeme getMeme(@PathVariable("id") final int memeId) {
		final Session session = sessionFactory.openSession();

		ShallowMeme shallowMeme = new ShallowMeme(new Meme());
		try {
			session.beginTransaction();

			final Query qry = session.createQuery("from Meme m where m.id = :mId");
			qry.setInteger("mId", memeId);
			final List<?> result = qry.list();

			Meme meme = new Meme();
			if (result != null && result.size() > 0) {
				final Object obj = result.get(0);

				if (obj instanceof Meme) {
					meme = (Meme)obj;
				}
			}

			shallowMeme = new ShallowMeme(meme);

		} catch (Exception e) {
			logger.error("error occurred while attempting to meme", e);

		} finally {
			if (session != null) {
				session.close();
			}
		}

		return shallowMeme;
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
