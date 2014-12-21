package com.proptiger.seo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proptiger.core.init.RequestResponseInterceptor;
import com.proptiger.core.model.cms.Builder;
import com.proptiger.core.model.cms.City;
import com.proptiger.core.model.cms.Locality;
import com.proptiger.core.model.cms.Project;
import com.proptiger.core.model.cms.Property;
import com.proptiger.core.model.cms.Suburb;
import com.proptiger.core.pojo.Selector;
import com.proptiger.core.pojo.response.PaginatedResponse;
//import com.proptiger.data.model.URLDetail;
import com.proptiger.core.util.Caching;
//import com.proptiger.data.util.Serializer;
import com.proptiger.seo.enums.PropertyType;
import com.proptiger.seo.enums.TaxonomyPropertyTypes;
import com.proptiger.seo.enums.URLTypeCategories;
import com.proptiger.seo.interceptor.ResponseInterceptor;
import com.proptiger.seo.model.URLDetail;
import com.proptiger.seo.model.URLPropertyTypeCategory;
import com.proptiger.seo.model.URLPropertyTypes;

@Service
public class URLCreaterService {

    @Autowired
    private Caching         caching;

    @Autowired
    private ResponseInterceptor responseInterceptor;

    private final String    bhkSuffix    = "bhk";

    private final String    budgetSuffix = "lacs";
    private final String    defaultUrl   = "";
    private final String    HYPHEN       = "-";
    private final String    SLASH        = "/";

    public void test(String type, Integer id) {
        switch (type) {
            case "builder":
                getCachedBuilderData(id);
                break;
            case "locality":
                getCachedLocalityData(id, "noida");
                break;
            case "project":
                getCachedProjectData(id);
                break;
            case "property":
                getCachedPropertyData(id);
                break;
            case "suburb":
                getCachedSuburbData(id, "noida");
                break;
        }

    }

    public String getCityUrl(URLDetail urlDetail) {
        String url = this.defaultUrl;

        if (urlDetail.getCityName() != null) {
            String cityName = getCleanName(urlDetail.getCityName());
            URLPropertyTypes urlPropertyTypes = urlDetail.getUrlCategory().getUrlPropertyTypes();

            URLTypeCategories urlTypeCategories = urlPropertyTypes.getUrlPropertyTypeCategory().getUrlTypeCategories();

            switch (urlTypeCategories) {
                case Listing:
                    url = cityName + "-real-estate";
                    break;
                case ListingPropertyType:
                    url = cityName + this.SLASH + urlPropertyTypes.getUrlSubPart() + "-sale";
                    addBhk(url, urlDetail.getBedrooms());
                    addBudget(url, urlDetail.getMinBudget(), urlDetail.getMaxBudget());
                    break;
                case ListingTaxonomy:
                    url = cityName + this.SLASH + urlPropertyTypes.getUrlSubPart();
                    addBhk(url, urlDetail.getBedrooms());
                    break;
                case Amenity:
                    url = cityName + this.SLASH + urlPropertyTypes.getUrlSubPart();
                    break;
                case Overview:
                    url = cityName + "-real-estate-overview";
                    break;
                case Sitemap:
                    url = cityName + "-real-estate/" + cityName + "-sitemap.php";
                    break;
                case AllLocality:
                    url = cityName + this.SLASH + "all-localities";
                    break;
                case AllBuilder:
                    url = cityName + this.SLASH + "all-builders";
                    break;
                default:
                    url = cityName + "-real-estate";

            }

        }

        return url;
    }

    public String getLocalityUrl(URLDetail urlDetail) {
        String url = this.defaultUrl;
        Integer localityId = urlDetail.getLocalityId();
        String localityName = urlDetail.getLocalityName();

        Locality locality = getCachedLocalityData(localityId, localityName);

        if (locality == null) {
            return url;
        }
        localityName = getCleanName(locality.getLabel());
        String cityName = getCleanName(locality.getSuburb().getCity().getLabel());
        URLPropertyTypes urlPropertyTypes = urlDetail.getUrlCategory().getUrlPropertyTypes();

        URLTypeCategories urlTypeCategories = urlPropertyTypes.getUrlPropertyTypeCategory().getUrlTypeCategories();

        switch (urlTypeCategories) {
            case ListingPropertyType:
                url = cityName + this.SLASH
                        + urlPropertyTypes.getUrlSubPart()
                        + this.HYPHEN
                        + localityName
                        + localityId;
                addBhk(url, urlDetail.getBedrooms());
                addBudget(url, urlDetail.getMinBudget(), urlDetail.getMaxBudget());
                break;
            case ListingTaxonomy:
                url = cityName + this.SLASH + urlPropertyTypes.getUrlSubPart() + "-in-" + localityName + localityId;
                break;
            case Overview:
                url = cityName + this.SLASH + localityName + "-overview-" + localityId;
                break;
            case Amenity:
                url = cityName + this.SLASH + localityName + localityId + this.SLASH + urlPropertyTypes.getUrlSubPart();
                break;
            default:
                url = cityName + this.SLASH + localityName + "-overview-" + localityId;
        }

        return url;
    }

    public String getSuburbUrl(URLDetail urlDetail) {
        String url = this.defaultUrl;
        Integer suburbId = urlDetail.getSuburbId();
        String suburbName = urlDetail.getSuburbName();

        Suburb suburb = getCachedSuburbData(suburbId, suburbName);

        if (suburb == null) {
            return url;
        }
        suburbName = getCleanName(suburb.getLabel());
        String cityName = getCleanName(suburb.getCity().getLabel());
        URLPropertyTypes urlPropertyTypes = urlDetail.getUrlCategory().getUrlPropertyTypes();

        URLTypeCategories urlTypeCategories = urlPropertyTypes.getUrlPropertyTypeCategory().getUrlTypeCategories();

        switch (urlTypeCategories) {
            case ListingPropertyType:
                url = cityName + this.SLASH + urlPropertyTypes.getUrlSubPart() + this.HYPHEN + suburbName + suburbId;
                addBhk(url, urlDetail.getBedrooms());
                addBudget(url, urlDetail.getMinBudget(), urlDetail.getMaxBudget());
                break;
            case ListingTaxonomy:
                url = cityName + this.SLASH + urlPropertyTypes.getUrlSubPart() + "-in-" + suburbName + suburbId;
                break;
            case Overview:
                url = cityName + this.SLASH + suburbName + "-overview-" + suburbId;
                break;
            case Amenity:
                url = cityName + this.SLASH + suburbName + suburbId + this.SLASH + urlPropertyTypes.getUrlSubPart();
                break;
            default:
                url = cityName + this.SLASH + suburbName + "-overview-" + suburbId;
        }

        return url;
    }

    public String getBuilderUrl(URLDetail urlDetail) {
        String url = this.defaultUrl;
        Builder builder = getCachedBuilderData(urlDetail.getBuilderId());

        if (builder == null) {
            return url;
        }

        String builderName = getCleanName(builder.getName());
        String cityName = getCleanName(urlDetail.getCityName());
        URLPropertyTypes urlPropertyTypes = urlDetail.getUrlCategory().getUrlPropertyTypes();
        URLTypeCategories urlTypeCategories = urlPropertyTypes.getUrlPropertyTypeCategory().getUrlTypeCategories();

        url = builderName + this.HYPHEN + builder.getId();
        switch (urlTypeCategories) {
            case Overview:
                if (cityName != null) {
                    url = cityName + this.SLASH + url;
                }
                break;
            case BuilderTaxonomy:
                url = urlPropertyTypes.getUrlSubPart() + "-by-" + url;
                break;
        }
        
        return url;
    }

    public String urlLibCityUrl(URLDetail urlDetail) {
        String url = this.defaultUrl;

        if (urlDetail.getCityName() != null) {
            String cityName = getCleanName(urlDetail.getCityName());
            String areaType = urlDetail.getAreaType();
            if (isEmpty(areaType)) {
                areaType = "overview";
            }

            if (areaType == "overview") {
                url = cityName + "-real-estate-overview";
            }
            else {
                url = cityName + this.SLASH + areaType;
            }

        }

        return url;
    }

    public String urlLibCountryListUrl(URLDetail urlDetail) {
        String url = this.defaultUrl;
        PropertyType propertyType = urlDetail.getUrlPropertyType();
        TaxonomyPropertyTypes taxonomyPropertyType = urlDetail.getTaxonomyPropertyType();

        if (taxonomyPropertyType != null) {
            return taxonomyPropertyType + "in-india";
        }
        else if (propertyType == null) {
            propertyType = PropertyType.Apartment;
        }

        url += "-sale";
        addBhk(url, urlDetail.getBedrooms());
        addBudget(url, urlDetail.getMinBudget(), urlDetail.getMaxBudget());

        return url;
    }

    public String urlLibCityListingUrl(URLDetail urlDetail) {
        String url = this.defaultUrl;
        String cityName = getCleanName(urlDetail.getCityName());
        PropertyType propertyType = urlDetail.getUrlPropertyType();
        TaxonomyPropertyTypes taxonomyPropertyType = urlDetail.getTaxonomyPropertyType();

        if (isEmpty(cityName)) {
            if (propertyType != null) {
                url = cityName + this.SLASH + propertyType.getUrlAlias() + "-sale";
                addBhk(url, urlDetail.getBedrooms());
                addBudget(url, urlDetail.getMinBudget(), urlDetail.getMaxBudget());
            }
            else if (taxonomyPropertyType != null) {
                url = cityName + this.SLASH + taxonomyPropertyType.getUrlAlias();
                addBhk(url, urlDetail.getBedrooms());
            }
            else {
                url = cityName + "-real-estate";
            }

        }

        return url;
    }

    public String urlLibCitySitemapUrl(URLDetail urlDetail) {
        String url = this.defaultUrl;
        String cityName = getCleanName(urlDetail.getCityName());

        if (isEmpty(cityName)) {
            url = cityName + "-real-estate/" + cityName + "-sitemap.php";
        }
        return url;
    }

    public String urlLibAllLocalityUrl(URLDetail urlDetail) {
        String url = this.defaultUrl;
        String cityName = getCleanName(urlDetail.getCityName());

        if (isEmpty(cityName)) {
            url = cityName + this.SLASH + "all-localities";
        }

        return url;
    }

    public String urlLibLocalityUrl(URLDetail urlDetail) {
        String url = this.defaultUrl;
        Integer localityId = urlDetail.getLocalityId();
        String localityName = urlDetail.getLocalityName();

        Locality locality = getCachedLocalityData(localityId, localityName);

        if (locality == null) {
            return url;
        }
        localityName = getCleanName(locality.getLabel());
        String cityName = getCleanName(locality.getSuburb().getCity().getLabel());
        String areaType = getCleanName(urlDetail.getAreaType());

        if (areaType == null) {
            url = cityName + this.SLASH + localityName + localityId;
        }
        else if (areaType.equalsIgnoreCase("overview")) {
            url = cityName + this.SLASH + localityName + "-overview-" + localityId;
        }
        else {
            url = cityName + this.SLASH + localityName + localityId + this.SLASH + areaType;
        }

        return url;
    }

    public String urlLibSuburbUrl(URLDetail urlDetail) {
        String url = this.defaultUrl;
        Integer suburbId = urlDetail.getSuburbId();
        String suburbName = urlDetail.getSuburbName();

        Suburb suburb = getCachedSuburbData(suburbId, suburbName);

        if (suburb == null) {
            return url;
        }
        suburbName = getCleanName(suburb.getLabel());
        String cityName = getCleanName(suburb.getCity().getLabel());
        String areaType = getCleanName(urlDetail.getAreaType());

        if (areaType == null) {
            url = cityName + this.SLASH + suburbName + suburbId;
        }
        else if (areaType.equalsIgnoreCase("overview")) {
            url = cityName + this.SLASH + suburbName + "-overview-" + suburbId;
        }
        else {
            url = cityName + this.SLASH + suburbName + suburbId + this.SLASH + areaType;
        }

        return url;
    }

    public String urlLibLocalityListingUrl(URLDetail urlDetail) {
        String url = this.defaultUrl;

        Integer localityId = urlDetail.getLocalityId();
        String localityName = urlDetail.getLocalityName();

        Locality locality = getCachedLocalityData(localityId, localityName);

        if (locality == null) {
            return url;
        }
        localityName = getCleanName(locality.getLabel());
        String cityName = getCleanName(locality.getSuburb().getCity().getLabel());
        PropertyType propertyType = urlDetail.getUrlPropertyType();
        TaxonomyPropertyTypes taxonomyPropertyType = urlDetail.getTaxonomyPropertyType();

        if (propertyType == null) {
            propertyType = PropertyType.Property;
        }

        if (taxonomyPropertyType != null) {
            url = cityName + this.SLASH + taxonomyPropertyType.getUrlAlias() + "-in-" + localityName + localityId;
        }
        else {
            url = cityName + this.SLASH + propertyType.getUrlAlias() + this.HYPHEN + localityName + localityId;
            addBhk(url, urlDetail.getBedrooms());
            addBudget(url, urlDetail.getMinBudget(), urlDetail.getMaxBudget());
        }

        return url;
    }

    public String urlLibSuburbListingUrl(URLDetail urlDetail) {
        String url = this.defaultUrl;

        Integer suburbId = urlDetail.getSuburbId();
        String suburbName = urlDetail.getSuburbName();

        Suburb suburb = getCachedSuburbData(suburbId, suburbName);

        if (suburb == null) {
            return url;
        }
        suburbName = getCleanName(suburb.getLabel());
        String cityName = getCleanName(suburb.getCity().getLabel());
        PropertyType propertyType = urlDetail.getUrlPropertyType();
        TaxonomyPropertyTypes taxonomyPropertyType = urlDetail.getTaxonomyPropertyType();

        if (propertyType == null) {
            propertyType = PropertyType.Property;
        }

        if (taxonomyPropertyType != null) {
            url = cityName + this.SLASH + taxonomyPropertyType.getUrlAlias() + "-in-" + suburbName + suburbId;
        }
        else {
            url = cityName + this.SLASH + propertyType.getUrlAlias() + this.HYPHEN + suburbName + suburbId;
            addBhk(url, urlDetail.getBedrooms());
            addBudget(url, urlDetail.getMinBudget(), urlDetail.getMaxBudget());
        }

        return url;
    }

    public String urlLibAllBuilderUrl(URLDetail urlDetail) {
        String url = this.defaultUrl;
        String cityName = getCleanName(urlDetail.getCityName());

        if (isEmpty(cityName)) {
            url = cityName + this.SLASH + "all-builders";
        }

        return url;
    }

    public String urlLibBuilderUrl(URLDetail urlDetail) {
        String url = this.defaultUrl;
        Builder builder = getCachedBuilderData(urlDetail.getBuilderId());

        if (builder != null) {
            String builderName = getCleanName(builder.getName());
            String cityName = getCleanName(urlDetail.getCityName());
            url = builderName + this.HYPHEN + builder.getId();

            if (urlDetail.getBuilderPropertyType() != null) {
                url = urlDetail.getBuilderPropertyType() + "-by-" + url;
            }
            else if (cityName != null) {
                url = cityName + this.SLASH + url;
            }
        }

        return url;
    }

    public String urlLibProjectUrl(URLDetail urlDetail) {
        String url = this.defaultUrl;
        Project project = getCachedProjectData(urlDetail.getProjectId());

        if (project != null) {
            String projectName = getCleanName(project.getName());
            String localityName = getCleanName(project.getLocality().getLabel());
            String cityName = getCleanName(project.getLocality().getSuburb().getCity().getLabel());
            String builderName = getCleanName(project.getBuilder().getName());

            url = cityName + this.SLASH
                    + localityName
                    + this.SLASH
                    + builderName
                    + this.HYPHEN
                    + projectName
                    + project.getProjectId();
        }

        return url;
    }

    public String urlLibPropertyUrl(URLDetail urlDetail) {
        String url = this.defaultUrl;
        Property property = getCachedPropertyData(urlDetail.getPropertyId());

        if (property != null) {
            String projectName = getCleanName(property.getProject().getName());
            String localityName = getCleanName(property.getProject().getLocality().getLabel());
            String cityName = getCleanName(property.getProject().getLocality().getSuburb().getCity().getLabel());
            String builderName = getCleanName(property.getProject().getBuilder().getName());
            String bedrooms = property.getBedrooms() < 1 ? "" : (property.getBedrooms() + "");

            url = cityName + this.SLASH
                    + builderName
                    + this.HYPHEN
                    + projectName
                    + this.HYPHEN
                    + localityName
                    + property.getPropertyId()
                    + this.SLASH
                    + bedrooms
                    + "bhk";
        }

        return url;
    }

    private String addBhk(String url, Integer bedrooms) {
        if (bedrooms != null && bedrooms > 0)
            return url + this.SLASH + bedrooms + this.bhkSuffix;

        return url;
    }

    private String addBudget(String url, Integer minBudget, Integer maxBudget) {
        if (minBudget != null && minBudget > 0 && maxBudget != null && maxBudget > 0)
            return url + this.SLASH + minBudget + this.budgetSuffix + this.HYPHEN + maxBudget + this.budgetSuffix;

        return url;
    }

    private Property getCachedPropertyData(Integer id) {
        if (id == null) {
            return null;
        }
        String propertyKeyIdPrefix = "parse_url_property_data_id";

        String cacheKey = propertyKeyIdPrefix + id;

        Property currentProperty = null;
        currentProperty = caching.getNonNullCachedResponse(cacheKey, currentProperty);

        if (currentProperty != null) {
            return currentProperty;
        }
        
        currentProperty = responseInterceptor.getPropertyById(id);
        //propertyService.getPropertyFromSolr(id);
        caching.saveResponse(cacheKey, currentProperty);
        
        return currentProperty;
    }

    private Project getCachedProjectData(Integer id) {
        if (id == null) {
            return null;
        }
        String projectKeyIdPrefix = "parse_url_project_data_id";

        String cacheKey = projectKeyIdPrefix + id;

        Project currentProject = null;
        currentProject = caching.getNonNullCachedResponse(cacheKey, currentProject);

        if (currentProject != null) {
            return currentProject;
        }
        
        currentProject = responseInterceptor.getProjectById(id);
        //projectService.getProjectDataFromSolr(id);
        caching.saveResponse(cacheKey, currentProject);
        
        return currentProject;
    }

    public Builder getCachedBuilderData(Integer id) {
        if (id == null) {
            return null;
        }
        String builderKeyIdPrefix = "parse_url_builder_data_id";

        String cacheKey = builderKeyIdPrefix + id;

        Builder currentBuilder = null;
        currentBuilder = caching.getNonNullCachedResponse(cacheKey, currentBuilder);

        if (currentBuilder != null) {
            return currentBuilder;
        }

        List<Builder> builders = getAllBuilders();

        for (Builder builder : builders) {
            cacheKey = builderKeyIdPrefix + builder.getId();
            caching.saveResponse(cacheKey, builder);
            if (builder.getId() == id) {
                currentBuilder = builder;
            }
        }
        builders.clear();

        return currentBuilder;
    }

    private Suburb getCachedSuburbData(Integer id, String name) {
        String suburbKeyIdPrefix = "parse_url_suburb_data_id";
        String suburbKeyNamePrefix = "parse_url_suburb_data_name";

        String cacheKey = "";
        if (id != null) {
            cacheKey = suburbKeyIdPrefix + "_" + id;
        }
        else if (name != null) {
            cacheKey = suburbKeyNamePrefix + "_" + name;
        }

        Suburb currentSuburb = null;

        if (!cacheKey.isEmpty()) {
            currentSuburb = caching.getNonNullCachedResponse(cacheKey, currentSuburb);

            if (currentSuburb != null) {
                return currentSuburb;
            }
        }

        List<Suburb> suburbs = null;
        if (id != null || name != null) {
            suburbs = getAllSuburbs();
            String suburbKeyId, suburbNameForKey, suburbKeyName;
            for (Suburb suburb : suburbs) {
                suburbKeyId = suburbKeyIdPrefix + "_" + suburb.getId();
                suburbNameForKey = getCleanName(suburb.getLabel()).replace(' ', '-') + this.HYPHEN
                        + getCleanName(suburb.getCity().getLabel()).replace(' ', '-');
                suburbKeyName = suburbKeyNamePrefix + "_" + suburbNameForKey;

                if ((id != null && id == suburb.getId()) || (name != null && name.equalsIgnoreCase(suburbNameForKey))) {
                    currentSuburb = suburb;
                }
                caching.saveResponse(suburbKeyId, suburb);
                caching.saveResponse(suburbKeyName, suburb);
            }
            suburbs.clear();
        }

        return currentSuburb;
    }

    private Locality getCachedLocalityData(Integer id, String name) {
        String localityKeyIdPrefix = "parse_url_locality_data_id";
        String localityKeyNamePrefix = "parse_url_locality_data_name";

        String cacheKey = "";
        if (id != null) {
            cacheKey = localityKeyIdPrefix + "_" + id;
        }
        else if (name != null) {
            cacheKey = localityKeyNamePrefix + "_" + name;
        }

        Locality currentLocality = null;

        if (!cacheKey.isEmpty()) {
            currentLocality = caching.getNonNullCachedResponse(cacheKey, currentLocality);

            if (currentLocality != null) {
                return currentLocality;
            }
        }

        List<Locality> localities = null;
        if (id != null || name != null) {
            localities = getAllLocalities();
            String localityKeyId, localityNameForKey, localityKeyName;
            for (Locality locality : localities) {
                localityKeyId = localityKeyIdPrefix + "_" + locality.getLocalityId();
                localityNameForKey = getCleanName(locality.getLabel()).replace(' ', '-') + this.HYPHEN
                        + getCleanName(locality.getSuburb().getCity().getLabel()).replace(' ', '-');
                localityKeyName = localityKeyNamePrefix + "_" + localityNameForKey;

                if ((id != null && id == locality.getLocalityId()) || (name != null && name
                        .equalsIgnoreCase(localityNameForKey))) {
                    currentLocality = locality;
                }
                caching.saveResponse(localityKeyId, locality);
                caching.saveResponse(localityKeyName, locality);
            }
            localities.clear();
        }

        return currentLocality;
    }

    private boolean isEmpty(String str) {
        return (str == null || str.isEmpty());
    }

    private String getCleanName(String name) {
        if (name == null) {
            return null;
        }
        name = name.replaceAll("[\\(\\).,&_]", "");
        name = name.trim();
        return name;
    }

    private List<Locality> getAllLocalities() {
        String selectorString = "{\"fields\":[\"localityId\", \"label\", \"city\"], \"paging\":{\"start\":0, \"rows\":100000}}";
        List<Locality> localities = responseInterceptor.getAllLocalities(); 
        //localityService.getLocalities(Serializer.fromJson( selectorString, Selector.class));

        return localities;
    }

    private List<Suburb> getAllSuburbs() {
        String selectorString = "{\"fields\":[\"id\", \"label\", \"city\"], \"paging\":{\"start\":0, \"rows\":100000}}";
        List<Suburb> suburbs = responseInterceptor.getAllSuburbs();
        //suburbService.getSuburbs(Serializer.fromJson(selectorString, Selector.class));

        return suburbs;
    }

    private List<City> getAllCities() {
        //String selectorString = "{\"fields\":[\"id\", \"label\"], \"paging\":{\"start\":0, \"rows\":100000}}";
        List<City> cities = responseInterceptor.getAllCities();

        return cities;
    }

    private List<Builder> getAllBuilders() {
        //String selectorString = "{\"fields\":[\"id\", \"label\", \"builderCities\"], \"paging\":{\"start\":0, \"rows\":100000}}";
        List<Builder> builders = responseInterceptor.getAllBuilders(); 
        		//builderService.getBuilders(Serializer.fromJson(selectorString, Selector.class));

        return builders;
    }
    
    /*private List<Project> getAllProjects(int start, int rows) {

        String selectorString = "{\"fields\":[\"projectId\", \"name\", \"locality\", \"city\", \"builderLabel\"], \"paging\":{\"start\":" + start
                + ", \"rows\":"
                + rows
                + "}}";
        PaginatedResponse<List<Project>> projects = projectService.getProjectsFromSolr(Serializer.fromJson(
                selectorString,
                Selector.class));

        return projects.getResults();
    }

    private List<Property> getAllProperties(int start, int rows) {
        String selectorString = "{\"fields\":[\"projectId\", \"name\", \"locality\", \"city\", \"builderLabel\", \"bedrooms\"], \"paging\":{\"start\":" + start
                + ", \"rows\":"
                + rows
                + "}}";
        System.out.println(selectorString);
        List<Property> properties = propertyService.getPropertiesFromSolr(Serializer.fromJson(
                selectorString,
                Selector.class));

        return properties;

    }
*/
}
