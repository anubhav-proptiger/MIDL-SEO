/**
 * 
 */
package com.proptiger.seo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.proptiger.core.model.BaseModel;

/**
 * @author tech
 * 
 */
@Entity
@Table(name = "PROJECT_SEO_TAGS")
@JsonInclude(Include.NON_NULL)
public class ProjectSeoTags extends BaseModel {

    /**
     * 
     */
    private static final long serialVersionUID = -7964883966099859194L;
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer           id;

    @Column(name = "URL")
    private String            url;

    @Column(name = "TITLE")
    private String            title;

    @Column(name = "DESC")
    private String            description;

    @Column(name = "KEYWORD")
    private String            keywords;

    @Column(name = "CONTENT")
    private String            content;

    @Column(name = "H1")
    private String            h1;

    @Column(name = "H2")
    private String            h2;

    @Transient
    private String            h3;

    @Transient
    private String            h4;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getH1() {
        return h1;
    }

    public void setH1(String h1) {
        this.h1 = h1;
    }

    public String getH2() {
        return h2;
    }

    public void setH2(String h2) {
        this.h2 = h2;
    }
    
    public String getH3() {
        return h3;
    }

    public void setH3(String h3) {
        this.h3 = h3;
    }

    public String getH4() {
        return h4;
    }

    public void setH4(String h4) {
        this.h4 = h4;
    }
}
