package com.eastapps.mgs.web;

import com.eastapps.mgs.model.LvPopularityType;
import com.eastapps.mgs.model.MemeBackground;
import com.eastapps.mgs.model.MemeBackgroundPopularity;
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
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/memebackgroundpopularitys")
@Controller
@RooWebScaffold(path = "memebackgroundpopularitys", formBackingObject = MemeBackgroundPopularity.class)
public class MemeBackgroundPopularityController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid MemeBackgroundPopularity memeBackgroundPopularity, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, memeBackgroundPopularity);
            return "memebackgroundpopularitys/create";
        }
        uiModel.asMap().clear();
        memeBackgroundPopularity.persist();
        return "redirect:/memebackgroundpopularitys/" + encodeUrlPathSegment(memeBackgroundPopularity.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new MemeBackgroundPopularity());
        return "memebackgroundpopularitys/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("memebackgroundpopularity", MemeBackgroundPopularity.findMemeBackgroundPopularity(id));
        uiModel.addAttribute("itemId", id);
        return "memebackgroundpopularitys/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("memebackgroundpopularitys", MemeBackgroundPopularity.findMemeBackgroundPopularityEntries(firstResult, sizeNo));
            float nrOfPages = (float) MemeBackgroundPopularity.countMemeBackgroundPopularitys() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("memebackgroundpopularitys", MemeBackgroundPopularity.findAllMemeBackgroundPopularitys());
        }
        return "memebackgroundpopularitys/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid MemeBackgroundPopularity memeBackgroundPopularity, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, memeBackgroundPopularity);
            return "memebackgroundpopularitys/update";
        }
        uiModel.asMap().clear();
        memeBackgroundPopularity.merge();
        return "redirect:/memebackgroundpopularitys/" + encodeUrlPathSegment(memeBackgroundPopularity.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, MemeBackgroundPopularity.findMemeBackgroundPopularity(id));
        return "memebackgroundpopularitys/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        MemeBackgroundPopularity memeBackgroundPopularity = MemeBackgroundPopularity.findMemeBackgroundPopularity(id);
        memeBackgroundPopularity.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/memebackgroundpopularitys";
    }

	void populateEditForm(Model uiModel, MemeBackgroundPopularity memeBackgroundPopularity) {
        uiModel.addAttribute("memeBackgroundPopularity", memeBackgroundPopularity);
        uiModel.addAttribute("lvpopularitytypes", LvPopularityType.findAllLvPopularityTypes());
        uiModel.addAttribute("memebackgrounds", MemeBackground.findAllMemeBackgrounds());
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
