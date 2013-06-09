package com.eastapps.mgs.web;

import com.eastapps.mgs.model.SampleMeme;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/samplememes")
@Controller
@RooWebScaffold(path = "samplememes", formBackingObject = SampleMeme.class)
public class SampleMemeController {
}
