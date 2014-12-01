package com.proptiger.seo.interceptor;

import java.net.URI;
import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;
import com.proptiger.core.model.cms.Builder;
import com.proptiger.core.model.cms.City;
import com.proptiger.core.model.cms.Locality;
import com.proptiger.core.model.cms.Project;
import com.proptiger.core.model.cms.Property;
import com.proptiger.core.model.cms.Suburb;
import com.proptiger.core.model.proptiger.Image;
import com.proptiger.core.model.proptiger.PortfolioListing;
import com.proptiger.core.pojo.Selector;
import com.proptiger.core.pojo.response.APIResponse;
import com.proptiger.core.util.Constants;
import com.proptiger.core.util.HttpRequestUtil;
import com.proptiger.core.util.PropertyKeys;
import com.proptiger.core.util.PropertyReader;
import com.proptiger.core.util.RequestHolderUtil;
import com.proptiger.seo.constants.URLSEOGenerationConstants;

@Aspect
@Order(1)
@Component
public class ResponseInterceptor {

    @Autowired
    private HttpRequestUtil httpRequestUtil;

    private Gson            gson = new Gson();

    public Locality getLocalityById(int localityId) {
        String buildParams = URLSEOGenerationConstants.Selector + URLSEOGenerationConstants.SelectorGetLocalityById;
        buildParams = String.format(buildParams, localityId);
        URI uri = URI
                .create(UriComponentsBuilder
                        .fromUriString(
                                getAPIUrl(PropertyReader.getRequiredPropertyAsString(PropertyKeys.LOCALITY_API_URL)) + buildParams)
                        .build().encode().toString());
        List<Locality> localities = httpRequestUtil.getInternalApiResultAsTypeListFromCache(uri, Locality.class);
        if(localities == null ){
            return null;
        }
        return localities.get(0);
    }

    public Locality getActiveInactiveLocality(int localityId) {
        String url = PropertyReader.getRequiredPropertyAsString(PropertyKeys.INACTIVE_LOCALITY_API_URL);
        url = url.replace(URLSEOGenerationConstants.idURLConstant, new StringBuffer(localityId));

        URI uri = URI.create(UriComponentsBuilder.fromUriString(getAPIUrl(url)).build().encode().toString());
        Locality locality = httpRequestUtil.getInternalApiResultAsTypeFromCache(uri, Locality.class);

        return locality;
    }

    public Suburb getSuburbById(int suburbId) {
        String url = PropertyReader.getRequiredPropertyAsString(PropertyKeys.SUBURB_API_URL);
        url = url.replace(URLSEOGenerationConstants.idURLConstant, new StringBuffer(suburbId));

        URI uri = URI.create(UriComponentsBuilder.fromUriString(getAPIUrl(url)).build().encode().toString());
        Suburb suburb = httpRequestUtil.getInternalApiResultAsTypeFromCache(uri, Suburb.class);

        return suburb;
    }

    public Suburb getActiveInactiveSuburb(int suburbId) {
        String url = PropertyReader.getRequiredPropertyAsString(PropertyKeys.INACTIVE_SUBURB_API_URL);
        url = url.replace(URLSEOGenerationConstants.idURLConstant, new StringBuffer(suburbId));

        URI uri = URI.create(UriComponentsBuilder.fromUriString(getAPIUrl(url)).build().encode().toString());
        Suburb suburb = httpRequestUtil.getInternalApiResultAsTypeFromCache(uri, Suburb.class);

        return suburb;
    }

    public City getCityByName(String cityName) {
        String buildParams = URLSEOGenerationConstants.Selector + URLSEOGenerationConstants.SelectorGetCityByName;
        buildParams = String.format(buildParams, cityName);
        URI uri = URI.create(UriComponentsBuilder
                .fromUriString(
                        getAPIUrl(PropertyReader.getRequiredPropertyAsString(PropertyKeys.CITY_API_URL)) + buildParams)
                .build().encode().toString());
        System.out.println(uri.toString());
        List<City> cities = httpRequestUtil.getInternalApiResultAsTypeListFromCache(uri, City.class);
        if(cities == null){
            return null;
        }
        return cities.get(0);
    }

    public City getCityById(int cityId) {
        String buildParams = URLSEOGenerationConstants.Selector + URLSEOGenerationConstants.SelectorGetCityById;
        buildParams = String.format(buildParams, cityId);
        URI uri = URI.create(   UriComponentsBuilder
                .fromUriString(
                        getAPIUrl(PropertyReader.getRequiredPropertyAsString(PropertyKeys.CITY_API_URL)) + buildParams)
                .build().encode().toString());
        List<City> cities = httpRequestUtil.getInternalApiResultAsTypeListFromCache(uri, City.class);
        if(cities == null){
            return null;
        }
        return cities.get(0);
    }

    public Project getProjectById(int projectId, Selector selector) {
        Selector savedSelector = gson.fromJson(URLSEOGenerationConstants.SelectorGetProjectById, Selector.class);
        if (selector != null && selector.getFields() != null) {
            if (savedSelector.getFields() == null) {
                savedSelector.setFields(selector.getFields());
            }
            else {
                savedSelector.getFields().addAll(selector.getFields());
            }
        }

        String buildParams = URLSEOGenerationConstants.Selector + gson.toJson(savedSelector);
        buildParams = String.format(buildParams, projectId);
        URI uri = URI
                .create(UriComponentsBuilder
                        .fromUriString(
                                getAPIUrl(PropertyReader.getRequiredPropertyAsString(PropertyKeys.PROJECT_API_URL)) + buildParams)
                        .build().encode().toString());
        List<Project> projects = httpRequestUtil.getInternalApiResultAsTypeListFromCache(uri, Project.class);
        if(projects == null){
            return null;
        }
        return projects.get(0);
    }

    public Project getProjectById(int projectId) {
        String buildParams = URLSEOGenerationConstants.Selector + URLSEOGenerationConstants.SelectorGetProjectById;
        buildParams = String.format(buildParams, projectId);
        URI uri = URI
                .create(UriComponentsBuilder
                        .fromUriString(
                                getAPIUrl(PropertyReader.getRequiredPropertyAsString(PropertyKeys.PROJECT_API_URL)) + buildParams)
                        .build().encode().toString());
        List<Project> projects = httpRequestUtil.getInternalApiResultAsTypeListFromCache(uri, Project.class);
        if(projects == null){
            return null;
        }
        return projects.get(0);
    }

    public Project getActiveInactiveProject(int projectId) {
        String url = PropertyReader.getRequiredPropertyAsString(PropertyKeys.INACTIVE_PROJECT_API_URL);
        url = url.replace(URLSEOGenerationConstants.idURLConstant, new StringBuffer(projectId));

        URI uri = URI.create(UriComponentsBuilder.fromUriString(url).build().encode().toString());
        Project project = httpRequestUtil.getInternalApiResultAsTypeFromCache(uri, Project.class);

        return project;
    }

    public Integer getProjectIdByDeletedPropertyId(int propertyId) {
        String url = PropertyReader.getRequiredPropertyAsString(PropertyKeys.DELETED_PROPERTY_API_URL);
        url = url.replace(URLSEOGenerationConstants.idURLConstant, new StringBuffer(propertyId));

        URI uri = URI.create(UriComponentsBuilder.fromUriString(url).build().encode().toString());
        Integer projectId = httpRequestUtil.getInternalApiResultAsTypeFromCache(uri, Integer.class);

        return projectId;
    }

    public Property getPropertyById(int propertyId) {
        String buildParams = URLSEOGenerationConstants.Selector + URLSEOGenerationConstants.SelectorGetPropertyById;
        buildParams = String.format(buildParams, propertyId);
        URI uri = URI
                .create(UriComponentsBuilder
                        .fromUriString(
                                getAPIUrl(PropertyReader.getRequiredPropertyAsString(PropertyKeys.PROPERTY_API_URL)) + buildParams)
                        .build().encode().toString());
        List<Property> properties = httpRequestUtil.getInternalApiResultAsTypeListFromCache(uri, Property.class);
        if(properties == null){
            return null;
        }
        return properties.get(0);
    }

    public PortfolioListing getPortfolioById(int portfolioId) {
        String buildParams = URLSEOGenerationConstants.RequestPortfolioById;
        buildParams = String.format(buildParams, portfolioId);
        URI uri = URI
                .create(UriComponentsBuilder
                        .fromUriString(
                                getAPIUrl(PropertyReader.getRequiredPropertyAsString(PropertyKeys.PORTFOLIO_API_URL)) + buildParams)
                        .build().encode().toString());
        
        HttpHeaders requestHeaders = new HttpHeaders();
        String jsessionId = RequestHolderUtil.getJsessionIdFromRequestCookie();
        requestHeaders.add("Cookie", Constants.Security.COOKIE_NAME_JSESSIONID + "=" + jsessionId);
        
        PortfolioListing portfolioListing = httpRequestUtil.getInternalApiResultAsTypeFromCache(
                uri,
                requestHeaders,
                PortfolioListing.class);

        return portfolioListing;

    }

    public Image getImageById(long imageId) {
        String url = PropertyReader.getRequiredPropertyAsString(PropertyKeys.IMAGE_API_URL);
        url = url.replace(URLSEOGenerationConstants.idURLConstant, Long.toString(imageId));

        URI uri = URI.create(UriComponentsBuilder.fromUriString(url).build().encode().toString());
        Image image = httpRequestUtil.getInternalApiResultAsTypeFromCache(uri, Image.class);

        return image;
    }

    public Builder getBuilderById(int builderId) {
        String buildParams = URLSEOGenerationConstants.Selector + URLSEOGenerationConstants.SelectorGetBuilderById;
        buildParams = String.format(buildParams, builderId);
        URI uri = URI
                .create(UriComponentsBuilder
                        .fromUriString(
                                getAPIUrl(PropertyReader.getRequiredPropertyAsString(PropertyKeys.BUILDER_API_URL)) + buildParams)
                        .build().encode().toString());
        List<Builder> builders = httpRequestUtil.getInternalApiResultAsTypeListFromCache(uri, Builder.class);
        if(builders == null){
            return null;
        }
        return builders.get(0);
    }

    private Object getApiResponseData(Object retVal) {
        if (retVal == null || !(retVal instanceof APIResponse)) {
            return null;
        }
        APIResponse apiResponse = (APIResponse) retVal;
        if (apiResponse.getError() != null) {
            return null;
        }
        return (apiResponse.getData());
    }

    private String getAPIUrl(String url) {
        return PropertyReader.getRequiredPropertyAsString(PropertyKeys.PROPTIGER_URL) + url;
    }
}
