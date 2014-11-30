package com.proptiger.seo.constants;

public class URLGenerationConstants {
    public static String       Selector                       = "selector=";
    public static String       SelectorGetProjectById        = "{\"filters\":{\"and\":[{\"equal\":{\"projectId\":%d}}]},\"fields\":[\"label\", \"city\", \"suburb\", \"id\",\"localityId\", \"projectId\", \"name\"]}";
    public static String       SelectorGetLocalityById        = "{\"filters\":{\"and\":[{\"equal\":{\"localityId\":%d}}]},\"fields\":[\"label\", \"city\", \"suburb\", \"id\",\"localityId\"]}";
    public static String       SelectorGetPropertyById        = "filters=propertyId==%d&fields=projectId,localityId,suburbId,cityId,propertyId,label,name,unitName,bedrooms";
    public static String       SelectorGetInactiveProjectById = "filters=projectId==%d&fields=projectId,localityId,suburbId,cityId,label,name";
    public static String       SelectorGetBuilderById         = "{\"filters\":{\"and\":[{\"equal\":{\"id\":%d}}]},\"fields\":[\"id\",\"name\",\"builderCities\"]}";
    public static String       SelectorGetCityByName          = "{\"filters\":{\"and\":[{\"equal\":{\"label\":\"%s\"}}]},\"fields\":[\"id\",\"label\"]}";
    public static String       SelectorGetCityById            = "{\"filters\":{\"and\":[{\"equal\":{\"id\":%d}}]},\"fields\":[\"id\",\"label\"]}";
    public static String       RequestPortfolioById           = "listingStatus=ACTIVE";
    public static CharSequence idURLConstant                  = "{id}";
}