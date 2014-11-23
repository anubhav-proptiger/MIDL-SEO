package com.proptiger.seo.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.proptiger.data.model.ProjectSeoTags;

public interface ProjectSeoTagsDao extends PagingAndSortingRepository<ProjectSeoTags, Integer>{
    public List<ProjectSeoTags> findByUrlOrderByIdDesc(String url, Pageable pageable);
}
