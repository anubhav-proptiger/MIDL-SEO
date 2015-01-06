package com.proptiger.seo.enums;

public enum PropertyType {
    Apartment("apartments-flats"), Flats("apartments-flats"), Site("sites-plots"), Plot("sites-plots"), Villa("villas"), Property("property");
    
    String urlAlias;
    PropertyType(String value){
        this.urlAlias = value;
    }
    public String getUrlAlias() {
        return urlAlias;
    }
    public void setUrlAlias(String urlAlias) {
        this.urlAlias = urlAlias;
    }
}
