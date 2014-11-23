package com.proptiger.seo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.proptiger.core.model.BaseModel;

@Entity
@Table(name = "REDIRECT_URL_MAP")
@JsonInclude(Include.NON_NULL)
@Deprecated
public class RedirectUrlMap extends BaseModel {

    /**
     * 
     */
    private static final long serialVersionUID = 5925132433438526816L;

    @Id
    @Column(name = "FROM_URL")
    private String            fromUrl;

    @Column(name = "TO_URL")
    private String            toUrl;

    public String getFromUrl() {
        return fromUrl;
    }

    public void setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl;
    }

    public String getToUrl() {
        return toUrl;
    }

    public void setToUrl(String toUrl) {
        this.toUrl = toUrl;
    }
}
