package com.proptiger.seo.processor.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proptiger.core.enums.DomainObject;
import com.proptiger.data.event.enums.EventTypeName;
import com.proptiger.data.event.model.EventGenerated;
import com.proptiger.data.event.service.EventGeneratedService;
import com.proptiger.data.notification.model.Subscriber.SubscriberName;
import com.proptiger.seo.model.URLCategories;
import com.proptiger.seo.processor.SeoEventUrlCreator;
import com.proptiger.seo.service.URLCategoriesService;

@Service
public class SeoEventHandler {
    /*private static Logger         logger = LoggerFactory.getLogger(SeoEventHandler.class);

    @Autowired
    private EventGeneratedService eventGeneratedService;

    @Autowired
    private URLCategoriesService  urlCategoriesService;

    @Autowired
    private SeoEventUrlCreator    seoEventUrlCreator;

    public int generateUrls(int numberOfEvents) {
        List<EventGenerated> events = eventGeneratedService.getLatestGeneratedEventsBySubscriber(
                SubscriberName.Seo,
                getUrlGeneratorEventTypeList());
        logger.info("Fetched " + events.size() + " events for url generation.");
        Map<DomainObject, List<URLCategories>> groupCategoryMap = urlCategoriesService
                .getAllUrlCategoryByDomainObject();
        
        int totalUrls = 0;
        DomainObject domainObject = null;
        for(EventGenerated eventGenerated:events){
            domainObject = DomainObject.getDomainInstance(Long.parseLong(eventGenerated.getEventTypeUniqueKey()));
            seoEventUrlCreator.generateUrls(domainObject, eventGenerated, groupCategoryMap.get(domainObject));
            totalUrls++;
        }

        return totalUrls;
    }

    protected Map<DomainObject, List<EventGenerated>> groupEventsByEventType(List<EventGenerated> eventsGenerated) {
        Map<DomainObject, List<EventGenerated>> mapEvents = new HashMap<DomainObject, List<EventGenerated>>();
        List<EventGenerated> groupEvents;// = new ArrayList<EventGenerated>();

        DomainObject domainObject;
        for (EventGenerated eventGenerated : eventsGenerated) {
            domainObject = DomainObject.getDomainInstance(Long.parseLong(eventGenerated.getEventTypeUniqueKey()));
            groupEvents = mapEvents.get(domainObject);
            if (groupEvents == null) {
                groupEvents = new ArrayList<EventGenerated>();
            }
            groupEvents.add(eventGenerated);
            mapEvents.put(domainObject, groupEvents);
        }

        return mapEvents;
    }

    private List<String> getUrlGeneratorEventTypeList() {
        List<String> eventTypeNames = new ArrayList<String>();
        eventTypeNames.add(EventTypeName.BuilderGenerateUrl.getEventTypeName());
        eventTypeNames.add(EventTypeName.ProjectGenerateUrl.getEventTypeName());
        eventTypeNames.add(EventTypeName.CityGenerateUrl.getEventTypeName());
        eventTypeNames.add(EventTypeName.PropertyGenerateUrl.getEventTypeName());
        eventTypeNames.add(EventTypeName.SuburbGenerateUrl.getEventTypeName());
        eventTypeNames.add(EventTypeName.LocalityGenerateUrl.getEventTypeName());

        return eventTypeNames;
    }*/
}
