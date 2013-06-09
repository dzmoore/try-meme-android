package com.eastapps.mgs.web;

import com.eastapps.mgs.model.MemeUser;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/memeusers")
@Controller
@RooWebScaffold(path = "memeusers", formBackingObject = MemeUser.class)
public class MemeUserController {
}
