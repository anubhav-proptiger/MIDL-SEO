package com.proptiger.seo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.proptiger.seo.model.URLCategories;

public interface URLCategoriesDao extends PagingAndSortingRepository<URLCategories, Integer>{
    
    @Query("select UC from URLCategories UC JOIN FETCH UC.objectType JOIN Fetch UC.urlPropertyTypes UPT JOIN Fetch UPT.urlPropertyTypeCategory")
    List<URLCategories> getAllUrlCategories();
}
