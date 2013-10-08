package com.eastapps.mgs.web;

import com.eastapps.mgs.model.Meme;
import com.eastapps.mgs.model.MemeBackground;
import com.eastapps.mgs.model.MemeText;
import com.eastapps.mgs.model.MemeUser;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/memes")
@Controller
@RooWebScaffold(path = "memes", formBackingObject = Meme.class)
public class MemeController {

    private static final Logger logger = Logger.getLogger(MemeController.class);

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Meme meme, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, meme);
            return "memes/create";
        }
        uiModel.asMap().clear();
        meme.persist();
        return "redirect:/memes/" + encodeUrlPathSegment(meme.getId().toString(), httpServletRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create/json")
    @ResponseBody
    public Long createJson(@RequestBody @Valid Meme meme, BindingResult bindingResult, HttpServletRequest httpServletRequest) {
        long result = -1L;
        if (bindingResult.hasErrors()) {
            logger.warn(StringUtils.join("validation error for meme {", meme, "}"));
        } else {
            if (logger.isTraceEnabled()) {
                logger.trace(StringUtils.join("successfully validated meme {", meme, "}"));
            }
            
            if (meme.getTopText() != null) {
            	meme.getTopText().setId(null);
            }
            
            if (meme.getBottomText() != null) {
            	meme.getBottomText().setId(null);
            }
            
            meme.setCreatedByUser(MemeUser.findMemeUser(meme.getCreatedByUser().getId()));
            meme.setMemeBackground(MemeBackground.findMemeBackground(meme.getMemeBackground().getId()));
            
            meme.setId(null);
            meme.persist();
            result = meme.getId();
        }
        if (logger.isTraceEnabled()) {
            logger.trace(StringUtils.join("meme id after store attempt: [", String.valueOf(result), "]"));
        }
        return result;
    }
    
    @RequestMapping(value = "/get_for_user/json//{page}/{size}/{user_id}")
    @ResponseBody
    public List<Meme> getMemesForUser(
    	@PathVariable("user_id") final Long userId,
    	@PathVariable("page") final Integer page,
    	@PathVariable("size") final Integer size) 
	{
    	List<Meme> results = new ArrayList<Meme>(0);
    	if (page != null || size != null) {
    		final int sizeNo = size == null ? 10 : size.intValue();
    		final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
    		results = Meme.findMemesForUserId(userId, firstResult, sizeNo);
    	}
    	
    	return results;
	}

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Meme());
        return "memes/create";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("meme", Meme.findMeme(id));
        uiModel.addAttribute("itemId", id);
        return "memes/show";
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("memes", Meme.findMemeEntries(firstResult, sizeNo));
            float nrOfPages = (float) Meme.countMemes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("memes", Meme.findAllMemes());
        }
        return "memes/list";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Meme meme, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, meme);
            return "memes/update";
        }
        uiModel.asMap().clear();
        meme.merge();
        return "redirect:/memes/" + encodeUrlPathSegment(meme.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Meme.findMeme(id));
        return "memes/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Meme meme = Meme.findMeme(id);
        meme.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/memes";
    }

    void populateEditForm(Model uiModel, Meme meme) {
        uiModel.addAttribute("meme", meme);
        uiModel.addAttribute("memebackgrounds", MemeBackground.findAllMemeBackgrounds());
        uiModel.addAttribute("memetexts", MemeText.findAllMemeTexts());
        uiModel.addAttribute("memeusers", MemeUser.findAllMemeUsers());
    }

    String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {
        }
        return pathSegment;
    }
}
