package com.proptiger.seo.interceptor;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;
import com.proptiger.core.enums.event.EventTypeEnum;
import com.proptiger.core.model.cms.Builder;
import com.proptiger.core.model.cms.City;
import com.proptiger.core.model.cms.LandMark;
import com.proptiger.core.model.cms.Locality;
import com.proptiger.core.model.cms.Project;
import com.proptiger.core.model.cms.Property;
import com.proptiger.core.model.cms.Suburb;
import com.proptiger.core.model.event.EventGenerated;
import com.proptiger.core.model.event.dto.EventRequestDto;
import com.proptiger.core.model.event.subscriber.Subscriber.SubscriberName;
import com.proptiger.core.model.proptiger.Image;
import com.proptiger.core.model.proptiger.PortfolioListing;
import com.proptiger.core.model.solr.DynamicSolrIndex;
import com.proptiger.core.model.user.portfolio.Portfolio;
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

	private Gson gson = new Gson();

	public Locality getLocalityById(int localityId) {
		String buildParams = URLSEOGenerationConstants.Selector
				+ URLSEOGenerationConstants.SelectorGetLocalityById;
		buildParams = String.format(buildParams, localityId);
		URI uri = URI
				.create(UriComponentsBuilder
						.fromUriString(
								getAPIUrl(PropertyReader
										.getRequiredPropertyAsString(PropertyKeys.LOCALITY_API_URL))
										+ buildParams).build().encode()
						.toString());
		List<Locality> localities = null;
		try {
			localities = httpRequestUtil
					.getInternalApiResultAsTypeListFromCache(uri,
							Locality.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (localities == null || localities.isEmpty()) {
			return null;
		}
		return localities.get(0);
	}

	public List<Locality> getAllLocalities() {
		String buildParams = URLSEOGenerationConstants.Selector
				+ URLSEOGenerationConstants.SelectorGetAllLocalities;
		URI uri = URI
				.create(UriComponentsBuilder
						.fromUriString(
								getAPIUrl(PropertyReader
										.getRequiredPropertyAsString(PropertyKeys.ALL_LOCALITY_API_URL))
										+ buildParams).build().encode()
						.toString());
		List<Locality> localities = null;
		try {
			localities = httpRequestUtil
					.getInternalApiResultAsTypeListFromCache(uri,
							Locality.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (localities == null || localities.isEmpty()) {
			return null;
		}
		return localities;
	}

	public Locality getActiveInactiveLocality(int localityId) {
		String url = PropertyReader
				.getRequiredPropertyAsString(PropertyKeys.INACTIVE_LOCALITY_API_URL);
		url = url.replace(URLSEOGenerationConstants.idURLConstant, localityId
				+ "");

		URI uri = URI.create(UriComponentsBuilder.fromUriString(getAPIUrl(url))
				.build().encode().toString());
		Locality locality = null;
		try {
			locality = httpRequestUtil.getInternalApiResultAsTypeFromCache(uri,
					Locality.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return locality;
	}

	public Suburb getSuburbById(int suburbId) {
		String buildParams = URLSEOGenerationConstants.Selector
				+ URLSEOGenerationConstants.SelectorGetSuburbById;
		String url = PropertyReader
				.getRequiredPropertyAsString(PropertyKeys.SUBURB_API_URL);
		url = url.replace(URLSEOGenerationConstants.idURLConstant, suburbId
				+ "")
				+ buildParams;

		URI uri = URI.create(UriComponentsBuilder.fromUriString(getAPIUrl(url))
				.build().encode().toString());
		Suburb suburb = null;
		try {
			suburb = httpRequestUtil.getInternalApiResultAsTypeFromCache(uri,
					Suburb.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return suburb;
	}

	public List<Suburb> getAllSuburbs() {
		String buildParams = URLSEOGenerationConstants.Selector
				+ URLSEOGenerationConstants.SelectorGetAllSuburb;
		String url = PropertyReader
				.getRequiredPropertyAsString(PropertyKeys.ALL_SUBURB_API_URL)
				+ buildParams;

		URI uri = URI.create(UriComponentsBuilder.fromUriString(getAPIUrl(url))
				.build().encode().toString());
		List<Suburb> suburbs = null;
		try {
			suburbs = httpRequestUtil.getInternalApiResultAsTypeListFromCache(
					uri, Suburb.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return suburbs;
	}

	public Suburb getActiveInactiveSuburb(int suburbId) {
		String url = PropertyReader
				.getRequiredPropertyAsString(PropertyKeys.INACTIVE_SUBURB_API_URL);
		url = url.replace(URLSEOGenerationConstants.idURLConstant, suburbId
				+ "");

		URI uri = URI.create(UriComponentsBuilder.fromUriString(getAPIUrl(url))
				.build().encode().toString());
		Suburb suburb = null;
		try {
			suburb = httpRequestUtil.getInternalApiResultAsTypeFromCache(uri,
					Suburb.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return suburb;
	}

	public City getCityByName(String cityName) {
		String buildParams = URLSEOGenerationConstants.Selector
				+ URLSEOGenerationConstants.SelectorGetCityByName;
		buildParams = String.format(buildParams, cityName);
		URI uri = URI
				.create(UriComponentsBuilder
						.fromUriString(
								getAPIUrl(PropertyReader
										.getRequiredPropertyAsString(PropertyKeys.CITY_API_URL))
										+ buildParams).build().encode()
						.toString());
		System.out.println(uri.toString());
		List<City> cities = null;
		try {
			cities = httpRequestUtil.getInternalApiResultAsTypeListFromCache(
					uri, City.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (cities == null || cities.isEmpty()) {
			return null;
		}
		return cities.get(0);
	}

	public City getCityById(int cityId) {
		String buildParams = URLSEOGenerationConstants.Selector
				+ URLSEOGenerationConstants.SelectorGetCityById;
		buildParams = String.format(buildParams, cityId);
		URI uri = URI
				.create(UriComponentsBuilder
						.fromUriString(
								getAPIUrl(PropertyReader
										.getRequiredPropertyAsString(PropertyKeys.CITY_API_URL))
										+ buildParams).build().encode()
						.toString());
		List<City> cities = null;
		try {
			cities = httpRequestUtil.getInternalApiResultAsTypeListFromCache(
					uri, City.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (cities == null || cities.isEmpty()) {
			return null;
		}
		return cities.get(0);
	}

	public List<City> getAllCities() {
		String buildParams = URLSEOGenerationConstants.Selector
				+ URLSEOGenerationConstants.SelectorGetAllCities;
		URI uri = URI
				.create(UriComponentsBuilder
						.fromUriString(
								getAPIUrl(PropertyReader
										.getRequiredPropertyAsString(PropertyKeys.ALL_CITY_API_URL))
										+ buildParams).build().encode()
						.toString());
		List<City> cities = null;
		try {
			cities = httpRequestUtil.getInternalApiResultAsTypeListFromCache(
					uri, City.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (cities == null || cities.isEmpty()) {
			return null;
		}
		return cities;
	}

	public Project getProjectById(int projectId, Selector selector) {
		Selector savedSelector = gson.fromJson(
				URLSEOGenerationConstants.SelectorGetProjectById,
				Selector.class);
		if (selector != null && selector.getFields() != null) {
			if (savedSelector.getFields() == null) {
				savedSelector.setFields(selector.getFields());
			} else {
				savedSelector.getFields().addAll(selector.getFields());
			}
		}

		String buildParams = URLSEOGenerationConstants.Selector
				+ gson.toJson(savedSelector);
		buildParams = String.format(buildParams, projectId);
		URI uri = URI
				.create(UriComponentsBuilder
						.fromUriString(
								getAPIUrl(PropertyReader
										.getRequiredPropertyAsString(PropertyKeys.PROJECT_API_URL))
										+ buildParams).build().encode()
						.toString());
		List<Project> projects = null;
		try {
			projects = httpRequestUtil.getInternalApiResultAsTypeListFromCache(
					uri, Project.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (projects == null || projects.isEmpty()) {
			return null;
		}
		return projects.get(0);
	}

	public Project getProjectById(int projectId) {
		String buildParams = URLSEOGenerationConstants.Selector
				+ URLSEOGenerationConstants.SelectorGetProjectById;
		buildParams = String.format(buildParams, projectId);
		URI uri = URI
				.create(UriComponentsBuilder
						.fromUriString(
								getAPIUrl(PropertyReader
										.getRequiredPropertyAsString(PropertyKeys.PROJECT_API_URL))
										+ buildParams).build().encode()
						.toString());
		List<Project> projects = null;
		try {
			projects = httpRequestUtil.getInternalApiResultAsTypeListFromCache(
					uri, Project.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (projects == null || projects.isEmpty()) {
			return null;
		}
		return projects.get(0);
	}

	public Project getActiveInactiveProject(int projectId) {
		String url = PropertyReader
				.getRequiredPropertyAsString(PropertyKeys.INACTIVE_PROJECT_API_URL);
		url = url.replace(URLSEOGenerationConstants.idURLConstant, projectId
				+ "");

		URI uri = URI.create(UriComponentsBuilder.fromUriString(getAPIUrl(url))
				.build().encode().toString());
		Project project = null;
		try {
			project = httpRequestUtil.getInternalApiResultAsTypeFromCache(uri,
					Project.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return project;
	}

	public Integer getProjectIdByDeletedPropertyId(int propertyId) {
		String url = PropertyReader
				.getRequiredPropertyAsString(PropertyKeys.DELETED_PROPERTY_API_URL);
		url = url.replace(URLSEOGenerationConstants.idURLConstant, propertyId
				+ "");

		URI uri = URI.create(UriComponentsBuilder.fromUriString(getAPIUrl(url))
				.build().encode().toString());
		Integer projectId = null;
		try {
			projectId = httpRequestUtil.getInternalApiResultAsTypeFromCache(
					uri, Integer.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return projectId;
	}

	public Property getPropertyById(int propertyId) {
		String buildParams = "?"
				+ URLSEOGenerationConstants.SelectorGetPropertyById;
		buildParams = String.format(buildParams, propertyId);
		URI uri = URI
				.create(UriComponentsBuilder
						.fromUriString(
								getAPIUrl(PropertyReader
										.getRequiredPropertyAsString(PropertyKeys.PROPERTY_API_URL))
										+ buildParams).build().encode()
						.toString());
		List<Property> properties = null;
		try {
			properties = httpRequestUtil
					.getInternalApiResultAsTypeListFromCache(uri,
							Property.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (properties == null || properties.isEmpty()) {
			return null;
		}
		return properties.get(0);
	}

	public Portfolio getPortfolioById(int portfolioId) {
		String buildParams = "?"
				+ URLSEOGenerationConstants.RequestPortfolioById;
		buildParams = String.format(buildParams, portfolioId);
		URI uri = URI
				.create(UriComponentsBuilder
						.fromUriString(
								getAPIUrl(PropertyReader
										.getRequiredPropertyAsString(PropertyKeys.PORTFOLIO_API_URL))
										+ buildParams).build().encode()
						.toString());

		HttpHeaders requestHeaders = new HttpHeaders();
		String jsessionId = RequestHolderUtil.getJsessionIdFromRequestCookie();
		requestHeaders.add("Cookie", Constants.Security.COOKIE_NAME_JSESSIONID
				+ "=" + jsessionId);

		Portfolio portfolio = null;
		try {
			portfolio = httpRequestUtil.getInternalApiResultAsTypeFromCache(
					uri, requestHeaders, Portfolio.class);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return portfolio;

	}

	public Image getImageById(long imageId) {
		String url = PropertyReader
				.getRequiredPropertyAsString(PropertyKeys.IMAGE_API_URL);
		url = url.replace(URLSEOGenerationConstants.idURLConstant,
				Long.toString(imageId));

		URI uri = URI.create(UriComponentsBuilder.fromUriString(getAPIUrl(url))
				.build().encode().toString());
		Image image = null;
		try {
			image = httpRequestUtil.getInternalApiResultAsTypeFromCache(uri,
					Image.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return image;
	}

	public Builder getBuilderById(int builderId) {
		String url = PropertyReader
				.getRequiredPropertyAsString(PropertyKeys.BUILDER_API_URL);
		url = url.replace(URLSEOGenerationConstants.idURLConstant,
				Long.toString(builderId))
				+ URLSEOGenerationConstants.SelectorGetBuilderById;
		URI uri = URI.create(UriComponentsBuilder.fromUriString(getAPIUrl(url))
				.build().encode().toString());
		Builder builder = null;
		try {
			builder = httpRequestUtil.getInternalApiResultAsTypeFromCache(uri,
					Builder.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return builder;
	}

	public List<Builder> getAllBuilders() {
		String buildParams = URLSEOGenerationConstants.Selector
				+ URLSEOGenerationConstants.SelectorGetAllBuilders;
		String url = PropertyReader
				.getRequiredPropertyAsString(PropertyKeys.ALL_BUILDER_API_URL)
				+ buildParams;
		URI uri = URI.create(UriComponentsBuilder.fromUriString(getAPIUrl(url))
				.build().encode().toString());
		List<Builder> builders = null;
		try {
			builders = httpRequestUtil.getInternalApiResultAsTypeList(uri,
					Builder.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return builders;
	}

	public List<EventGenerated> getLatestGeneratedEvents(
			SubscriberName subscriberName, List<String> listEventTypeEnums,
			Pageable pageable) {
		String url = PropertyReader
				.getRequiredPropertyAsString(PropertyKeys.EVENTS_API_URL);
		String buildParam = String.format(
				URLSEOGenerationConstants.GetEventRequestParam, gson
						.toJson(constructEventRequest(subscriberName,
								listEventTypeEnums, pageable)));

		URI uri = URI.create(UriComponentsBuilder
				.fromUriString(getAPIUrl(url) + buildParam).build().encode()
				.toString());

		List<EventGenerated> eventGenerateds = new ArrayList<EventGenerated>();
		try {
			eventGenerateds = httpRequestUtil.getInternalApiResultAsTypeList(
					uri, EventGenerated.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return eventGenerateds;
	}

	public List<DynamicSolrIndex> getSolrIndexEvents(List<Integer> eventIds) {
		String url = PropertyReader
				.getRequiredPropertyAsString(PropertyKeys.SOLR_INDEX_API_URL);
		String buildParam = URLSEOGenerationConstants.QuestionMark;
		for (Integer eventId : eventIds) {
			buildParam += URLSEOGenerationConstants.RequestDynamicSolrIndexParam
					+ "=" + eventId + "&";
		}
		buildParam = buildParam.substring(0, buildParam.length() - 1);
		URI uri = URI.create(UriComponentsBuilder
				.fromUriString(getAPIUrl(url) + buildParam).build().encode()
				.toString());

		List<DynamicSolrIndex> dynamicSolrIndexes = new ArrayList<DynamicSolrIndex>();
		try {
			dynamicSolrIndexes = httpRequestUtil
					.getInternalApiResultAsTypeList(uri, DynamicSolrIndex.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dynamicSolrIndexes;
	}

	public boolean postSolrIndexEvents(List<DynamicSolrIndex> dynamicSolrIndexs) {
		String url = PropertyReader
				.getRequiredPropertyAsString(PropertyKeys.SOLR_INDEX_API_URL);
		String postData = gson.toJson(dynamicSolrIndexs);

		URI uri = URI.create(UriComponentsBuilder.fromUriString(getAPIUrl(url))
				.build().encode().toString());

		List<DynamicSolrIndex> responseDynamicSolrIndexes = null;
		try {
			responseDynamicSolrIndexes = httpRequestUtil.postAndReturnInternalJsonRequest(uri,
					dynamicSolrIndexs, DynamicSolrIndex.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean postSubscriberLastEvent(SubscriberName subscriberName,
			Integer lastEventId) {
		String buildParam = String.format(URLSEOGenerationConstants.PostSubscriberEventId[0], lastEventId);
		String url = PropertyReader
				.getRequiredPropertyAsString(PropertyKeys.SUBSCRIBER_LAST_EVENT_UPDATE_API_URL);
		url = url.replace(URLSEOGenerationConstants.PostSubscriberEventId[1], subscriberName.name()) + buildParam;
		
		URI uri = URI.create(UriComponentsBuilder.fromUriString(getAPIUrl(url))
				.build().encode().toString());

		Object response = null;
		try {
			response = httpRequestUtil.postInternalRequest(uri, Object.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private EventRequestDto constructEventRequest(
			SubscriberName subscriberName, List<String> listEventTypes,
			Pageable pageable) {
		return new EventRequestDto(subscriberName, listEventTypes, pageable);
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
		return PropertyReader
				.getRequiredPropertyAsString(PropertyKeys.PROPTIGER_URL) + url;
	}

    public LandMark getLandMarkById(Integer landMarkId) {
		String url = PropertyReader.getRequiredPropertyAsString(PropertyKeys.LANDMARK_API_URL);
		url = url.replace(URLSEOGenerationConstants.idURLConstant,Integer.toString(landMarkId)) 
				+ URLSEOGenerationConstants.Selector
				+ URLSEOGenerationConstants.SelectorGetLandMarkById;

		URI uri = URI.create(UriComponentsBuilder.fromUriString(getAPIUrl(url))
				.build().encode().toString());
		LandMark landMark = null;
		try {
			landMark = httpRequestUtil.getInternalApiResultAsTypeFromCache(uri,LandMark.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return landMark;
	}
}
