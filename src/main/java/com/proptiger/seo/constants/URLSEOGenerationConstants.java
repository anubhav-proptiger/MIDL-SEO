package com.proptiger.seo.constants;

import org.springframework.stereotype.Component;

@Component
public class URLSEOGenerationConstants {
    public static String Selector                       = "?selector=";
    public static String SelectorGetProjectById         = "{\"filters\":{\"and\":[{\"equal\":{\"projectId\":%d}}]},\"fields\":[\"label\", \"city\", \"suburb\", \"id\",\"localityId\", \"projectId\", \"name\", \"URL\"]}";
    public static String SelectorGetLocalityById        = "{\"filters\":{\"and\":[{\"equal\":{\"localityId\":%d}}]},\"fields\":[\"label\", \"city\", \"suburb\", \"id\",\"localityId\", \"url\"]}";
    public static String SelectorGetSuburbById          = "{\"fields\":[\"label\", \"city\",\"id\",\"url\"]}";
    public static String SelectorGetPropertyById        = "filters=propertyId==%d";//&fields=projectId,localityId,suburbId,cityId,propertyId,label,name,unitName,bedrooms,URL";
    public static String SelectorGetInactiveProjectById = "filters=projectId==%d&fields=projectId,localityId,suburbId,cityId,label,name,URL";
    public static String SelectorGetBuilderById         = "{\"filters\":{\"and\":[{\"equal\":{\"builderId\":%d}}]},\"fields\":[\"id\",\"name\",\"builderCities\",\"url\"]}";
    public static String SelectorGetCityByName          = "{\"filters\":{\"and\":[{\"equal\":{\"label\":\"%s\"}}]},\"fields\":[\"id\",\"label\",\"url\"]}";
    public static String SelectorGetCityById            = "{\"filters\":{\"and\":[{\"equal\":{\"id\":%d}}]},\"fields\":[\"id\",\"label\",\"url\", \"centerLatitude\", \"centerLongitude\"]}";
    public static String RequestPortfolioById           = "listingStatus=ACTIVE";
    public static String idURLConstant                  = "{id}";
}