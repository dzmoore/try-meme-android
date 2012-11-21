package com.eastapps.meme_gen_server.service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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
import com.eastapps.meme_gen_server.domain.SampleMeme;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.meme_gen_server.util.Util;

@Component
public class MemeService {
	private static final Logger logger = LoggerFactory.getLogger(MemeService.class);

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
				shallowMeme.getTopTextType(), 
				shallowMeme.getTopTextFontSize()
				);

			sesh.beginTransaction();
			topText.setId(Util.getId(sesh.save(topText)));
			Util.commit(sesh);
			
			// save bottom text
			final MemeText bottomText = new MemeText(
				meme,
				shallowMeme.getBottomText(),
				shallowMeme.getBottomTextType(),
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

			doAddMemeAdmin(sample1TopText, sample1BottomText, session, overallResult, bg, memeType);
			doAddMemeAdmin(sample2TopText, sample2BottomText, session, overallResult, bg, memeType);
			doAddMemeAdmin(sample3TopText, sample3BottomText, session, overallResult, bg, memeType);

			model.addAttribute("controllerMessage", overallResult.toString());

		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private void doAddMemeAdmin(
		final String sample1TopText, 
		final String sample1BottomText, 
		final Session session,
		final StringBuilder overallResult, 
		final MemeBackground bg, 
		final LvMemeType memeType) 
	{
		session.beginTransaction();
		final MemeText txtTop1 = new MemeText();
		txtTop1.setText(sample1TopText);
		txtTop1.setTextType(Constants.TOP);
		try {
			txtTop1.setId((Integer)session.save(txtTop1));
		} catch (Exception e) {
			logger.error("err", e);
		}

		Util.commit(session);

		session.beginTransaction();
		final MemeText txtBtm1 = new MemeText();
		txtBtm1.setText(sample1BottomText);
		txtBtm1.setTextType(Constants.BOTTOM);
		try {
			txtBtm1.setId((Integer)session.save(txtBtm1));
		} catch (Exception e) {
			logger.error("err", e);
		}
		Util.commit(session);	


		final Meme meme1 = new Meme();
		txtTop1.setMeme(meme1);
		txtBtm1.setMeme(meme1);

		meme1.setLvMemeType(memeType);
		meme1.setMemeBackground(bg);
		meme1.getMemeTexts().add(txtTop1);
		meme1.getMemeTexts().add(txtBtm1);

		session.beginTransaction();

		final Serializable meme1Result = session.save(meme1);

		if (meme1Result instanceof Integer) {
			meme1.setId((Integer)meme1Result);
			overallResult.append("meme1_store_result=[");
			overallResult.append(meme1.toString()).append("]");
		}

		Util.commit(session);

		final SampleMeme sample = new SampleMeme();
		sample.setMeme(meme1);

		session.beginTransaction();
		try {
			sample.setId((Integer)session.save(sample));
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
	
	public Meme getMeme(final int memeId) {
		final Session session = sessionFactory.openSession();

		Meme meme = new Meme();
		try {
			session.beginTransaction();

			final Query qry = session.createQuery("from Meme m where m.id = :mId");
			qry.setInteger("mId", memeId);
			final List<?> result = qry.list();

			if (result != null && result.size() > 0) {
				final Object obj = result.get(0);

				if (obj instanceof Meme) {
					meme = (Meme)obj;
				}
			}

		} catch (Exception e) {
			logger.error("error occurred while attempting to meme", e);

		} finally {
			if (session != null) {
				session.close();
			}
		}

		return meme;
	}

}
