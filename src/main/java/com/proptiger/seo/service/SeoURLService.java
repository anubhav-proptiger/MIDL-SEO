package com.proptiger.seo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proptiger.data.event.model.EventGenerated;
import com.proptiger.data.notification.model.Subscriber;
import com.proptiger.data.notification.service.SubscriberConfigService;
import com.proptiger.seo.repo.SeoURLsDao;
import com.proptiger.seo.model.SeoURLs;
import com.proptiger.seo.model.URLCategories;
import com.proptiger.seo.model.SeoURLs.URLInfo;
import com.proptiger.seo.model.SeoURLs.URLStatus;

@Service
public class SeoURLService {
    @Autowired
    private SeoURLsDao seoURLsDao;
    
    @Autowired
    private SubscriberConfigService subscriberConfigService;

    public SeoURLs saveUrls(String url, int objectId, URLCategories urlCategories) {
        SeoURLs seoURLs = seoURLsDao.insertQuery(url, urlCategories.getId(), objectId, URLStatus.Active.name(), URLInfo.New.name(), URLInfo.ReActive.name());
        
        return seoURLs;
    }
    
    public SeoURLs createSeoURLObject(String url, int objectId, URLCategories urlCategories){
        return new SeoURLs(url, urlCategories, objectId);
    }
    
    @Transactional
    public void saveDomainUrls(List<SeoURLs> listSeoURLs, EventGenerated eventGenerated){
        for(SeoURLs seoURLs:listSeoURLs){
            saveUrls(seoURLs.getUrl(), seoURLs.getObjectId(), seoURLs.getUrlCategories());
        }
        subscriberConfigService.setLastEventGeneratedIdBySubscriber(eventGenerated.getId(), eventGenerated.getSubscriberMapping().get(0).getSubscriber());
    }
}
