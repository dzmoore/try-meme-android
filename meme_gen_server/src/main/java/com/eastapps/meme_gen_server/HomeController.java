package com.eastapps.meme_gen_server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eastapps.meme_gen_server.domain.IntResult;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.meme_gen_server.domain.ShallowMemeType;
import com.eastapps.meme_gen_server.domain.ShallowUser;
import com.eastapps.meme_gen_server.domain.User;
import com.eastapps.meme_gen_server.service.MemeService;
import com.eastapps.meme_gen_server.util.Util;

/**
 * Sample controller for going to the home page with a message
 */
@Controller
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private MemeService memeService;

	@Autowired
	public HomeController(final MemeService memeService) {
		super();
		this.memeService = memeService;
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
	
	@RequestMapping(value = "/store_meme", method = RequestMethod.POST)
    @ResponseBody
	public IntResult storeMeme(@RequestBody ShallowMeme shallowMeme) {
		IntResult result = new IntResult();
		
		result.setResult(memeService.storeMeme(shallowMeme));
		return result;
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
		memeService.addMemeAdmin(backgroundFileName, memeTitle, sample1TopText, sample1BottomText, sample2TopText, sample2BottomText, sample3TopText, sample3BottomText, model);

		return "home";
	}

	@RequestMapping(value = "/add_meme_admin", method = RequestMethod.GET)
	public String addMemeAdmin() {
		return "add_meme_admin";
	}

	@RequestMapping(value = "/meme_data/{id}/background", method = RequestMethod.GET)
	@ResponseBody
	public byte[] getMemeBackground(@PathVariable("id") final int memeId) throws IOException {
		return memeService.getMemeBackground(memeId);
	}

	@RequestMapping(value = { "/meme_data/{meme_id}/json" }, method = RequestMethod.GET)
	@ResponseBody
	public ShallowMeme getMeme(@PathVariable("meme_id") final int memeId) {
		ShallowMeme result = memeService.getShallowMeme(memeId);
		
		logger.debug(Util.concat("getmeme: id=", memeId, "] result=", result, "]"));
		
		return result;
	}
	
	@RequestMapping(value = {"/sample_meme_data/{meme_type_id}/json"}, method = RequestMethod.GET)
	@ResponseBody
	public ShallowMeme[] getSampleMemes(@PathVariable("meme_type_id") final int memeTypeId) {
		List<ShallowMeme> samples = new ArrayList<ShallowMeme>();
		
		try {
    		samples = memeService.getSampleMemes(memeTypeId);
    		
		} catch (Exception e) {
			logger.error("error occurred", e);
		}
		
		return samples.toArray(new ShallowMeme[samples.size()]);
	}
	
	
	@RequestMapping(value = {"/meme_type_data/json"}, method = RequestMethod.GET)
	@ResponseBody
	public ShallowMemeType[] getAllMemeTypes() {
		List<ShallowMemeType> types = Collections.emptyList();
		
		try {
    		types = memeService.getAllMemeTypes();
    		
		} catch (Exception e) {
			logger.error("error occurred while attempting to retrieve meme data", e);
		}
		
		return types.toArray(new ShallowMemeType[types.size()]);
	}

	
	@RequestMapping(value = "/meme_type_data/{id}/background", method = RequestMethod.GET)
	@ResponseBody
	public byte[] getThumbForType(@PathVariable("id") final int typeId) throws IOException {
		return memeService.getThumbForType(typeId);
	}
	
	@RequestMapping(value = "/user_data/user/{id}/json")
	@ResponseBody
	public ShallowUser getUserForId(@PathVariable("id") final int userId) {
		
		
		return null;
	}
	
	
	@RequestMapping(value = "/user_data/device/{id}/json")
	@ResponseBody
	public ShallowUser getUserForDeviceId(@PathVariable("id") final String deviceId) {
//		final ShallowUser user = 
		
		return null;
	}
	
	@RequestMapping(value = "/user_data/user/{id}/favtypes/json")
	@ResponseBody
	public ShallowMemeType[] getFavTypesForUserId(@PathVariable("id") final int userId) {
		final List<ShallowMemeType> types = memeService.getFavoriteMemeTypesForUser(userId);
	
		ShallowMemeType[] typeArr = new ShallowMemeType[0];
		if (types != null && types.size() > 0) {
			typeArr = new ShallowMemeType[types.size()];
		}
	
		return types.toArray(typeArr);
	}
	
	@RequestMapping(value = "/user_data/new_install_key/json")
	@ResponseBody
	public String getNewInstallKey() {
		return memeService.getNewInstallKey();
	}

	@RequestMapping(value = "/user_data/store_new/json")
	public int storeNewUser(@RequestBody final ShallowUser user) {
	    return memeService.storeNewUser(user);
	}


}

































