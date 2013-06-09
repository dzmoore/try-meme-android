package com.eastapps.mgs.web;

import com.eastapps.mgs.model.MemeBackground;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/memebackgrounds")
@Controller
@RooWebScaffold(path = "memebackgrounds", formBackingObject = MemeBackground.class)
public class MemeBackgroundController {
}
