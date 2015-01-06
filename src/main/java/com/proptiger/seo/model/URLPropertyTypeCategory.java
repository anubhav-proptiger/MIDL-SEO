package com.proptiger.seo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.proptiger.core.model.BaseModel;
import com.proptiger.seo.enums.URLTypeCategories;

@Entity
@Table(name = "seodb.url_property_type_category")
public class URLPropertyTypeCategory extends BaseModel {

    /**
     * 
     */
    private static final long serialVersionUID = -4944127996199684303L;
    
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    
    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private URLTypeCategories urlTypeCategories;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public URLTypeCategories getUrlTypeCategories() {
        return urlTypeCategories;
    }

    public void setUrlTypeCategories(URLTypeCategories urlTypeCategories) {
        this.urlTypeCategories = urlTypeCategories;
    }
}
