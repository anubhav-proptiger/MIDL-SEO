package com.proptiger.seo.processor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proptiger.core.enums.DomainObject;
import com.proptiger.core.model.event.EventGenerated;
import com.proptiger.seo.model.SeoURLs;
import com.proptiger.seo.model.URLCategories;
import com.proptiger.seo.model.URLDetail;
import com.proptiger.seo.service.SeoURLService;
import com.proptiger.seo.service.URLCreaterService;
//import com.proptiger.data.model.URLDetail;

@Service
public class SeoEventUrlCreator {
    @Autowired
    private URLCreaterService urlCreaterService;

    @Autowired
    private SeoURLService     seoURLService;
    
    public void generateUrls(DomainObject domainObject, EventGenerated eventGenerated, List<URLCategories> urlCategories) {

        switch (domainObject) {
            case property:
                generatePropertyURLS(eventGenerated, urlCategories);
                break;
            case project:
                generateProjectUrls(eventGenerated, urlCategories);
                break;
            case builder:
                generateBuilderUrls(eventGenerated, urlCategories);
                break;
            case locality:
                generateLocalityUrls(eventGenerated, urlCategories);
                break;
            case suburb:
                generateSuburbUrls(eventGenerated, urlCategories);
                break;
            case city:
                generateCityUrls(eventGenerated, urlCategories);
                break;

        }

    }

    public int generatePropertyURLS(EventGenerated eventGenerated, List<URLCategories> urlCategories) {
        URLDetail urlDetail = new URLDetail();
        String url = null;
        int totalUrls = 0;
        int objectId;

        List<SeoURLs> seoURLs = new ArrayList<SeoURLs>();
        for (URLCategories urlCategory : urlCategories) {
            objectId = Integer.parseInt(eventGenerated.getEventTypeUniqueKey());
            urlDetail.setUrlCategory(urlCategory);
            urlDetail.setPropertyId(objectId);
            url = urlCreaterService.urlLibPropertyUrl(urlDetail);
            seoURLs.add(seoURLService.createSeoURLObject(url, objectId, urlCategory));
        }
        seoURLService.saveDomainUrls(seoURLs, eventGenerated);

        return totalUrls;
    }

    public int generateProjectUrls(EventGenerated eventGenerated, List<URLCategories> urlCategories) {
        URLDetail urlDetail = new URLDetail();
        String url = null;
        int totalUrls = 0;
        int objectId;

        List<SeoURLs> seoURLs = new ArrayList<SeoURLs>();
        for (URLCategories urlCategory : urlCategories) {
            objectId = Integer.parseInt(eventGenerated.getEventTypeUniqueKey());
            urlDetail.setUrlCategory(urlCategory);
            urlDetail.setProjectId(Integer.parseInt(eventGenerated.getEventTypeUniqueKey()));
            url = urlCreaterService.urlLibProjectUrl(urlDetail);
            seoURLs.add(seoURLService.createSeoURLObject(url, objectId, urlCategory));
        }
        seoURLService.saveDomainUrls(seoURLs, eventGenerated);

        return totalUrls;
    }

    public int generateLocalityUrls(EventGenerated eventGenerated, List<URLCategories> urlCategories) {
        URLDetail urlDetail = new URLDetail();
        String url = null;
        int totalUrls = 0;
        int objectId;

        List<SeoURLs> seoURLs = new ArrayList<SeoURLs>();
        for (URLCategories urlCategory : urlCategories) {
            objectId = Integer.parseInt(eventGenerated.getEventTypeUniqueKey());
            urlDetail.setUrlCategory(urlCategory);
            urlDetail.setLocalityId(Integer.parseInt(eventGenerated.getEventTypeUniqueKey()));
            url = urlCreaterService.getLocalityUrl(urlDetail);
            seoURLs.add(seoURLService.createSeoURLObject(url, objectId, urlCategory));
        }
        seoURLService.saveDomainUrls(seoURLs, eventGenerated);

        return totalUrls;
    }

    public int generateSuburbUrls(EventGenerated eventGenerated, List<URLCategories> urlCategories) {
        URLDetail urlDetail = new URLDetail();
        String url = null;
        int totalUrls = 0;
        int objectId;

        List<SeoURLs> seoURLs = new ArrayList<SeoURLs>();
        for (URLCategories urlCategory : urlCategories) {
            objectId = Integer.parseInt(eventGenerated.getEventTypeUniqueKey());
            urlDetail.setUrlCategory(urlCategory);
            urlDetail.setSuburbId(Integer.parseInt(eventGenerated.getEventTypeUniqueKey()));
            url = urlCreaterService.getSuburbUrl(urlDetail);
            seoURLs.add(seoURLService.createSeoURLObject(url, objectId, urlCategory));
        }
        seoURLService.saveDomainUrls(seoURLs, eventGenerated);

        return totalUrls;
    }

    public int generateCityUrls(EventGenerated eventGenerated, List<URLCategories> urlCategories) {
        URLDetail urlDetail = new URLDetail();
        String url = null;
        int totalUrls = 0;
        int objectId;

        List<SeoURLs> seoURLs = new ArrayList<SeoURLs>();
        for (URLCategories urlCategory : urlCategories) {
            objectId = Integer.parseInt(eventGenerated.getEventTypeUniqueKey());
            urlDetail.setUrlCategory(urlCategory);
            urlDetail.setCityId(Integer.parseInt(eventGenerated.getEventTypeUniqueKey()));
            url = urlCreaterService.getCityUrl(urlDetail);
            seoURLs.add(seoURLService.createSeoURLObject(url, objectId, urlCategory));
        }
        seoURLService.saveDomainUrls(seoURLs, eventGenerated);

        return totalUrls;
    }
    
    /**
     * Adding the city builder urls.
     * @param eventGenerated
     * @param urlCategories
     * @return
     */
    public int generateBuilderUrls(EventGenerated eventGenerated, List<URLCategories> urlCategories) {
        URLDetail urlDetail = new URLDetail();
        String url = null;
        int totalUrls = 0;
        int objectId = Integer.parseInt(eventGenerated.getEventTypeUniqueKey());
        List<SeoURLs> seoURLs = new ArrayList<SeoURLs>();
        
        for (URLCategories urlCategory : urlCategories) {
            urlDetail.setUrlCategory(urlCategory);
            urlDetail.setBuilderId(Integer.parseInt(eventGenerated.getEventTypeUniqueKey()));
            url = urlCreaterService.getBuilderUrl(urlDetail);
            seoURLs.add(seoURLService.createSeoURLObject(url, objectId, urlCategory));
        }
        seoURLService.saveDomainUrls(seoURLs, eventGenerated);
        return totalUrls;
    }
}
