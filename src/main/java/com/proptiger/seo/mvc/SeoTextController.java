/**
 * 
 */
package com.proptiger.seo.mvc;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.proptiger.core.pojo.response.APIResponse;
import com.proptiger.data.model.URLDetail;
import com.proptiger.data.service.SeoPageService;
import com.proptiger.data.service.URLService;

/**
 * @author mandeep
 * 
 */
@Controller
@RequestMapping()
public class SeoTextController {
    private RestTemplate   restTemplate = new RestTemplate();

    @Value("${proptiger.url}")
    private String         websiteHost;

    @Autowired
    private SeoPageService seoPageService;

    @Autowired
    private URLService     urlService;

    @RequestMapping(value = "data/v1/seo-text", params = { "url" })
    @ResponseBody
    @Deprecated
    public APIResponse get(@RequestParam String url) {
        return new APIResponse(new Gson().fromJson(
                restTemplate.getForObject(
                        websiteHost + "getSeoTags.php?url={URL}",
                        String.class,
                        Collections.singletonMap("URL", url)),
                Object.class));
    }

    @RequestMapping(value = "data/v1/seo-text", params = { "urlDetails" })
    @ResponseBody
    public APIResponse getSeo(@RequestParam String urlDetails) throws FileNotFoundException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {

        URLDetail objectUrlDetails = new Gson().fromJson(urlDetails, URLDetail.class);
        if (objectUrlDetails.getUrl() == null) {
            throw new IllegalArgumentException("URL Field should not be empty.");
        }
        if (objectUrlDetails.getTemplateId() == null || objectUrlDetails.getTemplateId().isEmpty()) {
            throw new IllegalArgumentException("Template Id Field should not be empty.");
        }

        return new APIResponse(seoPageService.getSeoContentForPage(objectUrlDetails));
    }

}
