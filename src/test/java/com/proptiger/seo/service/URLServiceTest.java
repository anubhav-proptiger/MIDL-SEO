package com.proptiger.seo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.proptiger.core.service.AbstractTest;
import com.proptiger.seo.service.URLService.ValidURLResponse;

public class URLServiceTest extends AbstractTest{
    @Autowired
    private URLService  urlService;
    
    @Test
    public void testGetURLStatus() {
        ValidURLResponse validURLResponse = urlService.getURLStatus("bangalore-real-estate/electronics-city-overview-50186");
        assertEquals("HttpStatus Matched ", 200, validURLResponse.getHttpStatus());
        assertEquals("Http URL Matched ", null, validURLResponse.getRedirectUrl());
        
        validURLResponse = urlService.getURLStatus("noida-real-estate/electronics-city-overview-50186");
        assertEquals("HttpStatus Matched ", 301, validURLResponse.getHttpStatus());
        assertEquals("Http URL Matched ", "bangalore-real-estate/electronics-city-overview-50186", validURLResponse.getRedirectUrl());
    }
}
