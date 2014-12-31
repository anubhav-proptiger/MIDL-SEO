package com.proptiger.seo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.proptiger.core.model.BaseModel;

@Entity
@Table(name = "seodb.seo_urls")
public class SeoURLs extends BaseModel {
    public SeoURLs() {
        super();
    }

    public enum URLStatus {
        Active, Deleted
    }
    public enum URLInfo{
        New, ContentChange, ReActive
    }
    /**
     * 
     */
    private static final long serialVersionUID = 6963742371023038712L;
    
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    
    @Column(name = "url")
    private String url;
    
    @OneToOne
    @JoinColumn(name = "url_category_id", insertable = false, updatable = false)
    private URLCategories urlCategories;
    
    @Column(name = "object_id")
    private Integer objectId;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private URLStatus urlStatus;
    
    @Column(name = "number_of_results")
    private Integer numberOfResults;
    
    @Column(name = "url_info")
    private URLInfo urlInfo;
    
    @Column(name = "created_at")
    private Date createdAt;
    
    @Column(name = "updated_at")
    private Date updatedAt;
    
    @PreUpdate
    public void populateUpdateFields(){
        this.updatedAt = new Date();
    }
    
    public SeoURLs(String url, URLCategories urlCategories, Integer objectId) {
        super();
        this.url = url;
        this.urlCategories = urlCategories;
        this.objectId = objectId;
    }

    @PrePersist
    public void populateInsertFields(){
        populateUpdateFields();
        this.createdAt = new Date();
        this.numberOfResults = 0;
        this.urlInfo = URLInfo.New;
        this.urlStatus = URLStatus.Active;
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public URLCategories getUrlCategories() {
        return urlCategories;
    }

    public void setUrlCategories(URLCategories urlCategories) {
        this.urlCategories = urlCategories;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getNumberOfResults() {
        return numberOfResults;
    }

    public void setNumberOfResults(Integer numberOfResults) {
        this.numberOfResults = numberOfResults;
    }

    public URLInfo getUrlInfo() {
        return urlInfo;
    }

    public void setUrlInfo(URLInfo urlInfo) {
        this.urlInfo = urlInfo;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public URLStatus getUrlStatus() {
        return urlStatus;
    }

    public void setUrlStatus(URLStatus urlStatus) {
        this.urlStatus = urlStatus;
    }
    

}
