package com.eastapps.mgs.web;

import com.eastapps.mgs.model.CrawlerBackground;
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

@RequestMapping("/crawlerbackgrounds")
@Controller
@RooWebScaffold(path = "crawlerbackgrounds", formBackingObject = CrawlerBackground.class)
public class CrawlerBackgroundController {

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid CrawlerBackground crawlerBackground, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, crawlerBackground);
            return "crawlerbackgrounds/create";
        }
        uiModel.asMap().clear();
        crawlerBackground.persist();
        return "redirect:/crawlerbackgrounds/" + encodeUrlPathSegment(crawlerBackground.getId().toString(), httpServletRequest);
    }

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new CrawlerBackground());
        return "crawlerbackgrounds/create";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("crawlerbackground", CrawlerBackground.findCrawlerBackground(id));
        uiModel.addAttribute("itemId", id);
        return "crawlerbackgrounds/show";
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("crawlerbackgrounds", CrawlerBackground.findCrawlerBackgroundEntries(firstResult, sizeNo));
            float nrOfPages = (float) CrawlerBackground.countCrawlerBackgrounds() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("crawlerbackgrounds", CrawlerBackground.findAllCrawlerBackgrounds());
        }
        return "crawlerbackgrounds/list";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid CrawlerBackground crawlerBackground, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, crawlerBackground);
            return "crawlerbackgrounds/update";
        }
        uiModel.asMap().clear();
        crawlerBackground.merge();
        return "redirect:/crawlerbackgrounds/" + encodeUrlPathSegment(crawlerBackground.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, CrawlerBackground.findCrawlerBackground(id));
        return "crawlerbackgrounds/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        CrawlerBackground crawlerBackground = CrawlerBackground.findCrawlerBackground(id);
        crawlerBackground.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crawlerbackgrounds";
    }

    void populateEditForm(Model uiModel, CrawlerBackground crawlerBackground) {
        uiModel.addAttribute("crawlerBackground", crawlerBackground);
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
