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

import com.eastapps.meme_gen_server.constants.Constants;
import com.eastapps.meme_gen_server.domain.IntResult;
import com.eastapps.meme_gen_server.domain.LvMemeType;
import com.eastapps.meme_gen_server.domain.Meme;
import com.eastapps.meme_gen_server.domain.MemeBackground;
import com.eastapps.meme_gen_server.domain.MemeText;
import com.eastapps.meme_gen_server.domain.SampleMeme;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
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

	@RequestMapping(value = { "/meme_data/{id}/json" }, method = RequestMethod.GET)
	@ResponseBody
	public ShallowMeme getMeme(@PathVariable("id") final int memeId) {
		return new ShallowMeme(memeService.getMeme(memeId));
	}


}
