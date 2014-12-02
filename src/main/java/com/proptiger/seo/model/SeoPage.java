package com.proptiger.seo.model;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.Gson;
import com.proptiger.core.model.BaseModel;

@Entity
@Table(name = "seo_meta_content_templates")
@JsonInclude(Include.NON_NULL)
public class SeoPage extends BaseModel {
    /**
     * 
     */
    private static final long serialVersionUID = 6065883078087404621L;

    // @TODO get reference to the field Name like getting reference to class.
    /**
     * @Note The token should be in lowercase.
     * @author mukand
     * TODO scope of improvements in the enums. (The field selection heirarchy should be field1.field2.field3 ... instead of field1, field2).
     *       The enum value should contain the fields for adding prefix or suffix for token replacement.
     */
    public enum Tokens {
        /*
         * ENUM_NAME(token name, field1, field2)
         * They will be mapped with CompositeSeoTokenData class. 
         * field1 corresponds to the field name in the above mentioned class.
         * field2 corresponds to the field name in the field1 class. if field1 is null then in above mentioned class.
         */
        Locality("<locality>", "locality", "label", "%s"), City("<city>", "city", "label", "%s"), Suburb("<suburb>",
                "suburb", "label", "%s"), BuiderName("<builder name>", "builder", "name", "%s"), ProjectName(
                "<project name>", "project", "name", "%s"), UnitName("<unit name>", "property", "unitName", "%s"), BHK(
                "<bhk>", null, "bedroomsStr", "%s BHK"), PriceRange("<price range>", null, "priceRangeStr", "%s Lacs"), Bathrooms(
                "<t>", null, "bathrooms", "%dT"), Sqft("<sq ft>", null, "size", "%d sq ft"), CityLatitude(
                "<city latitude>", "city", "centerLatitude", "%f"), CityLongitude("<city longitude>", "city",
                "centerLongitude", "%f"), ProjectLatitude("<project latitude>", "project", "latitude", "%f"), ProjectLongitude(
                "<project longitude>", "project", "longitude", "%f"), ServerName("<server name>", null, "serverName",
                "%s"), Url("<url>", null, "url", "%s"), ProjectImageUrl("<project image url>", "project", "imageURL",
                "%s"), Page("<page>", null, "page", "Page %d"), ImageType("<image type>", null, "imageType", "%s");

        private String value;
        private String fieldName1;
        private String fieldName2;
        private String replaceString;

        Tokens(String value, String className, String fieldName, String replaceString) {
            this.value = value;
            this.fieldName1 = className;
            this.fieldName2 = fieldName;
            this.replaceString = replaceString;
        }

        public String getToken(Tokens token) {
            return token.value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getFieldName1() {
            return fieldName1;
        }

        public String getFieldName2() {
            return fieldName2;
        }

        public String getReplaceString() {
            return replaceString;
        }

        public void setReplaceString(String replaceString) {
            this.replaceString = replaceString;
        }
    }

    @Column(name = "template_name")
    @JsonIgnore
    @Id
    private String templateId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "keywords")
    private String keywords;

    @Column(name = "h1")
    private String h1;

    @Column(name = "h2")
    private String h2;

    @Column(name = "h3")
    private String h3;

    @Column(name = "h4")
    private String h4;
    
    @Column(name = "others")
    @JsonIgnore
    private String others;

    @Transient
    @JsonIgnore
    private Map<String, String> otherParams;
    
    @PostLoad
    public void setOtherParamsMap() {
        if (this.others != null) {
            this.otherParams = new Gson().fromJson(this.others, Map.class);
        }
    }
    
    public void setOtherParams(Map<String, String> otherParams) {
        this.otherParams = otherParams;
    }
    
    @JsonAnyGetter
    public Map<String, String> getOtherParams() {
        return otherParams;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
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

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}
