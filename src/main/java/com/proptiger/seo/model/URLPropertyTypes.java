package com.proptiger.seo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proptiger.core.model.BaseModel;

@Entity
@Table(name = "seodb.url_property_types")
public class URLPropertyTypes extends BaseModel {

    /**
     * 
     */
    private static final long serialVersionUID = 564958605531781686L;
    
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "url_sub_part")
    private String urlSubPart;
    
    @OneToOne
    @JoinColumn(name = "url_property_category_id")
    @JsonIgnore
    private URLPropertyTypeCategory urlPropertyTypeCategory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlSubPart() {
        return urlSubPart;
    }

    public void setUrlSubPart(String urlSubPart) {
        this.urlSubPart = urlSubPart;
    }

    public URLPropertyTypeCategory getUrlPropertyTypeCategory() {
        return urlPropertyTypeCategory;
    }

    public void setUrlPropertyTypeCategory(URLPropertyTypeCategory urlPropertyTypeCategory) {
        this.urlPropertyTypeCategory = urlPropertyTypeCategory;
    }

}
