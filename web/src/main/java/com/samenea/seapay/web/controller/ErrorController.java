package com.samenea.seapay.web.controller;

import com.samenea.commons.webmvc.controller.BaseController;
import com.samenea.seapay.web.model.View;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller

public class ErrorController extends BaseController {
	@RequestMapping(value="error/exception" , method=RequestMethod.GET)
	public String exception(){
		
		 	return View.Error.Excption;
	}
	@RequestMapping(value="error/pageNotFound" , method=RequestMethod.GET)
	public String pageNotFound(){
		
		return View.Error.PAGE_NOT_FOUND;
	}
}
