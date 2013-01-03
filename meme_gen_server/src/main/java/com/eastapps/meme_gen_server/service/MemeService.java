package com.eastapps.meme_gen_server.service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import antlr.Utils;

import com.eastapps.meme_gen_server.constants.Constants;
import com.eastapps.meme_gen_server.domain.DeviceInfo;
import com.eastapps.meme_gen_server.domain.LvMemeType;
import com.eastapps.meme_gen_server.domain.Meme;
import com.eastapps.meme_gen_server.domain.MemeBackground;
import com.eastapps.meme_gen_server.domain.MemeText;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.meme_gen_server.domain.ShallowUser;
import com.eastapps.meme_gen_server.domain.User;
import com.eastapps.meme_gen_server.domain.UserFavMemeType;
import com.eastapps.meme_gen_server.util.Util;

@Component
public class MemeService {
	private static final Logger logger = LoggerFactory.getLogger(MemeService.class);
	private static final int ADMIN_USER_ID = 1;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private String memeImagesRootDir;
	
	@Autowired
	private String memeThumbImagesRootDir;
	
	@Autowired
	private Long installKeyTimeoutMs;


	public MemeService() {
		super();
		
	}


	public MemeService(
		final SessionFactory sessionFactory, 
		final String memeImagesRootDir, 
		final String memeThumbImagesRootDir,
		final Long installKeyTimeoutMs) 
	{
		super();

		this.sessionFactory = sessionFactory;
		this.memeImagesRootDir = memeImagesRootDir;
		this.memeThumbImagesRootDir = memeThumbImagesRootDir;
		this.installKeyTimeoutMs = installKeyTimeoutMs;
	}

	public int storeMeme(final ShallowMeme shallowMeme) {
		final Session sesh = sessionFactory.openSession();

		int result = Constants.INVALID;

		Meme meme = shallowMeme.toMeme();

		try {

			// save meme
			sesh.beginTransaction();

			result = Util.getId(sesh.save(meme));
			Util.commit(sesh);

			shallowMeme.setId(result);

			// save top text
			final MemeText topText = new MemeText(
				meme,
				shallowMeme.getTopText(), 
				Constants.TOP,
				shallowMeme.getTopTextFontSize()
				);

			sesh.beginTransaction();
			topText.setId(Util.getId(sesh.save(topText)));
			Util.commit(sesh);

			// save bottom text
			final MemeText bottomText = new MemeText(
				meme,
				shallowMeme.getBottomText(),
				Constants.BOTTOM,
				shallowMeme.getBottomTextFontSize()
				);

			sesh.beginTransaction();
			bottomText.setId(Util.getId(sesh.save(bottomText)));
			Util.commit(sesh); 



		} catch (Exception e) {
			try {
				sesh.getTransaction().rollback();
			} catch (Exception e2) { }

			logger.error("error occurred while attempting to store meme", e);

		} finally {
			try {
				sesh.close();
			} catch (Exception e) { }
		}

		return result;
	}
	
	public List<ShallowMemeType> getFavoriteMemeTypesForUser(final int userId) {
		final Session sesh = sessionFactory.openSession();
		
		List<ShallowMemeType> types = new ArrayList<ShallowMemeType>();
		try {
			sesh.beginTransaction();
			
			// get the user object
			final Query qry = sesh.createQuery("from User u where u.id = :id");
			qry.setInteger("id", userId);
			
			final List<?> results = qry.list();
			if (results != null && !results.isEmpty()) {
				if (results.get(0) instanceof User) {
					final User u = (User) results.get(0);
					
					// build the shallow 'fav types' list
					for (final UserFavMemeType eaFavType : u.getUserFavMemeTypes()) {
						final LvMemeType eaLvFavType = eaFavType.getLvMemeType();
						
						final ShallowMemeType eaShFavType = new ShallowMemeType();
						eaShFavType.setTypeId(eaLvFavType.getId());
						eaShFavType.setTypeDescr(eaLvFavType.getDescr());
						
						// set the background id (making the assumption
						// here that all memes of a certain type have the same background)
						final Iterator<Meme> iterator = eaLvFavType.getMemes().iterator();
						if (iterator.hasNext()) {
    						final Meme next = iterator.next();
    						if (next != null && next.getMemeBackground() != null) {
    							eaShFavType.setBackgroundId(next.getMemeBackground().getId());
    						}
						}
						
						types.add(eaShFavType);
					}
				}
			}
			
		} catch (Exception e) {
			logger.error("error occurred while attempting to find user favorites for user " + userId, e);
		}
		
		
		return types;
	}


	public void addMemeAdmin(		
		final String backgroundFileName,
		final String memeTitle,
		final String sample1TopText,
		final String sample1BottomText,
		final String sample2TopText,
		final String sample2BottomText,
		final String sample3TopText,
		final String sample3BottomText,
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


			Util.commit(session);

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

			Util.commit(session);

			doAddSampleMemeAdmin(sample1TopText, sample1BottomText, session, overallResult, bg, memeType);
			doAddSampleMemeAdmin(sample2TopText, sample2BottomText, session, overallResult, bg, memeType);
			doAddSampleMemeAdmin(sample3TopText, sample3BottomText, session, overallResult, bg, memeType);

			model.addAttribute("controllerMessage", overallResult.toString());

		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private void doAddSampleMemeAdmin(
		final String sample1TopText, 
		final String sample1BottomText, 
		final Session session,
		final StringBuilder overallResult, 
		final MemeBackground bg, 
		final LvMemeType memeType) 
	{

		final Meme meme = new Meme();

		meme.setLvMemeType(memeType);
		meme.setMemeBackground(bg);
		meme.getUser().setId(ADMIN_USER_ID);
		meme.setIsSampleMeme(true);

		session.beginTransaction();

		final Serializable meme1Result = session.save(meme);

		if (meme1Result instanceof Integer) {
			meme.setId((Integer)meme1Result);
			overallResult.append("meme1_store_result=[");
			overallResult.append(meme.toString()).append("]");
		}

		Util.commit(session);

		session.beginTransaction();
		final MemeText txtTop = new MemeText();
		txtTop.setText(sample1TopText);
		txtTop.setTextType(Constants.TOP);
		txtTop.setMeme(meme);
		try {
			txtTop.setId((Integer)session.save(txtTop));
		} catch (Exception e) {
			logger.error("err", e);
		}

		Util.commit(session);

		session.beginTransaction();
		final MemeText txtBtm = new MemeText();
		txtBtm.setText(sample1BottomText);
		txtBtm.setTextType(Constants.BOTTOM);
		txtBtm.setMeme(meme);
		try {
			txtBtm.setId((Integer)session.save(txtBtm));
		} catch (Exception e) {
			logger.error("err", e);
		}
		Util.commit(session);	

	}

	public byte[] getMemeBackground(final int memeId) throws IOException {
		final Session session = sessionFactory.openSession();

		byte[] resultBytes = new byte[0];
		try {
			session.beginTransaction();

			final Query qry = session.createQuery("from Meme where id = :id");
			//	    qry.setInteger("bgId", Integer.parseInt(backgroundId));
			qry.setInteger("id", memeId);
			final List<?> results = qry.list();

			MemeBackground bg = new MemeBackground();
			if (results != null && results.size() > 0) {
				final Object obj = results.get(0);

				if (obj instanceof Meme) {
					bg = ((Meme)obj).getMemeBackground();

					logger.debug(StringUtils.join(new Object[]{"bg=[", bg, "]"}));
				}
			}


			//	    final File img = new File("/home/dylan/workspace/meme_gen/meme_gen_server/docs/imgs/tmimitw.jpg");
			final String imgPath = StringUtils.join(new Object[]{memeImagesRootDir, File.separator, bg.getPath()});

			logger.debug(StringUtils.join(new Object[] {"imgPath=[", imgPath, "]"}));

			final File img = new File(imgPath);
			resultBytes = Util.getBytesFromFile(img);

		} catch (Exception e) {
			logger.error("error occurred while attempting to find bg img", e);

		} finally {
			if (session != null) {
				session.close();
			}
		}

		return resultBytes;
	}

	public ShallowMeme getShallowMeme(final int memeId) {
		final Session session = sessionFactory.openSession();

		ShallowMeme meme = new ShallowMeme();
		try {
			meme = new ShallowMeme(doGetMeme(memeId, session));

		} catch (Exception e) {
			logger.error("err", e);
			session.clear();

		} finally {
			try {
				session.close();
			} catch (Exception e) { /* */ }
		}

		return meme;
	}

	public Meme getMeme(final int memeId) {
		final Session session = sessionFactory.openSession();

		Meme meme = new Meme();
		try {
			session.beginTransaction();

			meme = doGetMeme(memeId, session);

		} catch (Exception e) {
			logger.error("error occurred while attempting to meme", e);

		} finally {
			if (session != null) {
				session.close();
			}
		}

		return meme;
	}


	private Meme doGetMeme(final int memeId, final Session session) {
		final Query qry = session.createQuery("from Meme m where m.id = :mId");
		qry.setInteger("mId", memeId);
		final List<?> result = qry.list();

		Meme meme = new Meme(); 
		try {
			if (result != null && result.size() > 0) {
				final Object obj = result.get(0);

				if (obj instanceof Meme) {
					meme = (Meme)obj;
				}
			}
		} catch (Exception e) {
			logger.error("err", e);
		}

		return meme;
	}

	public List<ShallowMeme> getSampleMemes(final int memeTypeId) {
		final Session sesh = sessionFactory.openSession();

		List<ShallowMeme> samples = new ArrayList<ShallowMeme>(0);
		try {
			sesh.beginTransaction();
			samples = doGetSampleMemes(memeTypeId, sesh);

		} catch (Exception e) {
			logger.error("error occurred", e);

		} finally {
			try {
				sesh.close();
			} catch (Exception e) {}
		}

		return samples;
	}

	private List<ShallowMeme> doGetSampleMemes(final int memeTypeId, final Session session) {
		List<ShallowMeme> samples = new ArrayList<ShallowMeme>();

		final Query qry = session.createQuery("from Meme m where m.lvMemeType.id = :id and m.isSampleMeme = true");
		qry.setInteger("id", memeTypeId);

		try {
			final List<?> memes = qry.list();

			boolean typeMatches = false;
			for (final Object ea : memes) {
				if (typeMatches || ea instanceof Meme) {
					samples.add(new ShallowMeme((Meme)ea));

					typeMatches = true;
				}
			}

		} catch (Exception e) {
			logger.error("error occurred while getting sample memes", e);
		}


		return samples;
	}

	private static boolean memeTypeListContainsMemeType(final List<ShallowMemeType> typeList, LvMemeType lmt) {
		boolean contains = false;

		for (final ShallowMemeType eaType : typeList) {
			if (eaType.getTypeId() == lmt.getId()) {
				contains = true;
				break;
			}
		}

		return contains;
	}

	private static boolean doesNotContain(final List<ShallowMemeType> typeList, LvMemeType lmt) {
		return !memeTypeListContainsMemeType(typeList, lmt);
	}

	public List<ShallowMemeType> getAllMemeTypes() {
		final List<ShallowMemeType> types = new ArrayList<ShallowMemeType>();

		final Session sesh = sessionFactory.openSession();
		final Query qry = sesh.createQuery("from Meme m where m.isSampleMeme = true and m.lvMemeType.active = true");

		try {
			final List<?> results = qry.list();

			boolean typeMatch = false;
			for (final Object ea : results) {
				if (typeMatch || ea instanceof Meme) {
					final LvMemeType eaLvType = ((Meme)ea).getLvMemeType();

					if (doesNotContain(types, eaLvType)) {
						types.add(new ShallowMemeType(eaLvType));
					}

					typeMatch = true;
				}
			}

		} catch (Exception e) {
			logger.error("error occurred while attempting to get all meme types", e);

		} finally {
			try {
				sesh.close();

			} catch (Exception e) { }
		}

		return types;
	}

	public byte[] getThumbForType(final int typeId) {

		final Session session = sessionFactory.openSession();

		byte[] resultBytes = new byte[0];
		try {
			session.beginTransaction();

			final Query qry = session.createQuery(
				StringUtils.join(new Object[] {
    				"from Meme m ",
    				"where ",
    				"	m.isSampleMeme 			= true and ",
    				"	m.lvMemeType.active 	= true and ",
    				"	m.lvMemeType.id 		= :typeId"
				}
			));
			
			qry.setInteger("typeId", typeId);
			
			final List<?> results = qry.list();
			
			MemeBackground bg = null;
			if (results != null && results.size() > 0) {
				final Object obj = results.get(0);

				if (obj instanceof Meme) {
					bg = ((Meme)obj).getMemeBackground();
					
					if (logger.isDebugEnabled()) {
    					logger.debug(StringUtils.join(new Object[]{"bg=[", bg, "]"}));
					}
				}
			}

			if (bg != null) {
    			final String imgPath = StringUtils.join(new Object[]{memeThumbImagesRootDir, File.separator, bg.getPath()});

    			if (logger.isDebugEnabled()) {
        			logger.debug(StringUtils.join(new Object[] {"imgPath=[", imgPath, "]"}));
    			}

    			final File img = new File(imgPath);
    			resultBytes = Util.getBytesFromFile(img);
			}

		} catch (Exception e) {
			logger.error("error occurred while attempting to find bg img", e);

		} finally {
			if (session != null) {
				session.close();
			}
		}

		return resultBytes;
	}

	
	public ShallowUser getUser(final int userId) {
		ShallowUser user = new ShallowUser();
		
		final Session sesh = sessionFactory.openSession();
		try {
			sesh.beginTransaction();
			final Query qry = sesh.createQuery("from User u where u.id = :id");
			qry.setInteger("id", userId);
			
			final List<?> results = qry.list();
			if (results != null) {
				final Iterator<?> iterator = results.iterator();
				if (iterator.hasNext()) {
					final User resultUser = (User) iterator.next();
					
					user.setId(resultUser.getId());
					user.setUsername(resultUser.getUsername());
				}
			}
			
			
		} catch (final Exception e) {
			logger.error("error occurred while attempting to find user for id " + userId, e);
		}
		
		return user;
	}
	
	@Deprecated // probably..
	private ShallowUser getUserForDeviceId(final String deviceId) {
		ShallowUser user = new ShallowUser();
		
		final Session sesh = sessionFactory.openSession();
		try {
			
			
		} catch (final Exception e) {
			logger.error("error occurred while attempting to find user for device id " + deviceId, e);
		}
		
		return user;
	}


	public String getNewInstallKey() { 
		String key = UUID.randomUUID().toString();
		
		final DeviceInfo di = new DeviceInfo();
		
		boolean uniqueKeyNotFound = true;
		
		Session sesh = null;
		try {
    		while (uniqueKeyNotFound) {
        		di.setUniqueId(key);
        		di.setUser(null);
        		
        		sesh = sessionFactory.openSession();
        		sesh.beginTransaction();
        		
        		final Serializable result = sesh.save(di);
        		Util.commit(sesh);
        		
    			Util.close(sesh);
    			
    			sesh = sessionFactory.openSession();
    			sesh.beginTransaction();
    			
        		final Query qry = sesh.createQuery("from DeviceInfo d where d.uniqueId = :installKey");
        		qry.setString("installKey", key);
        	
        		final List<?> results = qry.list();
        		uniqueKeyNotFound = results == null || results.size() != 1;
        		
        		Util.close(sesh);
    		}
    		
		} catch (Exception e) {
    		logger.error("err", e);
    		
    	} finally {
    		Util.close(sesh);
		}
		
	    return key;
	}


	public int storeNewUser(ShallowUser user) {
		int userId = Constants.INVALID;
		
		if (isInstallKeyValid(user)) {
    		Session sesh = sessionFactory.openSession();
    		sesh.beginTransaction();
    		
    		try {
    			final User u = new User();
    			u.setActive(true);
    			u.setUsername(user.getUsername());
    			
    			userId = Util.getId(sesh.save(u));
    			Util.commit(sesh);
    			Util.close(sesh);
    			
    			if (userId > 0) {
    				u.setId(userId);
    				
    				sesh = sessionFactory.openSession();
    				sesh.beginTransaction();
    				
        			final DeviceInfo devInfo = new DeviceInfo();
        			devInfo.setUniqueId(user.getInstallKey());
        			devInfo.setUser(u);
        			
        			final int devId = Util.getId(sesh.save(devInfo));
        			if (devId <= 0) {
        				logger.warn("dev info didnt store", new Exception("stack trace only"));
        			}
    			}
    			
    		} catch (Exception e) {
    			logger.error("err", e);
    			
    		} finally {
    			Util.close(sesh);
    		}
    		
		} else {
			logger.info(StringUtils.join(new Object[] {
				"install key invalid; installKey=", user.getInstallKey(),
				"; user=", user.toString(), ";"
			}));
		}
		
		
		return userId;
	}
	
	private boolean isInstallKeyValid(final ShallowUser u) {
		boolean isValid = false;
		
		if (u != null && StringUtils.isNotBlank(u.getInstallKey())) {
    		final Session sesh = sessionFactory.openSession();
    		try {
        		final Query qry = sesh.createQuery("from DeviceInfo d where d.uniqueId = :installKey");
        		qry.setString("installKey", u.getInstallKey());
        		
        		final List<?> results = qry.list();
        		if (results != null && results.size() == 1) {
        			if (results.get(0) instanceof DeviceInfo) {
        				final DeviceInfo devInfo = (DeviceInfo)results.get(0);
        				
        				isValid = System.currentTimeMillis() - devInfo.getLastMod().getTime() <= installKeyTimeoutMs;
        			}
        		}
        		
    		} catch (Exception e) {
    			logger.error("err", e);
    			
    		} finally {
    			Util.close(sesh);
    		}
		}
		
		return isValid;
	}
}






























