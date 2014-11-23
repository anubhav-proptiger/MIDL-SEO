package com.proptiger.seo.model;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.gson.Gson;
import com.proptiger.core.model.BaseModel;

@Entity
@Table(name = "seo_footer")
@JsonInclude(Include.NON_NULL)
public class SeoFooter extends BaseModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "url")
    @JsonIgnore
    @Id
    String                    url;

    @Column(name = "footer_json")
    @JsonIgnore
    String                    footerJson;

    @Column(name = "page_type")
    @JsonIgnore
    String                    pageType;

    @Transient
    @JsonUnwrapped
    Map<String, Object>       footerUrls;

    @PostLoad
    public void setJsonPreference() {
        if (this.footerJson != null) {
            this.footerUrls = new Gson().fromJson(this.footerJson, Map.class);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFooterJson() {
        return footerJson;
    }

    public void setFooterJson(String footerJson) {
        this.footerJson = footerJson;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public Map<String, Object> getFooterUrls() {
        return footerUrls;
    }

    public void setFooterUrls(Map<String, Object> footerUrls) {
        this.footerUrls = footerUrls;
    }
}
