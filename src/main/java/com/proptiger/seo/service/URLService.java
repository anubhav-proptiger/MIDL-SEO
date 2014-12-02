/**
 * 
 */
package com.proptiger.seo.service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.proptiger.core.enums.DomainObject;
import com.proptiger.core.exception.ProAPIException;
import com.proptiger.core.exception.ResourceNotAvailableException;
import com.proptiger.core.model.cms.Builder;
import com.proptiger.core.model.cms.City;
import com.proptiger.core.model.cms.Locality;
import com.proptiger.core.model.cms.Project;
import com.proptiger.core.model.cms.Property;
import com.proptiger.core.model.cms.Suburb;
import com.proptiger.core.model.proptiger.Image;
import com.proptiger.core.model.proptiger.PortfolioListing;
import com.proptiger.core.model.user.portfolio.Portfolio;
import com.proptiger.core.util.Constants;
import com.proptiger.core.util.NullAwareBeanUtilsBean;
import com.proptiger.seo.interceptor.ResponseInterceptor;
import com.proptiger.seo.model.PageType;
import com.proptiger.seo.model.RedirectUrlMap;
import com.proptiger.seo.model.URLDetail;
import com.proptiger.seo.repo.RedirectUrlMapDao;

/**
 * 
 *
 */
@Service
public class URLService {
    private String              EMPTY_URL = "";

    /*
     * @Autowired private RedirectUrlMapDao redirectUrlMapDao;
     */

    @Autowired
    private ResponseInterceptor responseInterceptor;

    private static Pattern      PATTERN   = Pattern
                                                  .compile("^(?:.*)-(\\d+)(?:/[\\d+]{0,2}bhk(?:\\?\\d+-\\d+-lacs)?)?$");

    public ValidURLResponse getURLStatus(String url) {
        URLDetail urlDetail = null;

        // Removing trailing slace if any and maitaining a boolean flag
        // to update the HttpStatus for 301.
        boolean hasTrailingSlace = false;
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
            hasTrailingSlace = true;
        }

        /*
         * Removing the URL request params and saving them in variable.
         */
        String[] URLSplit = url.split("\\?");

        String filterString = "/filters";
        String URLRequestParamString = "";
        if (URLSplit.length > 1) {
            if (StringUtils.endsWith(URLSplit[0], filterString)) {
                URLSplit[0] = StringUtils.replace(URLSplit[0], filterString, "");
                URLRequestParamString = filterString + "?" + URLSplit[1];
            }
            else {
                URLRequestParamString = "?" + URLSplit[1];
            }
        }
        url = URLSplit[0];

        try {
            urlDetail = parse(url);
        }
        catch (Exception e) {
            throw new ProAPIException(e);
        }
        return validateUrl(urlDetail, hasTrailingSlace, URLRequestParamString);
    }

    private PageType setNewUrlDetails(Integer objectId, Integer integer, URLDetail newUrlDetail) {
        DomainObject domainObject = DomainObject.getDomainInstance(new Long(objectId));
        if (domainObject.equals(DomainObject.property)) {
            newUrlDetail.setPropertyId(objectId);
            return PageType.PROPERTY_URLS;
        }

        if (domainObject.equals(DomainObject.project)) {
            newUrlDetail.setProjectId(objectId);
            return PageType.PROJECT_URLS;
        }

        if (domainObject.equals(DomainObject.locality) || domainObject.equals(DomainObject.suburb)) {
            newUrlDetail.setLocalityId(objectId);
            newUrlDetail.setOverviewType("overview");
            return PageType.LOCALITY_SUBURB_OVERVIEW;
        }

        if (domainObject.equals(DomainObject.city)) {
            City city = null;
            try {
                city = responseInterceptor.getCityById(objectId);// cityService.getCity(objectId);
            }
            catch (Exception e) {
                city = null;
            }
            if (city == null) {
                return PageType.InvalidUrl;
            }
            newUrlDetail.setCityName(city.getLabel());
            return PageType.CITY_URLS;
        }

        if (domainObject.equals(DomainObject.builder)) {
            newUrlDetail.setBuilderId(objectId);
            return PageType.BUILDER_URLS;
        }
        return PageType.InvalidUrl;
    }

    private Integer getObjectIdFromRedirectUrl(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }

        Matcher matcher = PATTERN.matcher(url);
        if (matcher.find()) {
            return new Integer(matcher.group(1));
        }

        return null;
    }

    private ValidURLResponse validateUrl(URLDetail urlDetail, boolean hasTrailingSlace, String URLRequestParamString) {
        PageType pageType = urlDetail.getPageType();
        int responseStatus = HttpStatus.SC_OK;
        String redirectUrl = null, domainUrl = null;

        if (urlDetail.getBedroomString() == null) {
            urlDetail.setBedroomString("");
        }
        if (urlDetail.getPriceString() == null) {
            urlDetail.setPriceString("");
        }
        if (urlDetail.getCityName() == null) {
            urlDetail.setCityName("");
        }
        if (urlDetail.getPropertyType() == null) {
            urlDetail.setPropertyType("");
        }

        switch (pageType) {
            case HOME_PAGE:
                responseStatus = HttpStatus.SC_OK;
                break;
            case PROPERTY_URLS:
                responseStatus = HttpStatus.SC_MOVED_PERMANENTLY;
                Property property = null;
                try {
                    property = responseInterceptor.getPropertyById(urlDetail.getPropertyId());
                    // propertyService.getPropertyFromSolr(urlDetail.getPropertyId());
                }
                catch (Exception e) {
                    property = null;
                }

                if (property == null) {
                    redirectUrl = getHigherHierarchyUrl(urlDetail.getPropertyId(), DomainObject.property.getText());
                }
                else if (!property.getURL().equals(urlDetail.getUrl()) || hasTrailingSlace) {
                    redirectUrl = property.getURL();
                }
                else {
                    responseStatus = HttpStatus.SC_OK;
                }
                break;
            case PROJECT_URLS:
                responseStatus = HttpStatus.SC_MOVED_PERMANENTLY;
                Project project = null;
                try {
                    project = responseInterceptor.getProjectById(urlDetail.getProjectId());
                    // projectService.getProjectDataFromSolr(urlDetail.getProjectId());
                }
                catch (Exception e) {
                    project = null;
                }

                if (project == null) {
                    redirectUrl = getHigherHierarchyUrl(urlDetail.getProjectId(), DomainObject.project.getText());
                }
                else if (!project.getURL().equals(urlDetail.getUrl()) || hasTrailingSlace) {
                    redirectUrl = project.getURL();
                }
                else {
                    responseStatus = HttpStatus.SC_OK;
                }
                break;
            case BUILDER_URLS:
            case BUILDER_URLS_SEO:
                responseStatus = HttpStatus.SC_MOVED_PERMANENTLY;
                Builder builder = null;
                City builderCity = new City();
                try {
                    builder = responseInterceptor.getBuilderById(urlDetail.getBuilderId());
                    // builderService.getBuilderById(urlDetail.getBuilderId());
                }
                catch (Exception e) {
                    builder = null;
                }
                try {
                    if (urlDetail.getCityName() != null && !urlDetail.getCityName().isEmpty()) {
                        builderCity = responseInterceptor.getCityByName(urlDetail.getCityName().replace("/", ""));
                        // cityService.getCityByName(urlDetail.getCityName().replace("/",
                        // ""));
                    }
                }
                catch (Exception e) {
                    builderCity = null;
                }

                if (builder == null || builderCity == null) {
                    if (builderCity != null) {
                        if (urlDetail.getCityName() != null && !urlDetail.getCityName().isEmpty()) {
                            redirectUrl = builderCity.getUrl();
                        }
                        else {
                            redirectUrl = EMPTY_URL;
                        }
                    }
                    else if (builder != null) {
                        redirectUrl = builder.getUrl();
                    }
                    else {
                        redirectUrl = EMPTY_URL;
                    }
                }
                else {
                    domainUrl = urlDetail.getPropertyType() + builder.getUrl() + urlDetail.getBedroomString();
                    if (builder.getBuilderCities() != null && builder.getBuilderCities().size() > 1) {
                        domainUrl = urlDetail.getCityName() + domainUrl;
                    }
                    if (!domainUrl.equals(urlDetail.getUrl()) || hasTrailingSlace) {
                        redirectUrl = domainUrl;
                    }
                    else {
                        responseStatus = HttpStatus.SC_OK;
                    }
                }
                break;
            case LOCALITY_SUBURB_LISTING:
            case LOCALITY_SUBURB_LISTING_SEO:
                // localitySuburbListingUrl, cityName, response status
                responseStatus = HttpStatus.SC_MOVED_PERMANENTLY;
                Object[] localitySuburbData = getLocalitySuburbListingUrl(urlDetail);

                domainUrl = (String) localitySuburbData[0];
                responseStatus = (Integer) localitySuburbData[2];
                boolean is404FallbackSet = (boolean) localitySuburbData[4];

                if (is404FallbackSet) {
                    redirectUrl = domainUrl;
                }
                else {
                    domainUrl = domainUrl.replaceFirst("property-sale", urlDetail.getPropertyType()) + urlDetail
                            .getBedroomString() + urlDetail.getPriceString();

                    if (!domainUrl.equals(urlDetail.getUrl()) || hasTrailingSlace) {
                        redirectUrl = domainUrl;
                        responseStatus = HttpStatus.SC_MOVED_PERMANENTLY;
                    }
                    else {
                        responseStatus = HttpStatus.SC_OK;
                    }
                }
                break;
            case LOCALITY_SUBURB_OVERVIEW:
                // localitySuburbListingUrl, cityName, response status
                responseStatus = HttpStatus.SC_MOVED_PERMANENTLY;
                Object[] localitySuburbUrlData = getLocalitySuburbListingUrl(urlDetail);

                domainUrl = (String) localitySuburbUrlData[0];
                responseStatus = (Integer) localitySuburbUrlData[2];
                String cityName = (String) localitySuburbUrlData[1];
                is404FallbackSet = (boolean) localitySuburbUrlData[4];

                if (is404FallbackSet) {
                    redirectUrl = domainUrl;
                    if (domainUrl != null && !domainUrl.isEmpty()) {
                        redirectUrl = domainUrl;
                        if (urlDetail.getOverviewType() != null) {
                            redirectUrl = domainUrl;
                        }
                    }
                }
                else {
                    domainUrl = domainUrl.replaceFirst("property-sale-", "");
                    domainUrl = domainUrl.replaceFirst(cityName, cityName + "-real-estate");
                    if (urlDetail.getOverviewType() != null) {
                        domainUrl = domainUrl.replaceFirst(urlDetail.getLocalityId() + "", urlDetail.getOverviewType()) + "-"
                                + urlDetail.getLocalityId();
                    }
                    else {
                        domainUrl = domainUrl + urlDetail.getAppendingString();
                    }

                    if (!domainUrl.equals(urlDetail.getUrl()) || hasTrailingSlace) {
                        redirectUrl = domainUrl;
                        responseStatus = HttpStatus.SC_MOVED_PERMANENTLY;
                    }
                    else {
                        responseStatus = HttpStatus.SC_OK;
                    }
                }
                break;
            case LOCALITY_SUBURB_LANDMARK:
                responseStatus = HttpStatus.SC_MOVED_PERMANENTLY;
                Object[] localitySuburbLandMarkUrlData = getLocalitySuburbListingUrl(urlDetail);

                domainUrl = (String) localitySuburbLandMarkUrlData[0];
                responseStatus = (Integer) localitySuburbLandMarkUrlData[2];
                is404FallbackSet = (boolean) localitySuburbLandMarkUrlData[4];

                if (is404FallbackSet) {
                    redirectUrl = domainUrl;
                    if (domainUrl != null && !domainUrl.isEmpty()) {
                        redirectUrl = domainUrl;
                        if (urlDetail.getOverviewType() != null) {
                            redirectUrl = domainUrl;
                        }
                    }
                }
                else {
                    domainUrl = domainUrl.replaceFirst("property-sale-", "");
                    domainUrl = domainUrl + urlDetail.getAppendingString();
                    if (!domainUrl.equals(urlDetail.getUrl()) || hasTrailingSlace) {
                        redirectUrl = domainUrl;
                        responseStatus = HttpStatus.SC_MOVED_PERMANENTLY;
                    }
                    else {
                        responseStatus = HttpStatus.SC_OK;
                    }
                }
                break;
            case CITY_URLS:
                City city = null;
                try {
                    city = responseInterceptor.getCityByName(urlDetail.getCityName());
                    // cityService.getCityByName(urlDetail.getCityName());
                }
                catch (Exception e) {
                    city = null;
                }
                if (city == null || hasTrailingSlace) {
                    redirectUrl = EMPTY_URL;
                    responseStatus = HttpStatus.SC_MOVED_PERMANENTLY;
                }
                break;
            case IMAGE_PAGE_URL:
                responseStatus = HttpStatus.SC_MOVED_PERMANENTLY;
                URLDetail newUrlDetail = new URLDetail();
                PageType objectIdToPageType = setNewUrlDetails(
                        urlDetail.getObjectId(),
                        urlDetail.getObjectId(),
                        newUrlDetail);
                newUrlDetail.setPageType(objectIdToPageType);
                newUrlDetail.setUrl("");

                ValidURLResponse urlResponse = validateUrl(newUrlDetail, false, "");
                String redirectionUrl = urlResponse.getRedirectUrl();
                Integer objectIdFromRedirectUrl = getObjectIdFromRedirectUrl(redirectionUrl);

                // If object-Id corresponds to city in the page_url, then
                // HttpStatus for that city will
                // be either 200 or 301, if HttpStatus is 200 redirection url
                // will be null else it will
                // be Empty. so in case of 301 HttpStatus city is either
                // inactive or invalid and redirection
                // url be empty and nothing can be extracted from this, but in
                // case of HttpStatus
                // 200 city is active and hence directly assigning cityId to
                // objectIdFromRedirectUrl as
                // redirection url will be null.
                if (objectIdToPageType.equals(PageType.CITY_URLS) && urlResponse.getHttpStatus() == HttpStatus.SC_OK) {
                    objectIdFromRedirectUrl = urlDetail.getObjectId();
                    urlResponse = new ValidURLResponse(HttpStatus.SC_MOVED_PERMANENTLY, "");
                }

                Image image = null;
                try {
                    image = responseInterceptor.getImageById(urlDetail.getImageId());
                }
                catch (Exception e) {
                    // TODO: handle exception
                }

                // imageService.getImage(urlDetail.getImageId());
                /*
                 * HttpStatus is 301, domain object is active and image is
                 * active then, either url will be same as image object page url
                 * or different. if both are same then HttpStatus will be 200
                 * else 301 and redirection url will be image object page url.
                 */
                if (image != null && objectIdFromRedirectUrl != null
                        && urlResponse.getHttpStatus() == HttpStatus.SC_MOVED_PERMANENTLY
                        && image.isActive()
                        && urlDetail.getObjectId().equals(objectIdFromRedirectUrl)) {
                    if (image.getPageUrl().equals(urlDetail.getUrl())) {
                        responseStatus = HttpStatus.SC_OK;
                    }
                    else {
                        redirectUrl = image.getPageUrl();
                    }
                }
                /*
                 * if Domain object id null or not null and not equal to object
                 * id and HttpStatus is 301 and image active/inactive then
                 * redirect url to urlResponse's redirection url
                 */
                else if (image != null && urlResponse.getHttpStatus() == HttpStatus.SC_MOVED_PERMANENTLY) {
                    redirectUrl = urlResponse.getRedirectUrl();
                }
                /*
                 * HttpStatus is 404 or image is null
                 */
                else {
                    responseStatus = HttpStatus.SC_NOT_FOUND;
                }
                break;
            case STATIC_URLS:
            case DIWALI_MELA_URL:
                responseStatus = HttpStatus.SC_OK;
                break;
            case PORTFOLIO_URLS:
                System.out.println("portfolio urls");
                if (urlDetail.getPortfolioId() != null) {
                    Portfolio portfolio = null;
                    try {
                        portfolio = responseInterceptor.getPortfolioById(urlDetail.getPortfolioId());
                    }
                    catch (Exception e) {

                    }
                    // portfolioService.getActivePortfolioOnId(urlDetail.getPortfolioId());
                    if (portfolio == null || portfolio.getListings() == null
                            || portfolio.getListings().isEmpty()
                            || !portfolio.getListings().contains(urlDetail.getPortfolioId())) {
                        responseStatus = HttpStatus.SC_NOT_FOUND;
                    }
                }
                break;
            case NEWS_URLS:
                if (urlDetail.getCityName() != null && !urlDetail.getCityName().isEmpty()) {
                    city = null;
                    try {
                        city = responseInterceptor.getCityByName(urlDetail.getCityName());// cityService.getCityByName(urlDetail.getCityName());
                    }
                    catch (Exception e) {
                        city = null;
                    }
                    if (city == null) {
                        redirectUrl = EMPTY_URL;
                        responseStatus = HttpStatus.SC_MOVED_PERMANENTLY;
                    }
                }
                break;
            default:
                responseStatus = HttpStatus.SC_NOT_FOUND;
                break;
        }
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            redirectUrl += URLRequestParamString;
        }
        return new ValidURLResponse(responseStatus, redirectUrl);
    }

    private String getHigherHierarchyUrl(Integer id, String domainType) {
        Integer projectId = null;
        Integer localityId = null;
        Integer suburbId = null;
        Integer cityId = null;
        if (domainType.equals(DomainObject.property.getText())) {
            projectId = responseInterceptor.getProjectIdByDeletedPropertyId(id);
            // projectService.getProjectIdForPropertyId(id);
            if (projectId == null) {
                return EMPTY_URL;
            }
            domainType = DomainObject.project.getText();
            id = projectId;
        }
        if (domainType.equals(DomainObject.project.getText())) {
            Project project = null;
            if (projectId != null) {
                try {
                    project = responseInterceptor.getProjectById(projectId);
                    // projectService.getProjectData(projectId);
                    return project.getURL();
                }
                catch (ResourceNotAvailableException | NullPointerException e) {
                    project = null;
                }
            }
            project = responseInterceptor.getActiveInactiveProject(id);
            // projectService.getActiveOrInactiveProjectById(id);
            if (project == null) {
                return EMPTY_URL;
            }
            localityId = project.getLocalityId();
            suburbId = project.getLocality().getSuburbId();
            cityId = project.getLocality().getSuburb().getCityId();
            id = localityId;
            domainType = DomainObject.locality.getText();
        }
        if (domainType.equals(DomainObject.locality.getText())) {
            Locality locality = null;
            if (localityId != null) {
                try {
                    locality = responseInterceptor.getLocalityById(localityId);
                    // localityService.getLocality(localityId);
                    return locality.getUrl();
                }
                catch (ResourceNotAvailableException | NullPointerException e) {
                    locality = null;
                }
            }
            else {
                locality = responseInterceptor.getActiveInactiveLocality(id);
                // localityService.getActiveOrInactiveLocalityById(id);
                if (locality == null) {
                    return EMPTY_URL;
                }
                else {
                    suburbId = locality.getSuburbId();
                    cityId = locality.getSuburb().getCityId();
                }
            }
            id = suburbId;
            domainType = DomainObject.city.getText();
        }
        if (domainType.equals(DomainObject.suburb.getText())) {
            Suburb suburb = null;
            if (suburbId != null) {
                try {
                    suburb = responseInterceptor.getSuburbById(suburbId);
                    // suburbService.getSuburb(suburbId);
                    return suburb.getUrl();
                }
                catch (ResourceNotAvailableException | NullPointerException e) {
                    suburb = null;
                }
            }
            else {
                suburb = responseInterceptor.getActiveInactiveSuburb(id);
                // suburbService.getActiveOrInactiveSuburbById(id);
                if (suburb == null) {
                    return EMPTY_URL;
                }
                else {
                    cityId = suburb.getCityId();
                }
            }
            id = cityId;
            domainType = DomainObject.city.getText();
        }
        if (domainType.equals(DomainObject.city.getText())) {
            City city = null;
            if (cityId != null) {
                try {
                    city = responseInterceptor.getCityById(cityId);
                    // cityService.getCity(cityId);
                    return city.getUrl();
                }
                catch (ResourceNotAvailableException | NullPointerException e) {
                    city = null;
                }
            }
        }
        return EMPTY_URL;
    }

    private Object[] getLocalitySuburbListingUrl(URLDetail urlDetail) {
        DomainObject domainObject = DomainObject.getDomainInstance(urlDetail.getLocalityId().longValue());
        String newUrl = "", cityName = "", domainName = "";
        int responseStatus = HttpStatus.SC_OK;
        boolean is404FallbackSet = false;
        switch (domainObject) {
            case locality:
                Locality locality = null;
                try {
                    locality = responseInterceptor.getLocalityById(urlDetail.getLocalityId());
                    // localityService.getLocality(urlDetail.getLocalityId());
                }
                catch (ResourceNotAvailableException e) {
                    locality = null;
                }

                if (locality == null) {
                    newUrl = getHigherHierarchyUrl(urlDetail.getLocalityId(), DomainObject.locality.getText());
                    responseStatus = HttpStatus.SC_MOVED_PERMANENTLY;
                    is404FallbackSet = true;
                }
                else {
                    newUrl = locality.getUrl();
                    cityName = locality.getSuburb().getCity().getLabel();
                    domainName = locality.getLabel();
                }
                break;
            case suburb:
                Suburb suburb = null;
                try {
                    suburb = responseInterceptor.getSuburbById(urlDetail.getLocalityId());
                    // suburbService.getSuburbById(urlDetail.getLocalityId());
                }
                catch (ResourceNotAvailableException e) {
                    suburb = null;
                }
                if (suburb == null) {
                    newUrl = getHigherHierarchyUrl(urlDetail.getLocalityId(), DomainObject.suburb.getText());
                    responseStatus = HttpStatus.SC_MOVED_PERMANENTLY;
                    is404FallbackSet = true;
                }
                else {
                    newUrl = suburb.getUrl();
                    cityName = suburb.getCity().getLabel();
                    domainName = suburb.getLabel();
                }
                break;
            default:
                responseStatus = HttpStatus.SC_NOT_FOUND;
        }

        return new Object[] {
                newUrl,
                cityName.toLowerCase(),
                responseStatus,
                domainName.toLowerCase(),
                is404FallbackSet };
    }

    /*
     * This function(parse) parses the url to determine the pageType and sets
     * the required fields in urlDetail
     */
    public URLDetail parse(String URL) throws IllegalAccessException, InvocationTargetException {
        URLDetail urlDetail = new URLDetail();
        List<String> groups = new ArrayList<String>();
        BeanUtilsBean beanUtilsBean = new NullAwareBeanUtilsBean();

        for (PageType pageType : PageType.values()) {
            Pattern pattern = Pattern.compile(pageType.getRegex());
            Matcher matcher = pattern.matcher(URL);
            if (matcher.matches()) {
                int c = matcher.groupCount();
                for (int j = 0; j < c; j++) {
                    groups.add(matcher.group(j + 1));
                }

                urlDetail.setPageType(pageType);
                int i = 0;

                for (String field : pageType.getURLDetailFields()) {
                    beanUtilsBean.copyProperty(urlDetail, field, groups.get(i++));
                }
                break;
            }
        }

        urlDetail.setUrl(URL);
        return urlDetail;
    }

    /*
     * @Deprecated
     * 
     * @Cacheable(value = Constants.CacheName.REDIRECT_URL_MAP) public
     * RedirectUrlMap getRedirectUrlForOldUrl(String fromUrl) { return
     * redirectUrlMapDao.findOne(fromUrl); }
     */

    public static class ValidURLResponse implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private int               httpStatus;
        private String            redirectUrl;

        public ValidURLResponse(int httpStatus, String redirectUrl) {
            this.httpStatus = httpStatus;
            this.redirectUrl = redirectUrl;
        }

        public int getHttpStatus() {
            return httpStatus;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }
    }
}
