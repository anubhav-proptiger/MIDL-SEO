package com.proptiger.seo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.proptiger.seo.model.URLCategories;

public interface URLCategoriesDao extends PagingAndSortingRepository<URLCategories, Integer>{
    
    @Query("select uc from URLCategories JOIN FETCH ObjectType JOIN Fetch URLPropertyType JOIN Fetch URLPropertyTypeCategory")
    List<URLCategories> getAllUrlCategories();
}
