package com.proptiger.seo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proptiger.core.model.event.EventGenerated;
import com.proptiger.seo.interceptor.ResponseInterceptor;
import com.proptiger.seo.model.SeoURLs;
import com.proptiger.seo.model.SeoURLs.URLInfo;
import com.proptiger.seo.model.SeoURLs.URLStatus;
import com.proptiger.seo.model.URLCategories;
import com.proptiger.seo.repo.SeoURLsDao;

@Service
public class SeoURLService {
	@Autowired
	private SeoURLsDao seoURLsDao;

	@Autowired
	private ResponseInterceptor responseInterceptor;

	public int saveUrls(String url, int objectId,
			URLCategories urlCategories) {
		int returnStatus = seoURLsDao.insertQuery(url, urlCategories.getId(),
				objectId, URLStatus.Active.name(), URLInfo.New.name(),
				URLInfo.ReActive.name());

		return returnStatus;
	}

	public SeoURLs createSeoURLObject(String url, int objectId,
			URLCategories urlCategories) {
		return new SeoURLs(url, urlCategories, objectId);
	}

	@Transactional
	public void saveDomainUrls(List<SeoURLs> listSeoURLs,
			EventGenerated eventGenerated) {
		for (SeoURLs seoURLs : listSeoURLs) {
			saveUrls(seoURLs.getUrl(), seoURLs.getObjectId(),
					seoURLs.getUrlCategories());
		}
		responseInterceptor.postSubscriberLastEvent(eventGenerated
				.getSubscriberMapping().get(0).getSubscriber()
				.getSubscriberName(), eventGenerated.getId());
	}
}
