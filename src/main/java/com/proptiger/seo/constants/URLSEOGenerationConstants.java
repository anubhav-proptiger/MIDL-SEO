package com.proptiger.seo.constants;

import org.springframework.stereotype.Component;

@Component
public class URLSEOGenerationConstants {
	public static String QuestionMark                   = "?";
    public static String Selector                       = QuestionMark+"selector=";
    public static String SelectorGetProjectById         = "{\"filters\":{\"and\":[{\"equal\":{\"projectId\":%d}}]},"
            + "\"fields\":[\"label\", \"city\", \"suburb\", \"locality\", \"builder\", \"id\",\"localityId\", "
            + "\"projectId\", \"name\", \"URL\", \"latitude\", \"longitude\",\"distinctBedrooms\",\"imageURL\","
            + " \"name\",\"centerLatitude\",\"centerLongitude\", \"builderLabel\"]}";
    public static String SelectorGetLocalityById        = "{\"filters\":{\"and\":[{\"equal\":{\"localityId\":%d}}]},"
            + "\"fields\":[\"label\", \"city\", \"suburb\", \"id\",\"localityId\", \"url\",\"centerLatitude\",\"centerLongitude\",\"latitude\",\"longitude\"]}";
    public static String SelectorGetAllLocalities       = "\"fields\":[\"label\", \"city\", \"suburb\", \"id\",\"localityId\", \"url\",\"centerLatitude\","
    		+ "\"centerLongitude\",\"latitude\",\"longitude\"], , \"paging\":{\"start\":0, \"rows\":100000}}";
    public static String SelectorGetSuburbById          = "{\"fields\":[\"label\", \"city\",\"id\",\"url\",\"centerLatitude\",\"centerLongitude\"]}";
    public static String SelectorGetAllSuburb           = "{\"fields\":[\"label\", \"city\",\"id\",\"url\",\"centerLatitude\",\"centerLongitude\"], \"paging\":{\"start\":0, \"rows\":100000}}";
    public static String SelectorGetPropertyById        = "filters=propertyId==%d";                                                                                                                                                                                                                                   // &fields=projectId,localityId,suburbId,cityId,propertyId,label,name,unitName,bedrooms,URL";
    public static String SelectorGetInactiveProjectById = "filters=projectId==%d&fields=projectId,localityId,suburbId,cityId,label,name,URL,latitude,longitude,distinctBedrooms, imageURL,city,suburb,locality,builder,id,centerLatitude,centerLongitude";
    public static String SelectorGetBuilderById         = "?fields=id,name,builderCities,url";
    public static String SelectorGetAllBuilders         = "{\"fields\":[\"id\", \"label\", \"builderCities\",\"url\"], \"paging\":{\"start\":0, \"rows\":100000}}";
    public static String SelectorGetCityByName          = "{\"filters\":{\"and\":[{\"equal\":{\"label\":\"%s\"}}]},\"fields\":[\"id\",\"label\",\"url\", \"centerLatitude\",\"centerLongitude\"]}";
    public static String SelectorGetCityById            = "{\"filters\":{\"and\":[{\"equal\":{\"id\":%d}}]},\"fields\":[\"id\",\"label\",\"url\", \"centerLatitude\", \"centerLongitude\"]}";
    public static String SelectorGetAllCities           = "{\"fields\":[\"id\",\"label\",\"url\", \"centerLatitude\", \"centerLongitude\"], \"paging\":{\"start\":0, \"rows\":100000}}";
    public static String RequestPortfolioById           = "listingStatus=ACTIVE";
    public static String idURLConstant                  = "{id}";
    public static String RequestDynamicSolrIndexParam   = "eventIds";
    public static String GetEventRequestParam           = QuestionMark + "requestParam=%s";
    public static String PostSubscriberEventId[]        = {QuestionMark + "eventId=%d", "{subscriberName}"};
	public static String SelectorGetLandMarkById        = "{\"fields\":[\"id\",\"cityId\",\"name\"]}";
}