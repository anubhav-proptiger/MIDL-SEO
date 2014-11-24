package com.proptiger.seo.mvc;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proptiger.core.mvc.BaseController;
import com.proptiger.core.pojo.response.APIResponse;
import com.proptiger.seo.service.URLService;

@Controller
@RequestMapping
public class UrlController extends BaseController {
    @Autowired
    private URLService urlService;

    @RequestMapping("data/v1/url")
    @ResponseBody
    public APIResponse getProjects(@RequestParam String url, HttpServletResponse response) {
        return new APIResponse(urlService.getURLStatus(url));
    }

}
