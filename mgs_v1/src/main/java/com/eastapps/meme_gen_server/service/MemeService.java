package com.eastapps.meme_gen_server.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eastapps.mgs.model.MemeBackground;

@Component
public class MemeService {
//	private static final Logger logger = LoggerFactory.getLogger(MemeService.class);
//	private static final int ADMIN_USER_ID = 1;
//
//	@Autowired
//	private SessionFactory sessionFactory;
//
//	@Autowired
//	private Long installKeyTimeoutMs;
//	
//	@Autowired
//	private String 	memeThumbDirName;
//	
	@Autowired
	private IMemeFileReader imageFileReader;
//
//
//	public MemeService() {
//		super();
//		
//	}
//
//	public int storeMeme(final ShallowMeme shallowMeme) {
//		final Session sesh = getSession();
//
//		int result = Constants.INVALID;
//
//		Meme meme = shallowMeme.toMeme();
//
//		try {
//			// save meme
//			sesh.beginTransaction();
//
//			result = Util.getId(sesh.save(meme));
//
//			shallowMeme.setId(result);
//
//			// save top text
//			final MemeText topText = new MemeText(
//				meme,
//				shallowMeme.getTopText(), 
//				Constants.TOP,
//				shallowMeme.getTopTextFontSize()
//			);
//
//			topText.setId(Util.getId(sesh.save(topText)));
//
//			// save bottom text
//			final MemeText bottomText = new MemeText(
//				meme,
//				shallowMeme.getBottomText(),
//				Constants.BOTTOM,
//				shallowMeme.getBottomTextFontSize()
//			);
//
//			bottomText.setId(Util.getId(sesh.save(bottomText)));
//			
//			Util.commit(sesh); 
//
//		} catch (Exception e) {
//			Util.rollback(sesh);
//			
//			logger.error("error occurred while attempting to store meme", e);
//
//		} finally {
//			Util.close(sesh);
//		}
//
//		return result;
//	}
//	
//	public List<ShallowMemeType> getFavoriteMemeTypesForUser(final int userId) {
//		final Session sesh = getSession();
//		
//		List<ShallowMemeType> types = new ArrayList<ShallowMemeType>();
//		try {
//			sesh.beginTransaction();
//			
//			// get the user object
//			final Query qry = sesh.createQuery("from User u where u.id = :id");
//			qry.setInteger("id", userId);
//			
//			final List<?> results = qry.list();
//			if (results != null && !results.isEmpty()) {
//				if (results.get(0) instanceof User) {
//					final User u = (User) results.get(0);
//					
//					// build the shallow 'fav types' list
//					for (final UserFavMemeType eaFavType : u.getUserFavMemeTypes()) {
//						final LvMemeType eaLvFavType = eaFavType.getLvMemeType();
//						
//						final ShallowMemeType eaShFavType = new ShallowMemeType();
//						eaShFavType.setTypeId(eaLvFavType.getId());
//						eaShFavType.setTypeDescr(eaLvFavType.getDescr());
//						
//						// set the background id (making the assumption
//						// here that all memes of a certain type have the same background)
//						final Iterator<Meme> iterator = eaLvFavType.getMemes().iterator();
//						if (iterator.hasNext()) {
//    						final Meme next = iterator.next();
//    						if (next != null && next.getMemeBackground() != null) {
//    							eaShFavType.setBackgroundId(next.getMemeBackground().getId());
//    						}
//						}
//						
//						types.add(eaShFavType);
//					}
//				}
//			}
//			
//		} catch (Exception e) {
//			Util.rollback(sesh);
//			
//			logger.error("error occurred while attempting to find user favorites for user " + userId, e);
//			
//		} finally {
//			Util.close(sesh);
//		}
//		
//		
//		return types;
//	}
//
//
//	private org.hibernate.classic.Session getSession() {
//		return sessionFactory.getCurrentSession();
//	}
//
//
//	public void addMemeAdmin(		
//		final String backgroundFileName,
//		final String memeTitle,
//		final String sample1TopText,
//		final String sample1BottomText,
//		final String sample2TopText,
//		final String sample2BottomText,
//		final String sample3TopText,
//		final String sample3BottomText,
//		Model model) 
//	{
//		logger.debug(StringUtils.join(new Object[] {
//			"backgroundFileName=[", backgroundFileName, "] ",
//			"memeTitle=[", memeTitle, "] ",
//			"sample1TopText=[", sample1TopText, "] ",
//			"sample1BottomText=[", sample1BottomText, "] ",
//			"sample2TopText=[", sample2TopText, "] ",
//			"sample2BottomText=[", sample1BottomText, "] ",
//			"sample3TopText=[", sample1TopText, "] ", "] ",
//			"sample3BottomText=[", sample1BottomText, "] "
//		}));
//
//		final Session sesh = getSession();
//
//		try {
//			final StringBuilder overallResult = new StringBuilder();
//
//
//			final MemeBackground bg = new MemeBackground();
//			bg.setPath(backgroundFileName);
//			bg.setActive(true);
//
//			sesh.beginTransaction();
//			final Serializable bgIdResult = sesh.save(bg);
//
//			logger.debug(StringUtils.join(new Object[] {
//				"bg store result=[", bgIdResult, "]"
//			}));
//
//			if (bgIdResult instanceof Integer) {
//				bg.setId((Integer)bgIdResult);
//
//				overallResult.append("background_store_result=["+bg.toString()+"]  ");
//			}
//
//			final LvMemeType memeType = new LvMemeType();
//			memeType.setActive(true);
//			memeType.setDescr(memeTitle);
//
//			final Serializable memeTypeResult = sesh.save(memeType);
//
//			logger.debug(StringUtils.join(new Object[] {
//				"meme type store result=[", memeTypeResult, "]"
//			}));	
//
//			if (memeTypeResult instanceof Integer) {
//				memeType.setId((Integer)memeTypeResult);
//
//				overallResult.append("memeType_store_result=[" + memeType.toString() + "]");
//			}
//
//			doAddSampleMemeAdmin(sample1TopText, sample1BottomText, sesh, overallResult, bg, memeType);
//			doAddSampleMemeAdmin(sample2TopText, sample2BottomText, sesh, overallResult, bg, memeType);
//			doAddSampleMemeAdmin(sample3TopText, sample3BottomText, sesh, overallResult, bg, memeType);
//			
//			Util.commit(sesh);
//
//			model.addAttribute("controllerMessage", overallResult.toString());
//
//		} catch (Exception e) {
//			Util.rollback(sesh);
//			
//			logger.error("err", e);
//			
//		} finally {
//			Util.close(sesh);
//		}
//	}
//
//	private void doAddSampleMemeAdmin(
//		final String sample1TopText, 
//		final String sample1BottomText, 
//		final Session sesh,
//		final StringBuilder overallResult, 
//		final MemeBackground bg, 
//		final LvMemeType memeType) 
//	{
//		final Meme meme = new Meme();
//
//		meme.setLvMemeType(memeType);
//		meme.setMemeBackground(bg);
//		meme.getUser().setId(ADMIN_USER_ID);
//		meme.setIsSampleMeme(true);
//
//		final Serializable meme1Result = sesh.save(meme);
//
//		if (meme1Result instanceof Integer) {
//			meme.setId((Integer)meme1Result);
//			overallResult.append("meme1_store_result=[");
//			overallResult.append(meme.toString()).append("]");
//		}
//
//		final MemeText txtTop = new MemeText();
//		txtTop.setText(sample1TopText);
//		txtTop.setTextType(Constants.TOP);
//		txtTop.setMeme(meme);
//		txtTop.setId((Integer)sesh.save(txtTop));
//
//		final MemeText txtBtm = new MemeText();
//		txtBtm.setText(sample1BottomText);
//		txtBtm.setTextType(Constants.BOTTOM);
//		txtBtm.setMeme(meme);
//		txtBtm.setId((Integer)sesh.save(txtBtm));
//	}
//
	public byte[] getMemeBackgroundBytes(final long id) throws IOException {
		byte[] resultBytes = new byte[0];
		
		final MemeBackground bg = MemeBackground.findMemeBackground(id);
		
		if (bg != null) {
			resultBytes = imageFileReader.getBytes(bg.getFilePath());
		}

		return resultBytes;
	}
//
//	public ShallowMeme getShallowMeme(final int memeId) {
//		final Session sesh = getSession();
//
//		ShallowMeme meme = new ShallowMeme();
//		try {
//			sesh.beginTransaction();
//			
//			meme = new ShallowMeme(doGetMeme(memeId, sesh));
//
//		} catch (Exception e) {
//			Util.rollback(sesh);
//			
//			logger.error("err", e);
//
//		} finally {
//			Util.close(sesh);
//		}
//
//		return meme;
//	}
//
//	public Meme getMeme(final int memeId) {
//		final Session sesh = getSession();
//
//		Meme meme = new Meme();
//		try {
//			sesh.beginTransaction();
//
//			meme = doGetMeme(memeId, sesh);
//
//		} catch (Exception e) {
//			Util.rollback(sesh);
//			
//			logger.error("error occurred while attempting to meme", e);
//
//		} finally {
//			Util.close(sesh);
//		}
//
//		return meme;
//	}
//
//
//	private Meme doGetMeme(final int memeId, final Session session) {
//		final Query qry = session.createQuery("from Meme m where m.id = :mId");
//		qry.setInteger("mId", memeId);
//		final List<?> result = qry.list();
//
//		Meme meme = new Meme(); 
//		if (result != null && result.size() > 0) {
//			final Object obj = result.get(0);
//
//			if (obj instanceof Meme) {
//				meme = (Meme)obj;
//			}
//		}
//
//		return meme;
//	}
//
//	public List<ShallowMeme> getSampleMemes(final int memeTypeId) {
//		final Session sesh = getSession();
//
//		List<ShallowMeme> samples = new ArrayList<ShallowMeme>(0);
//		try {
//			sesh.beginTransaction();
//			samples = doGetSampleMemes(memeTypeId, sesh);
//
//		} catch (Exception e) {
//			Util.rollback(sesh);
//			
//			logger.error("error occurred", e);
//
//		} finally {
//			Util.close(sesh);
//		}
//
//		return samples;
//	}
//
//	private List<ShallowMeme> doGetSampleMemes(final int memeTypeId, final Session session) {
//		List<ShallowMeme> samples = new ArrayList<ShallowMeme>();
//
//		final Query qry = session.createQuery("from Meme m where m.lvMemeType.id = :id and m.isSampleMeme = true");
//		qry.setInteger("id", memeTypeId);
//
//		final List<?> memes = qry.list();
//
//		boolean typeMatches = false;
//		for (final Object ea : memes) {
//			if (typeMatches || ea instanceof Meme) {
//				samples.add(new ShallowMeme((Meme)ea));
//
//				typeMatches = true;
//			}
//		}
//
//		return samples;
//	}
//
//	private static boolean memeTypeListContainsMemeType(final List<ShallowMemeType> typeList, LvMemeType lmt) {
//		boolean contains = false;
//
//		for (final ShallowMemeType eaType : typeList) {
//			if (eaType.getTypeId() == lmt.getId()) {
//				contains = true;
//				break;
//			}
//		}
//
//		return contains;
//	}
//
//	private static boolean doesNotContain(final List<ShallowMemeType> typeList, LvMemeType lmt) {
//		return !memeTypeListContainsMemeType(typeList, lmt);
//	}
//
//	public List<ShallowMemeType> getAllMemeTypes() {
//		final List<ShallowMemeType> types = new ArrayList<ShallowMemeType>();
//
//		final Session sesh = getSession();
//		
//		try {
//			sesh.beginTransaction();
//			
//    		final Query qry = sesh.createQuery("from Meme m where m.isSampleMeme = true and m.lvMemeType.active = true order by m.lvMemeType.descr");
//    		
//			final List<?> results = qry.list();
//
//			boolean typeMatch = false;
//			for (final Object ea : results) {
//				if (typeMatch || ea instanceof Meme) {
//					final LvMemeType eaLvType = ((Meme)ea).getLvMemeType();
//
//					if (doesNotContain(types, eaLvType)) {
//						types.add(new ShallowMemeType(eaLvType));
//					}
//
//					typeMatch = true;
//				}
//			}
//
//		} catch (Exception e) {
//			Util.rollback(sesh);
//			
//			logger.error("error occurred while attempting to get all meme types", e);
//
//		} finally {
//			Util.close(sesh);
//		}
//
//		return types;
//	}
//
//	public byte[] getThumbForType(final int typeId) {
//		final Session session = getSession();
//
//		byte[] resultBytes = new byte[0];
//		try {
//			session.beginTransaction();
//
//			final Query qry = session.createQuery(
//				StringUtils.join(new Object[] {
//    				"from Meme m ",
//    				"where ",
//    				"	m.isSampleMeme 			= true and ",
//    				"	m.lvMemeType.active 	= true and ",
//    				"	m.lvMemeType.id 		= :typeId"
//				}
//			));
//			
//			qry.setInteger("typeId", typeId);
//			
//			final List<?> results = qry.list();
//			
//			MemeBackground bg = null;
//			if (results != null && results.size() > 0) {
//				final Object obj = results.get(0);
//
//				if (obj instanceof Meme) {
//					bg = ((Meme)obj).getMemeBackground();
//					
//					if (logger.isDebugEnabled()) {
//    					logger.debug(StringUtils.join(new Object[]{"bg=[", bg, "]"}));
//					}
//				}
//			}
//
//			if (bg != null) {
//    			resultBytes = imageFileReader.getBytes(StringUtils.join(new Object[] { 
//    				memeThumbDirName, "/", bg.getPath()
//    			}));
//			}
//
//		} catch (Exception e) {
//			Util.rollback(session);
//			
//			logger.error("error occurred while attempting to find bg img", e);
//
//		} finally {
//			Util.close(session);
//		}
//
//		return resultBytes;
//	}
//
//	
//	public ShallowUser getUser(final int userId) {
//		ShallowUser user = new ShallowUser();
//		
//		final Session sesh = getSession();
//		try {
//			sesh.beginTransaction();
//			final Query qry = sesh.createQuery("from User u where u.id = :id");
//			qry.setInteger("id", userId);
//			
//			final List<?> results = qry.list();
//			if (results != null) {
//				final Iterator<?> iterator = results.iterator();
//				if (iterator.hasNext()) {
//					final User resultUser = (User) iterator.next();
//					
//					user.setId(resultUser.getId());
//					user.setUsername(resultUser.getUsername());
//				}
//			}
//			
//			
//		} catch (final Exception e) {
//			Util.rollback(sesh);
//			
//			logger.error("error occurred while attempting to find user for id " + userId, e);
//			
//		} finally {
//			Util.close(sesh);
//		}
//		
//		return user;
//	}
//	
//	public String getNewInstallKey() { 
//		String key = UUID.randomUUID().toString();
//		
//		final DeviceInfo di = new DeviceInfo();
//		
//		boolean uniqueKeyNotFound = true;
//		
//		Session sesh = getSession();
//		try {
//			sesh.beginTransaction();
//			
//    		while (uniqueKeyNotFound) {
//        		di.setUniqueId(key);
//        		di.setUser(null);
//        		
//        		sesh = getSession();
//        		sesh.beginTransaction();
//        		
//        		final Serializable result = sesh.save(di);
//    			
//        		final Query qry = sesh.createQuery("from DeviceInfo d where d.uniqueId = :installKey");
//        		qry.setString("installKey", key);
//        	
//        		final List<?> results = qry.list();
//        		uniqueKeyNotFound = results == null || results.size() != 1;
//    		}
//    		
//    		Util.commit(sesh);
//    		
//		} catch (Exception e) {
//			Util.rollback(sesh);
//			
//    		logger.error("err", e);
//    		
//    	} finally {
//    		Util.close(sesh);
//		}
//		
//	    return key;
//	}
//
//
//	public int storeNewUser(ShallowUser user) {
//		int userId = Constants.INVALID;
//		
//		if (isInstallKeyValid(user)) {
//			
//    		Session sesh = getSession();
//    		try {
//        		sesh.beginTransaction();
//        		
//    			final User u = new User();
//    			u.setActive(true);
//    			u.setUsername(user.getUsername());
//    			
//    			userId = Util.getId(sesh.save(u));
//    			
//    			if (userId > 0) {
//    				u.setId(userId);
//    				
//    				sesh = getSession();
//    				sesh.beginTransaction();
//    				
//        			final DeviceInfo devInfo = new DeviceInfo();
//        			devInfo.setUniqueId(user.getInstallKey());
//        			devInfo.setUser(u);
//        			
//        			final int devId = Util.getId(sesh.save(devInfo));
//        			
//        			if (devId <= 0) {
//        				logger.warn("dev info didnt store", new Exception("stack trace only"));
//        				
//        			} else {
//        				Util.commit(sesh);
//        			}
//        			
//    			} else {
//    				logger.warn("user not stored");
//    			}
//    			
//    		} catch (Exception e) {
//    			Util.rollback(sesh);
//    			
//    			logger.error("err", e);
//    			
//    		} finally {
//    			Util.close(sesh);
//    		}
//    		
//		} else {
//			logger.info(StringUtils.join(new Object[] {
//				"install key invalid; installKey=", user.getInstallKey(),
//				"; user=", user.toString(), ";"
//			}));
//		}
//		
//		
//		return userId;
//	}
//	
//	private boolean isInstallKeyValid(final ShallowUser u) {
//		boolean isValid = false;
//		
//		if (u != null && StringUtils.isNotBlank(u.getInstallKey())) {
//    		final Session sesh = getSession();
//    		
//    		try {
//        		sesh.beginTransaction();
//        		
//        		final Query qry = sesh.createQuery("from DeviceInfo d where d.uniqueId = :installKey");
//        		qry.setString("installKey", u.getInstallKey());
//        		
//        		final List<?> results = qry.list();
//        		if (results != null && results.size() == 1) {
//        			if (results.get(0) instanceof DeviceInfo) {
//        				final DeviceInfo devInfo = (DeviceInfo)results.get(0);
//        				
//        				final long elapsedTime = System.currentTimeMillis() - devInfo.getLastMod().getTime();
//        				
//        				if (logger.isDebugEnabled()) {
//        					logger.debug(StringUtils.join(new Object[] {
//        						"check install key validity; installKeyTimeout=",
//        						installKeyTimeoutMs, "; elapsedTime=", elapsedTime, ";"}
//        					));
//        				}
//        				
//						isValid = elapsedTime <= installKeyTimeoutMs;
//        			}
//        		}
//        		
//    		} catch (Exception e) {
//    			Util.rollback(sesh);
//    			
//    			logger.error("err", e);
//    			
//    		} finally {
//    			Util.close(sesh);
//    		}
//		}
//		
//		return isValid;
//	}
//	
//	public boolean saveFavType(int userId, int typeId) {
//		Session sesh = getSession();
//		
//		boolean storeSuccess = false;
//		
//		try {
//    		sesh.beginTransaction();
//    		
//			final User user = (User) sesh.get(User.class, userId);
//			
//			final LvMemeType type = (LvMemeType) sesh.get(LvMemeType.class, typeId);
//			
//			UserFavMemeType favType = new UserFavMemeType();
//			favType.setActive(true);
//			favType.setLvMemeType(type);
//			favType.setUser(user);
//			
//			final int resultId = Util.getId(sesh.save(favType));
//			
//			storeSuccess = resultId > 0;
//			
//			Util.commit(sesh);
//			
//		} catch (Exception e) {
//			Util.rollback(sesh);
//			
//			logger.error("err while store fav type", e);
//			
//		} finally {
//			Util.close(sesh);
//		}
//		
//		return storeSuccess;
//	}
//
//
//	public boolean removeFavType(int userId, int typeId) {
//		boolean removeSuccess = false;
//		
//		final String queryString = StringUtils.join(new Object[] {
//			"delete ",
//			"from ", 
//			"	UserFavMemeType ufmt ",
//			"where ",
//			"	ufmt.lvMemeType.id = :typeid and ",
//			"	ufmt.user.id = :userid"
//		});
//		
//		Session sesh = getSession();
//		
//		try {
//			sesh.beginTransaction();
//			
//			final Query qry = sesh.createQuery(queryString);
//			qry.setInteger("userid", userId);
//			qry.setInteger("typeid", typeId);
//	
//			final int resultCount = qry.executeUpdate();
//			removeSuccess = resultCount > 0;
//			
//			Util.commit(sesh);
//			
//		} catch (Exception e) {
//			Util.rollback(sesh);
//			
//			logger.error("Err", e);
//			
//		} finally {
//			Util.close(sesh);
//		}
//		
//		return removeSuccess;
//	}
//
//
//	public List<ShallowMemeType> getPopularMemeTypes() {
//		final List<ShallowMemeType> types = new ArrayList<ShallowMemeType>();
//
//		final Session sesh = getSession();
//
//		List<MemeTypePopularity> typePopList = null;
//		try {
//			sesh.beginTransaction();
//
//			final Query qry = sesh.createQuery("from MemeTypePopularity");
//
//			final List<?> results = qry.list();
//
//			typePopList = new ArrayList<MemeTypePopularity>();
//			boolean typeMatch = false;
//			for (final Object ea : results) {
//				if (typeMatch || ea instanceof MemeTypePopularity) {
//					final MemeTypePopularity p = (MemeTypePopularity) ea;
//
//					typePopList.add(p);
//
//					typeMatch = true;
//				}
//			}
//
//			if (typePopList != null && typePopList.size() > 1) {
//				Collections.sort(typePopList, new Comparator<MemeTypePopularity>() {
//					@Override
//					public int compare(MemeTypePopularity o1, MemeTypePopularity o2) {
//						int result = 0;
//
//						if (o1 == null && o2 == null) {
//							result = 0;
//
//						} else if (o1 != null && o2 == null) {
//							result = -1;
//
//						} else if (o1 == null && o2 != null) {
//							result = 1;
//
//						} else {
//							result = o1.getRanking().compareTo(o2.getRanking());
//						}
//
//						return result;
//					}
//				});
//
//				for (final MemeTypePopularity eaTypePop : typePopList) {
//					types.add(populateBackgroundId(sesh, eaTypePop.getLvMemeType()));
//				}
//			}
//
//		} catch (Exception e) {
//			Util.rollback(sesh);
//
//			logger.error("error occurred while attempting to get all meme types", e);
//
//		} finally {
//			Util.close(sesh);
//		}
//
//		return types;
//	}
//	
//	private ShallowMemeType populateBackgroundId(final Session sesh, LvMemeType type) {
//		final ShallowMemeType eaShallowType = new ShallowMemeType(type);
//
//		final Query bgQry = sesh.createQuery("from Meme m where m.lvMemeType.id = :typeid and m.isSampleMeme = true");
//		bgQry.setInteger("typeid", type.getId());
//
//		final List<?> memeListForBgs = bgQry.list();
//
//		boolean isTypeMeme = false;
//		if (memeListForBgs != null) {
//			final Iterator<?> iterator = memeListForBgs.iterator();
//
//			if (iterator.hasNext()) {
//				final Object ea = iterator.next();
//
//				if (isTypeMeme || ea instanceof Meme) {
//					final Meme eaMeme = (Meme) ea;
//					eaShallowType.setBackgroundId(eaMeme.getMemeBackground().getId());
//				}
//			}
//		}
//
//		return eaShallowType;
//	}
//
//
//	public List<ShallowMemeType> getTypesForSearch(String searchTerm) {
//		final List<ShallowMemeType> types = new ArrayList<ShallowMemeType>();
//
//		if (StringUtils.isNotBlank(searchTerm)) {
//    		final Session sesh = getSession();
//    		
//    		try {
//    			sesh.beginTransaction();
//    			
//        		final Query qry = sesh.createQuery("from LvMemeType lmt where lower(lmt.descr) like :searchterm");
//        		qry.setString("searchterm", StringUtils.join(new Object[] {
//        			"%", StringUtils.lowerCase(StringUtils.trim(searchTerm)), "%" 
//    			}));
//        		
//    			final List<?> results = qry.list();
//    
//    			boolean typeMatch = false;
//    			for (final Object ea : results) {
//    				if (typeMatch || ea instanceof LvMemeType) {
//    					final LvMemeType eaLvType = ((LvMemeType)ea);
//    					
//    					types.add(populateBackgroundId(sesh, eaLvType));
//    					
//    					typeMatch = true;
//    				}
//    			}
//    
//    		} catch (Exception e) {
//    			Util.rollback(sesh);
//    			
//    			logger.error("error occurred while attempting to get all meme types", e);
//    
//    		} finally {
//    			Util.close(sesh);
//    		}
//		}
//
//		return types;
//	}
//
//
//	public Boolean storeCrawlerMemes(final List<CrawlerMeme> memes) {
//		
//		final Session sesh = getSession();
//		
//		boolean success = false;
//		
//		try {
//			sesh.beginTransaction();
//			
//			int i = 0;
//			for (final CrawlerMeme ea : memes) {
//    			final int id = Util.getId(sesh.save(ea));
//    			
//    			if (id <= 0) {
//    				break;
//    				
//    			} else if (++i == memes.size()) {
//    				success = true;
//    			}
//    			
//			}
//			
//			if (success) {
//				Util.commit(sesh);
//			}
//			
//		} catch (Exception e) {
//			Util.rollback(sesh);
//			
//			logger.error("error occurred while storing crawler memes", e);
//
//		} finally {
//			Util.close(sesh);
//		}
//
//		return success;
//	}
//	
//	public Boolean storeCrawlerBackground(final List<CrawlerBackground> memes) {
//		
//		final Session sesh = getSession();
//		
//		boolean success = false;
//		
//		try {
//			sesh.beginTransaction();
//			
//			int i = 0;
//			for (final CrawlerBackground ea : memes) {
//    			final int id = Util.getId(sesh.save(ea));
//    			
//    			if (id <= 0) {
//    				break;
//    				
//    			} else if (++i == memes.size()) {
//    				success = true;
//    			}
//    			
//			}
//			
//			if (success) {
//				Util.commit(sesh);
//			}
//			
//		} catch (Exception e) {
//			Util.rollback(sesh);
//			
//			logger.error("error occurred while storing crawler backgrounds", e);
//
//		} finally {
//			Util.close(sesh);
//		}
//
//		return success;
//	}
}






























