package com.proptiger.seo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.proptiger.core.model.BaseModel;
import com.proptiger.core.model.proptiger.ObjectType;

@Entity
@Table(name = "seodb.url_categories")
public class URLCategories extends BaseModel{
    
    /**
     * 
     */
    private static final long serialVersionUID = -3800420699039778074L;

    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;
    
    @Column(name="name")
    private String name;
    
    @Column(name="column_call")
    private String contentCall;
    
    @OneToOne
    @JoinColumn(name="object_type_id", insertable = false, updatable = false)
    private ObjectType objectType;
    
    @Column(name="redirection_parent_url_category_id")
    private int redirectParentUrlCategoryId;
    
    @OneToOne
    @JoinColumn(name="url_property_type_id", nullable = true)
    private URLPropertyTypes urlPropertyTypes;

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

    public String getContentCall() {
        return contentCall;
    }

    public void setContentCall(String contentCall) {
        this.contentCall = contentCall;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public int getRedirectParentUrlCategoryId() {
        return redirectParentUrlCategoryId;
    }

    public void setRedirectParentUrlCategoryId(int redirectParentUrlCategoryId) {
        this.redirectParentUrlCategoryId = redirectParentUrlCategoryId;
    }

    public URLPropertyTypes getUrlPropertyTypes() {
        return urlPropertyTypes;
    }

    public void setUrlPropertyTypes(URLPropertyTypes urlPropertyTypes) {
        this.urlPropertyTypes = urlPropertyTypes;
    }
   
}
