package com.eastapps.mgs.web;

import com.eastapps.meme_gen_server.util.Util;
import com.eastapps.mgs.model.DeviceInfo;
import com.eastapps.mgs.model.Meme;
import com.eastapps.mgs.model.MemeUser;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

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

@RequestMapping("/memeusers")
@Controller
@RooWebScaffold(path = "memeusers", formBackingObject = MemeUser.class)
public class MemeUserController {
	private static final Logger logger = Logger.getLogger(MemeUserController.class);

	@RequestMapping(method = RequestMethod.POST, value = "/create/json")
	@ResponseBody
	public Long createJson(@RequestBody @Valid final MemeUser memeUser, final BindingResult bindingResult, final HttpServletRequest httpServletRequest) {
		long userId = -1L;
		if (bindingResult.hasErrors()) {
			logger.warn(StringUtils.join("validation error for meme user {", memeUser, "}"));
			
		} else {
			if (logger.isTraceEnabled()) {
				logger.trace(StringUtils.join("successfully validated meme {", memeUser, "}"));
			}
			
			memeUser.setId(null);
			memeUser.persist();
			userId = memeUser.getId();
			
		}
		
		if (logger.isTraceEnabled()) {
			logger.trace(StringUtils.join("meme user id after store attempt: [", String.valueOf(userId), "]"));
		}
		
		return userId;
	}

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid MemeUser memeUser, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, memeUser);
            return "memeusers/create";
        }
        uiModel.asMap().clear();
        memeUser.persist();
        return "redirect:/memeusers/" + encodeUrlPathSegment(memeUser.getId().toString(), httpServletRequest);
    }

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new MemeUser());
        return "memeusers/create";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("memeuser", MemeUser.findMemeUser(id));
        uiModel.addAttribute("itemId", id);
        return "memeusers/show";
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("memeusers", MemeUser.findMemeUserEntries(firstResult, sizeNo));
            float nrOfPages = (float) MemeUser.countMemeUsers() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("memeusers", MemeUser.findAllMemeUsers());
        }
        return "memeusers/list";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid MemeUser memeUser, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, memeUser);
            return "memeusers/update";
        }
        uiModel.asMap().clear();
        memeUser.merge();
        return "redirect:/memeusers/" + encodeUrlPathSegment(memeUser.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, MemeUser.findMemeUser(id));
        return "memeusers/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        MemeUser memeUser = MemeUser.findMemeUser(id);
        memeUser.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/memeusers";
    }

    void populateEditForm(Model uiModel, MemeUser memeUser) {
        uiModel.addAttribute("memeUser", memeUser);
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
