package com.eastapps.mgs.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.eastapps.meme_gen_server.service.MemeService;
import com.eastapps.mgs.model.MemeBackground;

@RequestMapping("/memebackgrounds")
@Controller
@RooWebScaffold(path = "memebackgrounds", formBackingObject = MemeBackground.class)
public class MemeBackgroundController {

	private MemeService memeService;


	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid MemeBackground memeBackground, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, memeBackground);
            return "memebackgrounds/create";
        }
        uiModel.asMap().clear();
        memeBackground.persist();
        return "redirect:/memebackgrounds/" + encodeUrlPathSegment(memeBackground.getId().toString(), httpServletRequest);
    }

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new MemeBackground());
        return "memebackgrounds/create";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("memebackground", MemeBackground.findMemeBackground(id));
        uiModel.addAttribute("itemId", id);
        return "memebackgrounds/show";
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("memebackgrounds", MemeBackground.findMemeBackgroundEntries(firstResult, sizeNo));
            float nrOfPages = (float) MemeBackground.countMemeBackgrounds() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("memebackgrounds", MemeBackground.findAllMemeBackgrounds());
        }
        return "memebackgrounds/list";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid MemeBackground memeBackground, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, memeBackground);
            return "memebackgrounds/update";
        }
        uiModel.asMap().clear();
        memeBackground.merge();
        return "redirect:/memebackgrounds/" + encodeUrlPathSegment(memeBackground.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, MemeBackground.findMemeBackground(id));
        return "memebackgrounds/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        MemeBackground memeBackground = MemeBackground.findMemeBackground(id);
        memeBackground.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/memebackgrounds";
    }

    void populateEditForm(Model uiModel, MemeBackground memeBackground) {
        uiModel.addAttribute("memeBackground", memeBackground);
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
    
    
	@RequestMapping(value = "/background_bytes/{id}", method = RequestMethod.GET)
	@ResponseBody
	public byte[] getMemeBackground(@PathVariable("id") final int id) throws IOException {
		return memeService.getMemeBackgroundBytes(id);
	}
}
