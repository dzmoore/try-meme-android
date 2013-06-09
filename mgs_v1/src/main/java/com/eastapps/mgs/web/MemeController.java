package com.eastapps.mgs.web;

import com.eastapps.mgs.model.Meme;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/memes")
@Controller
@RooWebScaffold(path = "memes", formBackingObject = Meme.class)
public class MemeController {
}
