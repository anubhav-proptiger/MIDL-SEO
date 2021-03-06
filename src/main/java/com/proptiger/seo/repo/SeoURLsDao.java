package com.proptiger.seo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.proptiger.seo.model.SeoURLs;

public interface SeoURLsDao extends JpaRepository<SeoURLs, Integer>{
	
	@Modifying
    @Query(value = "INSERT INTO seodb.seo_urls (id, url, url_category_id, object_id, status, number_of_results, url_info, created_at, updated_at) "
            + " VALUES(0, ?1, ?2, ?3, ?4, 0, ?5, now(), now()) ON DUPLICATE KEY UPDATE status = ?4, url_info=?6, updated_at = now()", nativeQuery = true)
    public int insertQuery(String url, int url_category_id, int object_id, String status, String insertUrlInfo, String updateUrlInfo);
}
