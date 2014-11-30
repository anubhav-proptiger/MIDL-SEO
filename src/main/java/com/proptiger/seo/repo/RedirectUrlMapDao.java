package com.proptiger.seo.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.proptiger.seo.model.RedirectUrlMap;

@Deprecated
public interface RedirectUrlMapDao extends PagingAndSortingRepository<RedirectUrlMap, String> {

}
