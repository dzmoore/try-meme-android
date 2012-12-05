package com.eastapps.meme_gen_server.service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.eastapps.meme_gen_server.constants.Constants;
import com.eastapps.meme_gen_server.domain.LvMemeType;
import com.eastapps.meme_gen_server.domain.Meme;
import com.eastapps.meme_gen_server.domain.MemeBackground;
import com.eastapps.meme_gen_server.domain.MemeText;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.meme_gen_server.util.Util;

@Component
public class MemeService {
	private static final Logger logger = LoggerFactory.getLogger(MemeService.class);
	private static final int ADMIN_USER_ID = 1;

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private String memeImagesRootDir;
	
	
	public MemeService() {
		super();
	}
	
	
	public MemeService(final SessionFactory sessionFactory, final String memeImagesRootDir) {
		super();

		this.sessionFactory = sessionFactory;
		this.memeImagesRootDir = memeImagesRootDir;
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

}
