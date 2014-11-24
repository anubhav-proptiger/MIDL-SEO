package com.proptiger.seo.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.proptiger.seo.model.SeoPage;

@Repository
public interface SeoPageDao extends PagingAndSortingRepository<SeoPage, String> {

}
