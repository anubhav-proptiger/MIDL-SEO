package com.proptiger.seo.processor.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.proptiger.core.enums.DomainObject;
import com.proptiger.core.enums.event.DBOperation;
import com.proptiger.core.enums.event.EventTypeEnum;
import com.proptiger.core.model.event.EventGenerated;
import com.proptiger.core.model.event.subscriber.Subscriber.SubscriberName;
import com.proptiger.core.model.solr.DynamicSolrIndex;
import com.proptiger.seo.interceptor.ResponseInterceptor;
import com.proptiger.seo.model.URLCategories;
import com.proptiger.seo.processor.SeoEventUrlCreator;
import com.proptiger.seo.service.URLCategoriesService;

@Service
public class SeoEventHandler {
	private static Logger logger = LoggerFactory
			.getLogger(SeoEventHandler.class);

	@Autowired
	private ResponseInterceptor responseInterceptor;

	@Autowired
	private URLCategoriesService urlCategoriesService;

	@Autowired
	private SeoEventUrlCreator seoEventUrlCreator;

	public int generateUrls(int numberOfEvents) {
		List<EventGenerated> events = getEventsAfterSolrIndexing(
				SubscriberName.Seo, getUrlGeneratorEventTypeList(), null);
		logger.info("Fetched " + events.size() + " events for url generation.");
		Map<DomainObject, List<URLCategories>> groupCategoryMap = urlCategoriesService
				.getAllUrlCategoryByDomainObject();

		int totalUrls = 0;
		DomainObject domainObject = null;
		for (EventGenerated eventGenerated : events) {
			domainObject = DomainObject.getDomainInstance(Long
					.parseLong(eventGenerated.getEventTypeUniqueKey()));
			seoEventUrlCreator.generateUrls(domainObject, eventGenerated,
					groupCategoryMap.get(domainObject));
			totalUrls++;
		}

		return totalUrls;
	}

	protected List<EventGenerated> getEventsAfterSolrIndexing(
			SubscriberName subscriberName, List<String> eventTypeList,
			Pageable pageable) {
		List<EventGenerated> events = responseInterceptor
				.getLatestGeneratedEvents(SubscriberName.Seo,
						getUrlGeneratorEventTypeList(), null);
		
		if( events.isEmpty() ){
			return new ArrayList<EventGenerated>();
		}
		// Getting Event Ids List
		List<Integer> eventIds = new ArrayList<Integer>();
		for (EventGenerated eventGenerated : events) {
			eventIds.add(eventGenerated.getId());
		}
		// Fetching the Solr Indexing Events.
		List<DynamicSolrIndex> dynamicSolrIndexes = responseInterceptor
				.getSolrIndexEvents(eventIds);
		// Constructing the Map of Solr Index Of event Id and Solr Index Events.
		Map<Integer, DynamicSolrIndex> solrMap = new HashMap<Integer, DynamicSolrIndex>();
		for (DynamicSolrIndex dynamicSolrIndex : dynamicSolrIndexes) {
			solrMap.put(dynamicSolrIndex.getEventGeneratedId(),
					dynamicSolrIndex);
		}

		/**
		 * Checking the events generated have been updated in the solr. First
		 * Event is checked if it is send to solr for updating. If not then then
		 * a list of new solr Events. If solr contains those events. If they are
		 * completed, then these events can be used for generating urls.
		 */
		List<EventGenerated> listEventsAfterSolrIndex = new ArrayList<EventGenerated>();
		List<DynamicSolrIndex> createSolrIndexEvents = new ArrayList<DynamicSolrIndex>();
		DynamicSolrIndex dynamicSolrIndex = null, newSolrIndexEvent = null;
		Integer objectId = null;
		for (EventGenerated eventGenerated : events) {
			dynamicSolrIndex = solrMap.get(eventGenerated.getId());
			// creating new solr index events
			if (dynamicSolrIndex == null) {
				objectId = Integer.parseInt(eventGenerated
						.getEventTypeUniqueKey());
				newSolrIndexEvent = new DynamicSolrIndex(
						eventGenerated.getId(), DomainObject.getDomainInstance(
								objectId.longValue()).getObjectTypeId(),
						objectId, DBOperation.INSERT.name());
				createSolrIndexEvents.add(newSolrIndexEvent);
			}
			// Solr Indexing for those events have been completed.
			else if (dynamicSolrIndex.getStatus().equalsIgnoreCase(
					DynamicSolrIndex.Status.Done.name())) {
				listEventsAfterSolrIndex.add(eventGenerated);
			}
		}

		responseInterceptor.postSolrIndexEvents(createSolrIndexEvents);
		return listEventsAfterSolrIndex;
	}

	protected Map<DomainObject, List<EventGenerated>> groupEventsByEventType(
			List<EventGenerated> eventsGenerated) {
		Map<DomainObject, List<EventGenerated>> mapEvents = new HashMap<DomainObject, List<EventGenerated>>();
		List<EventGenerated> groupEvents;// = new ArrayList<EventGenerated>();

		DomainObject domainObject;
		for (EventGenerated eventGenerated : eventsGenerated) {
			domainObject = DomainObject.getDomainInstance(Long
					.parseLong(eventGenerated.getEventTypeUniqueKey()));
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
		/*eventTypeNames.add(EventTypeEnum.BuilderGenerateUrl.getName());
		eventTypeNames.add(EventTypeEnum.ProjectGenerateUrl.getName());
		eventTypeNames.add(EventTypeEnum.CityGenerateUrl.getName());
		eventTypeNames.add(EventTypeEnum.PropertyGenerateUrl.getName());
		eventTypeNames.add(EventTypeEnum.SuburbGenerateUrl.getName());
		eventTypeNames.add(EventTypeEnum.LocalityGenerateUrl.getName());*/
		eventTypeNames.add(EventTypeEnum.ProjectInsertionUrl.getName());
		return eventTypeNames;
	}
}
