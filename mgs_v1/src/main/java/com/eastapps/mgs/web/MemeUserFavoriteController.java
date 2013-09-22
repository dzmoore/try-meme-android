package com.eastapps.mgs.web;

import com.eastapps.mgs.model.MemeBackground;
import com.eastapps.mgs.model.MemeUser;
import com.eastapps.mgs.model.MemeUserFavorite;
import java.io.UnsupportedEncodingException;
import java.util.List;

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

@RequestMapping("/memeuserfavorites")
@Controller
@RooWebScaffold(path = "memeuserfavorites", formBackingObject = MemeUserFavorite.class)
public class MemeUserFavoriteController {

	@RequestMapping(value = "/backgroundsforuserid/json/{page}/{size}/{userid}")
	@ResponseBody
	public List<MemeBackground> findAllFavoriteMemeBackgroundsForUserId(
		@PathVariable("userid") final Long userId,
		@PathVariable("page") final Integer page,
		@PathVariable("size") final Integer size) 
	{
		int sizeNo = size == null ? 10 : size.intValue();
        final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
        return MemeUserFavorite.findAllFavoriteMemeBackgroundsForUserId(userId, firstResult, sizeNo);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid MemeUserFavorite memeUserFavorite, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, memeUserFavorite);
            return "memeuserfavorites/create";
        }
        uiModel.asMap().clear();
        memeUserFavorite.persist();
        return "redirect:/memeuserfavorites/" + encodeUrlPathSegment(memeUserFavorite.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new MemeUserFavorite());
        return "memeuserfavorites/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("memeuserfavorite", MemeUserFavorite.findMemeUserFavorite(id));
        uiModel.addAttribute("itemId", id);
        return "memeuserfavorites/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("memeuserfavorites", MemeUserFavorite.findMemeUserFavoriteEntries(firstResult, sizeNo));
            float nrOfPages = (float) MemeUserFavorite.countMemeUserFavorites() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("memeuserfavorites", MemeUserFavorite.findAllMemeUserFavorites());
        }
        return "memeuserfavorites/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid MemeUserFavorite memeUserFavorite, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, memeUserFavorite);
            return "memeuserfavorites/update";
        }
        uiModel.asMap().clear();
        memeUserFavorite.merge();
        return "redirect:/memeuserfavorites/" + encodeUrlPathSegment(memeUserFavorite.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, MemeUserFavorite.findMemeUserFavorite(id));
        return "memeuserfavorites/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        MemeUserFavorite memeUserFavorite = MemeUserFavorite.findMemeUserFavorite(id);
        memeUserFavorite.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/memeuserfavorites";
    }

	void populateEditForm(Model uiModel, MemeUserFavorite memeUserFavorite) {
        uiModel.addAttribute("memeUserFavorite", memeUserFavorite);
        uiModel.addAttribute("memebackgrounds", MemeBackground.findAllMemeBackgrounds());
        uiModel.addAttribute("memeusers", MemeUser.findAllMemeUsers());
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
