package com.proptiger.seo.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.proptiger.core.enums.ResourceType;
import com.proptiger.core.enums.ResourceTypeAction;
import com.proptiger.core.exception.ProAPIException;
import com.proptiger.core.exception.ResourceNotAvailableException;
import com.proptiger.core.model.cms.Builder;
import com.proptiger.core.model.cms.City;
import com.proptiger.core.model.cms.Locality;
import com.proptiger.core.model.cms.Project;
import com.proptiger.core.model.cms.Property;
import com.proptiger.core.model.cms.Suburb;
import com.proptiger.core.model.proptiger.Image;
import com.proptiger.core.pojo.LimitOffsetPageRequest;
import com.proptiger.core.pojo.Selector;
import com.proptiger.core.util.Constants;
import com.proptiger.core.util.ExclusionAwareBeanUtilsBean;
import com.proptiger.seo.interceptor.ResponseInterceptor;
import com.proptiger.seo.model.ProjectSeoTags;
import com.proptiger.seo.model.SeoFooter;
import com.proptiger.seo.model.SeoPage;
import com.proptiger.seo.model.SeoPage.Tokens;
import com.proptiger.seo.model.URLDetail;
import com.proptiger.seo.repo.ProjectSeoTagsDao;
import com.proptiger.seo.repo.SeoFooterDao;
import com.proptiger.seo.repo.SeoPageDao;

@Service
public class SeoPageService {

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private SeoFooterDao       seoFooterDao;

    @Autowired
    private SeoPageDao         seoPageDao;

    @Autowired
    private URLService         urlService;

    private RestTemplate       restTemplate = new RestTemplate();

    @Value("${proptiger.url}")
    private String             websiteHost;
    
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ProjectSeoTagsDao  projectSeoTagsDao;
    
    @Autowired
    private ResponseInterceptor responseInterceptor;

    private static Logger      logger       = LoggerFactory.getLogger(SeoPageService.class);

    private Pattern            pattern      = Pattern.compile("(<.+?>)");

    public Map<String, Object> getSeoContentForPage(URLDetail urlDetail) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        SeoPage seoPage = getSeoPage(urlDetail);
        Map<String, Object> seoResponse = new HashMap<String, Object>();
        seoResponse.put("meta", seoPage);
        String url = getFooterUrl(urlDetail);
        // RTRIM the urls with extra slashes.
        url = url.replaceAll("[/]*$", "");
        seoResponse.put("footer", applicationContext.getBean(SeoPageService.class).getSeoFooterUrlsByPage(url)
                .getFooterUrls());
        return seoResponse;
    }

    /*
     * First ProjectSeoTags is retrieved from DB (PROJECT_SEO_TAGS Table) for an
     * URL, and all fields except null or empty, are copied to the seoPage for a 
     * templateId. If there exist any tags in the seoPage it get replaced with
     * the appropriate entry and finally returned.
     */
    private SeoPage getSeoPage(URLDetail urlDetail) {
    	String url = urlDetail.getUrl();
    	if (url !=null && url.startsWith("gallery/")) {
    		updateUrlDetailWithImageType(urlDetail, url);
    	}
        SeoPage seoPage = applicationContext.getBean(SeoPageService.class).getSeoPageByTemplateId(
                urlDetail.getTemplateId(),
                url);
        ProjectSeoTags projectSeoTags = applicationContext.getBean(SeoPageService.class).getProjectSeoTags(url);
        if (projectSeoTags != null) {
            copyProperties(projectSeoTags, seoPage);
        }
        return getSeoMetaContentForPage(urlDetail, seoPage);
    }

    private void updateUrlDetailWithImageType(URLDetail urlDetail, String url) {
    	Long imageId = Long.parseLong(url.substring(url.lastIndexOf("-") + 1));
    	Image image = responseInterceptor.getImageById(imageId);
    	if (image != null) {
    		String imageType = image.getImageTypeObj().getType(); 
    		// Constructing template Id dynamically by appending "_ImageType" (in upper case)
    		urlDetail.setTemplateId(urlDetail.getTemplateId() + "_" + imageType.toUpperCase()); 
    		
    		//Introducing space before Uppercase character of ImageType 
    		imageType = addSpaceBeforeUpperCaseCharacter(imageType, 0, imageType.length());
    				
    		// First character from ImageType to convert in upper case
    	    String startChar = imageType.substring(0, 1);
    	    imageType = imageType.replaceFirst(startChar, startChar.toUpperCase());
    		urlDetail.setImageType(imageType);
    	}
	}

    private String addSpaceBeforeUpperCaseCharacter(String imageType, int index, int length) {
		if (index < length) {
			if (Character.isUpperCase(imageType.charAt(index))) {
				imageType = imageType.replace(imageType.substring(index, index + 1), " " + imageType.charAt(index));
				return addSpaceBeforeUpperCaseCharacter(imageType, index + 2, length);
			}
			return addSpaceBeforeUpperCaseCharacter(imageType, index + 1, length);
		}
		return imageType;
	}

	private void copyProperties(ProjectSeoTags projectSeoTags, SeoPage seoPage) {
        BeanUtilsBean beanUtilsBean = new ExclusionAwareBeanUtilsBean();
        for (Field field : projectSeoTags.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object fieldVal = field.get(projectSeoTags);
                if (fieldVal != null && !fieldVal.equals("")) {
                    beanUtilsBean.copyProperty(seoPage, field.getName(), fieldVal);
                }
            }
            catch (IllegalAccessException | InvocationTargetException e) {
            }
        }
    }

    @Cacheable(value = Constants.CacheName.SEO_TEMPLATE)
    public ProjectSeoTags getProjectSeoTags(String url) {
        List<ProjectSeoTags> projectSeoTags = projectSeoTagsDao.findByUrlOrderByIdDesc(url, new LimitOffsetPageRequest(
                0,
                1));
        if (projectSeoTags != null && !projectSeoTags.isEmpty()) {
            return projectSeoTags.get(0);
        }
        return null;
    }

    public SeoPage getSeoMetaContentForPage(URLDetail urlDetail, SeoPage seoPage) {
        CompositeSeoTokenData compositeSeoTokenData = buildTokensValuesObject(urlDetail);
        Map<String, String> mappings = null;
        try {
            mappings = buildTokensMap(compositeSeoTokenData);
        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
                | InstantiationException e) {
            // TODO Auto-generated catch block
            throw new ProAPIException(e);
        }
        setSeoTemplate(seoPage, mappings, urlDetail);
        return seoPage;
    }

    @Cacheable(value = Constants.CacheName.SEO_FOOTER)
    public SeoFooter getSeoFooterUrlsByPage(String url) {
        SeoFooter seoFooter = seoFooterDao.findOne(url);
        if (seoFooter == null) {
            seoFooter = new SeoFooter();
        }
        return seoFooter;
    }

    @Cacheable(value = Constants.CacheName.SEO_TEMPLATE)
    public SeoPage getSeoPageByTemplateId(String templateId, String url) {
        SeoPage seoPage = seoPageDao.findOne(templateId);
        if (seoPage == null) {
            logger.error(" SEO CONTENT NOT FOUND For templateId, url : " + templateId + "," + url);
            seoPage = new SeoPage();
        }
        return seoPage;
    }

    private String getFooterUrl(URLDetail urlDetail) {
        if (urlDetail.getFallBackUrl() != null)
            return urlDetail.getFallBackUrl();
        else
            return urlDetail.getUrl();
    }

    private void setSeoTemplate(SeoPage seopage, Map<String, String> mappings, URLDetail urlDetail) {
        seopage.setTitle(replace(seopage.getTitle(), mappings, urlDetail).trim());
        seopage.setDescription(replace(seopage.getDescription(), mappings, urlDetail).trim());
        seopage.setKeywords(replace(seopage.getKeywords(), mappings, urlDetail).trim());
        seopage.setH1(replace(seopage.getH1(), mappings, urlDetail).trim());
        seopage.setH2(replace(seopage.getH2(), mappings, urlDetail).trim());
        seopage.setH3(replace(seopage.getH3(), mappings, urlDetail).trim());
        seopage.setH4(replace(seopage.getH4(), mappings, urlDetail).trim());
        if (seopage.getOtherParams() != null) {
            for(String key : seopage.getOtherParams().keySet()) {
                seopage.getOtherParams().put(key, replace(seopage.getOtherParams().get(key), mappings, urlDetail).trim());
            }
        }
    }

    private Map<String, String> buildTokensMap(CompositeSeoTokenData compositeSeoTokenData)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException,
            InstantiationException {
        Map<String, String> mappingTokenValues = new HashMap<String, String>();
        Tokens tokens[] = Tokens.values();

        Class<?> classObject = compositeSeoTokenData.getClass();
        Object nestedObject = null;
        Field field = null;
        Object valueObject = null;
        for (int i = 0; i < tokens.length; i++) {
            nestedObject = compositeSeoTokenData;
            if (tokens[i].getFieldName1() != null) {

                field = classObject.getDeclaredField(tokens[i].getFieldName1());
                field.setAccessible(true);
                nestedObject = field.get(compositeSeoTokenData);

            }
            if (nestedObject == null) {
                continue;
            }

            field = nestedObject.getClass().getDeclaredField(tokens[i].getFieldName2());
            field.setAccessible(true);
            valueObject = field.get(nestedObject);
            if (valueObject == null) {
                continue;
            }
            mappingTokenValues.put(
                    tokens[i].getValue(),
                    (String) String.format(tokens[i].getReplaceString(), valueObject));
        }
        return mappingTokenValues;
    }

    private CompositeSeoTokenData buildTokensValuesObject(URLDetail urlDetail) {
        Property property = null;
        Project project = null;
        Locality locality = null;
        Suburb suburb = null;
        City city = null;
        Builder builder = null;
        String bedroomStr = null;
        String priceRangeStr = null;
        Integer bathrooms = null;
        Integer minBudget = urlDetail.getMinBudget();
        Integer maxBudget = urlDetail.getMaxBudget();
        Integer size = null;
        Double centerLatitude = null;
        Double centerLongitude = null;
        Double latitude = null;
        Double longitude = null;
        String serverName = null;
        String url = null;
        String imageURL = null;
        Gson gson = new Gson();
        Integer page = null;
        String  imageType = null;

        if (urlDetail.getPropertyId() != null) {
            property = responseInterceptor.getPropertyById(urlDetail.getPropertyId());
            //propertyService.getPropertyFromSolr(urlDetail.getPropertyId());
            if (property == null) {
                throw new ResourceNotAvailableException(ResourceType.PROPERTY, ResourceTypeAction.GET);
            }
            project = property.getProject();
            locality = project.getLocality();
            suburb = locality.getSuburb();
            city = suburb.getCity();
            builder = project.getBuilder();
            if (property.getBathrooms() > 0) {
                bathrooms = property.getBathrooms();
            }
            if (property.getSize() != null) {
                size = property.getSize().intValue();
            }
            if (property.getBedrooms() > 0) {
                bedroomStr = property.getBedrooms() + "";
            }
        }
        if (urlDetail.getProjectId() != null) {
            String json = "{\"fields\":[\"distinctBedrooms\"]}";
            Selector selector = gson.fromJson(json, Selector.class);
            project = responseInterceptor.getProjectById(urlDetail.getProjectId(), selector);
            //projectService.getProjectInfoDetailsFromSolr(selector, urlDetail.getProjectId());
            if (project == null) {
                throw new ResourceNotAvailableException(ResourceType.PROJECT, ResourceTypeAction.GET);
            }
            locality = project.getLocality();
            suburb = locality.getSuburb();
            city = suburb.getCity();
            builder = project.getBuilder();
            Set<Integer> bedrooms = project.getDistinctBedrooms();
            bedrooms.remove(0);
            if (bedrooms.size() > 0) {
                bedroomStr = project.getDistinctBedrooms().toString().replaceAll("[\\[\\]]", "");
            }
            latitude = project.getLatitude();
            longitude = project.getLongitude();
            imageURL = project.getImageURL();
        }
        if (urlDetail.getLocalityId() != null) {
            locality = responseInterceptor.getLocalityById(urlDetail.getLocalityId());
            //localityService.getLocality(urlDetail.getLocalityId());
            if (locality == null) {
                throw new ResourceNotAvailableException(ResourceType.LOCALITY, ResourceTypeAction.GET);
            }
            suburb = locality.getSuburb();
            city = suburb.getCity();
        }
        if (urlDetail.getSuburbId() != null) {
            suburb = responseInterceptor.getSuburbById(urlDetail.getSuburbId());
            //suburbService.getSuburbById(urlDetail.getSuburbId());
            if (suburb == null) {
                throw new ResourceNotAvailableException(ResourceType.SUBURB, ResourceTypeAction.GET);
            }
            city = suburb.getCity();
        }
        if (urlDetail.getCityName() != null || urlDetail.getCityId() != null) {
            
            if(urlDetail.getCityName() != null){
                city = responseInterceptor.getCityByName(urlDetail.getCityName());//cityService.getCityByName(urlDetail.getCityName());
            }
            else{
                city = responseInterceptor.getCityById(urlDetail.getCityId());//cityService.getCityByName(urlDetail.getCityName());
            }
            
            if (city == null) {
                throw new ResourceNotAvailableException(ResourceType.CITY, ResourceTypeAction.GET);
            }
            centerLatitude = city.getCenterLatitude();
            centerLongitude = city.getCenterLongitude();
        }
        if (urlDetail.getBuilderId() != null) {
            builder = responseInterceptor.getBuilderById(urlDetail.getBuilderId());//builderService.getBuilderById(urlDetail.getBuilderId());
            if (builder == null) {
                throw new ResourceNotAvailableException(ResourceType.BUILDER, ResourceTypeAction.GET);
            }
        }
        if (urlDetail.getBedrooms() != null && urlDetail.getBedrooms() > 0) {
            bedroomStr = urlDetail.getBedrooms().toString();
        }
        // Conversion of price in Lacs.
        if (minBudget != null) {
            minBudget = minBudget / 100000;
            maxBudget = maxBudget / 100000;
            priceRangeStr = minBudget + "-" + maxBudget;
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request.getServerName() != null) {
            serverName = request.getServerName();
        }
        
        if (urlDetail.getUrl() != null) {
            url = urlDetail.getUrl();
        }
        
        if (urlDetail.getPage() != null) {
        	page = urlDetail.getPage();
        }
        
        if (urlDetail.getImageType() != null) {
        	imageType = urlDetail.getImageType();
        }
        
        return new CompositeSeoTokenData(
                property,
                project,
                locality,
                suburb,
                city,
                builder,
                bedroomStr,
                priceRangeStr,
                bathrooms,
                size,
                centerLatitude,
                centerLongitude,
                latitude,
                longitude,
                serverName,
                url,
                imageURL,
                page,
                imageType);
    }

    /*
     * This function(replace()) replaces the tokens in the TEXT with their
     * values using the MAPPING
     */
    private String replace(String text, Map<String, String> mappings, URLDetail urlDetail) {
        if (text == null) {
            return null;
        }

        Matcher matcher = this.pattern.matcher(text);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String replacement = mappings.get(matcher.group(1).toLowerCase());
            if (replacement == null) {
                replacement = "";
                logger.error(matcher.group(1) + " Token Not Found At constructing SEO template with request details: "
                        + urlDetail);
            }
            matcher.appendReplacement(buffer, replacement);
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    public static class CompositeSeoTokenData {
        private Property property;
        private Project  project;
        private Locality locality;
        private Suburb   suburb;
        private City     city;
        private Builder  builder;
        private String   bedroomsStr;
        private String   priceRangeStr;
        private Integer  bathrooms;
        private Integer  size;
        private Double   centerLatitude;
        private Double   centerLongitude;
        private Double   latitude;
        private Double   longitude;
        private String   serverName;
        private String   url;
        private String   imageURL;
        private Integer  page;
        private String   imageType;

        public CompositeSeoTokenData(
                Property property,
                Project project,
                Locality locality,
                Suburb suburb,
                City city,
                Builder builder,
                String bedrooms,
                String priceRange,
                Integer bathrooms,
                Integer size, 
                Double centerLatitude,
                Double centerLongitude,
                Double latitude,
                Double longitude,
                String serverName,
                String url,
                String imageURL,
                Integer page,
                String imageType) {
            this.property = property;
            this.project = project;
            this.locality = locality;
            this.suburb = suburb;
            this.city = city;
            this.builder = builder;
            this.bedroomsStr = bedrooms;
            this.priceRangeStr = priceRange;
            this.bathrooms = bathrooms;
            this.size = size;
            this.centerLatitude = centerLatitude;
            this.centerLongitude = centerLongitude;
            this.latitude = latitude;
            this.longitude = longitude;
            this.serverName = serverName;
            this.url = url;
            this.imageURL = imageURL;
            this.page = page;
            this.imageType = imageType;
        }

        public Property getProperty() {
            return property;
        }

        public void setProperty(Property property) {
            this.property = property;
        }

        public Project getProject() {
            return project;
        }

        public void setProject(Project project) {
            this.project = project;
        }

        public Locality getLocality() {
            return locality;
        }

        public void setLocality(Locality locality) {
            this.locality = locality;
        }

        public Suburb getSuburb() {
            return suburb;
        }

        public void setSuburb(Suburb suburb) {
            this.suburb = suburb;
        }

        public City getCity() {
            return city;
        }

        public void setCity(City city) {
            this.city = city;
        }

        public String getBedroomsStr() {
            return bedroomsStr;
        }

        public void setBedroomsStr(String bedroomsStr) {
            this.bedroomsStr = bedroomsStr;
        }

        public String getPriceRangeStr() {
            return priceRangeStr;
        }

        public void setPriceRangeStr(String priceRangeStr) {
            this.priceRangeStr = priceRangeStr;
        }

        public Builder getBuilder() {
            return builder;
        }

        public void setBuilder(Builder builder) {
            this.builder = builder;
        }

        public Integer getBathrooms() {
            return bathrooms;
        }

        public void setBathrooms(Integer bathrooms) {
            this.bathrooms = bathrooms;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }
        
        public Double getCenterLongitude() {
            return centerLongitude;
        }

        public void setCenterLongitude(Double centerLongitude) {
            this.centerLongitude = centerLongitude;
        }

        public Double getCenterLatitude() {
            return centerLatitude;
        }

        public void setCenterLatitude(Double centerLatitude) {
            this.centerLatitude = centerLatitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public String getServerName() {
            return serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImageURL() {
            return imageURL;
        }

        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

		public Integer getPage() {
			return page;
		}

		public void setPage(Integer page) {
			this.page = page;
		}

		public String getImageType() {
			return imageType;
		}

		public void setImageType(String imageType) {
			this.imageType = imageType;
		}
    }
}
