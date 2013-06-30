package com.eastapps.mgs.web;

import com.eastapps.mgs.model.DeviceInfo;
import com.eastapps.mgs.model.MemeUser;
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

@RequestMapping("/deviceinfoes")
@Controller
@RooWebScaffold(path = "deviceinfoes", formBackingObject = DeviceInfo.class)
public class DeviceInfoController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid DeviceInfo deviceInfo, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, deviceInfo);
            return "deviceinfoes/create";
        }
        uiModel.asMap().clear();
        deviceInfo.persist();
        return "redirect:/deviceinfoes/" + encodeUrlPathSegment(deviceInfo.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new DeviceInfo());
        return "deviceinfoes/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("deviceinfo", DeviceInfo.findDeviceInfo(id));
        uiModel.addAttribute("itemId", id);
        return "deviceinfoes/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("deviceinfoes", DeviceInfo.findDeviceInfoEntries(firstResult, sizeNo));
            float nrOfPages = (float) DeviceInfo.countDeviceInfoes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("deviceinfoes", DeviceInfo.findAllDeviceInfoes());
        }
        return "deviceinfoes/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid DeviceInfo deviceInfo, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, deviceInfo);
            return "deviceinfoes/update";
        }
        uiModel.asMap().clear();
        deviceInfo.merge();
        return "redirect:/deviceinfoes/" + encodeUrlPathSegment(deviceInfo.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, DeviceInfo.findDeviceInfo(id));
        return "deviceinfoes/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        DeviceInfo deviceInfo = DeviceInfo.findDeviceInfo(id);
        deviceInfo.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/deviceinfoes";
    }

	void populateEditForm(Model uiModel, DeviceInfo deviceInfo) {
        uiModel.addAttribute("deviceInfo", deviceInfo);
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
