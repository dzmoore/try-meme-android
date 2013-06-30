package com.eastapps.mgs.web;

import com.eastapps.mgs.model.Meme;
import com.eastapps.mgs.model.MemeBackground;
import com.eastapps.mgs.model.SampleMeme;
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

@RequestMapping("/samplememes")
@Controller
@RooWebScaffold(path = "samplememes", formBackingObject = SampleMeme.class)
public class SampleMemeController {

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid SampleMeme sampleMeme, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, sampleMeme);
            return "samplememes/create";
        }
        uiModel.asMap().clear();
        sampleMeme.persist();
        return "redirect:/samplememes/" + encodeUrlPathSegment(sampleMeme.getId().toString(), httpServletRequest);
    }

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new SampleMeme());
        return "samplememes/create";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("samplememe", SampleMeme.findSampleMeme(id));
        uiModel.addAttribute("itemId", id);
        return "samplememes/show";
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("samplememes", SampleMeme.findSampleMemeEntries(firstResult, sizeNo));
            float nrOfPages = (float) SampleMeme.countSampleMemes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("samplememes", SampleMeme.findAllSampleMemes());
        }
        return "samplememes/list";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid SampleMeme sampleMeme, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, sampleMeme);
            return "samplememes/update";
        }
        uiModel.asMap().clear();
        sampleMeme.merge();
        return "redirect:/samplememes/" + encodeUrlPathSegment(sampleMeme.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, SampleMeme.findSampleMeme(id));
        return "samplememes/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        SampleMeme sampleMeme = SampleMeme.findSampleMeme(id);
        sampleMeme.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/samplememes";
    }

    void populateEditForm(Model uiModel, SampleMeme sampleMeme) {
        uiModel.addAttribute("sampleMeme", sampleMeme);
        uiModel.addAttribute("memes", Meme.findAllMemes());
        uiModel.addAttribute("memebackgrounds", MemeBackground.findAllMemeBackgrounds());
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
