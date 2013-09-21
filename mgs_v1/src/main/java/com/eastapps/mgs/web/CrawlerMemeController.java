package com.eastapps.mgs.web;

import com.eastapps.mgs.model.CrawlerMeme;
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

@RequestMapping("/crawlermemes")
@Controller
@RooWebScaffold(path = "crawlermemes", formBackingObject = CrawlerMeme.class)
public class CrawlerMemeController {

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid CrawlerMeme crawlerMeme, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, crawlerMeme);
            return "crawlermemes/create";
        }
        uiModel.asMap().clear();
        crawlerMeme.persist();
        return "redirect:/crawlermemes/" + encodeUrlPathSegment(crawlerMeme.getId().toString(), httpServletRequest);
    }

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new CrawlerMeme());
        return "crawlermemes/create";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("crawlermeme", CrawlerMeme.findCrawlerMeme(id));
        uiModel.addAttribute("itemId", id);
        return "crawlermemes/show";
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("crawlermemes", CrawlerMeme.findCrawlerMemeEntries(firstResult, sizeNo));
            float nrOfPages = (float) CrawlerMeme.countCrawlerMemes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("crawlermemes", CrawlerMeme.findAllCrawlerMemes());
        }
        return "crawlermemes/list";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid CrawlerMeme crawlerMeme, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, crawlerMeme);
            return "crawlermemes/update";
        }
        uiModel.asMap().clear();
        crawlerMeme.merge();
        return "redirect:/crawlermemes/" + encodeUrlPathSegment(crawlerMeme.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, CrawlerMeme.findCrawlerMeme(id));
        return "crawlermemes/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        CrawlerMeme crawlerMeme = CrawlerMeme.findCrawlerMeme(id);
        crawlerMeme.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crawlermemes";
    }

    void populateEditForm(Model uiModel, CrawlerMeme crawlerMeme) {
        uiModel.addAttribute("crawlerMeme", crawlerMeme);
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
