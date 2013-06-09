package com.eastapps.mgs.web;

import com.eastapps.mgs.model.MemeText;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/memetexts")
@Controller
@RooWebScaffold(path = "memetexts", formBackingObject = MemeText.class)
public class MemeTextController {
}
