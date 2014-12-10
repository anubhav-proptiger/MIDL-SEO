package com.proptiger.seo.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proptiger.seo.processor.handler.SeoEventHandler;

@Service
public class SeoEventInitiator {
    /*private static Logger                logger = LoggerFactory.getLogger(SeoEventInitiator.class);

    @Autowired
    private SeoEventHandler seoEventHandler;
    
    public void generateUrl(){
        Thread.currentThread().setName("Seo Event Initiator.");

        logger.info("SEO Event URL Generator starting.");
        int numberOfUrls = seoEventHandler.generateUrls(100);
        logger.info("SEO Event URL Generator : generated "+numberOfUrls+" urls. ");
    }*/
}
