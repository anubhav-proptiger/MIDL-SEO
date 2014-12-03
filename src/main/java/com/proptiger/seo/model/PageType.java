/**
 * 
 */
package com.proptiger.seo.model;

/**
 * 
 *
 */
public enum PageType {
    /*
     * URL should match the specification, then only the regex will work
     * correctly
     */
    HOME_PAGE("^$", new String[]{}),
    IMAGE_PAGE_URL("^gallery/(?:[\\w]+(?:-[\\w]+)*)-(\\d+)-(\\d+)$", new String[] {"objectId", "imageId"}),
    CITY_URLS("^([\\w]+)(?:(((/(apartments-flats-sale|property-sale|house-sale|villas-sale|sites-plots-sale))|-real-estate|-real-estate/\\1-sitemap.php)(/[\\d]{1,2}bhk)?(/\\d+-\\d+-lacs)?)|(-real-estate-overview|/schools|/restaurants|/banks|/atms|/hospitals|/petrol-pumps)|/(resale-property|ready-to-move-property|under-construction-property|upcoming-property|upcoming-projects|luxury-projects|low-budget-flats|affordable-flats|new-projects-for-sale|upcoming-flats-for-sale|ready-to-move-flats|resale-apartments|new-apartments-for-sale|all-builders|all-localities))$", new String[] { "cityName" }),
    LOCALITY_SUBURB_LISTING_SEO("^(?:[\\w]+)/(society-flats-in|resale-property-in|ready-to-move-property-in|under-construction-property-in|upcoming-property-in|upcoming-Projects-in|luxury-projects-in|low-budget-flats-in|affordable-flats-in|new-projects-for-sale-in|residential-property-for-sale-in|upcoming-flats-for-sale-in|ready-to-move-flats-in|resale-apartments-in|new-apartments-for-sale-in)-(?:[\\-\\w]+)-(\\d{5,5})$", new String[] { "propertyType", "localityId"}),
    LOCALITY_SUBURB_LISTING("^(?:[\\w]+)/(apartments-flats-sale|property-sale|house-sale|villas-sale|sites-plots-sale)-(?:[\\-\\w]+)-(\\d{5,5})(/[\\d]{1,2}bhk)?(/\\d+-\\d+-lacs)?$", new String[] { "propertyType", "localityId", "bedroomString", "priceString" }),
    /**
     * Suburb does not support amenities URL.
     */
    LOCALITY_SUBURB_LANDMARK("(?:[\\w]+)/(?:[\\-\\w]+)-([5-9]\\d{4,4})(/schools|/restaurants|/banks|/atms|/hospitals|/petrol-pumps)", new String[] { "localityId", "appendingString"}), 
    LOCALITY_SUBURB_OVERVIEW("^(?:[\\-\\w]+)/(?:[\\-\\w]+)-(overview)-(\\d{5,5})$", new String[] { "overviewType", "localityId" }),
    PROJECT_URLS("^(?:.*)([5-9]\\d{5})$", new String[] { "projectId" }),
    PROPERTY_URLS("^(?:.*)([5-9]\\d{6,6})/(?:[\\d]{0,2}bhk)$", new String[] {"propertyId"}),
    BUILDER_URLS_SEO("^([\\w]+/)?(under-construction-by-|apartments-by-|villas-by-|plots-by-|resale-property-by-|ready-to-move-property-by-|ready-to-move-apartments-by-|resale-Flats-by-|under-construction-property-by-|upcoming-property-by-|new-launch-project-by-|completed-property-by-|upcoming-project-by-|new-flats-by|new-project-by-|luxury-projects-by-|low-budget-flats-by-|affordable-flats-by-)(?:[\\-\\w]+)-(1\\d{5})$", new String[] {"cityName", "propertyType", "builderId"}),
    BUILDER_URLS("^([\\w]+/)?(?:[\\-\\w]+)-(1\\d{5})(/[\\d]{1,2}bhk)?$", new String[] { "cityName", "builderId", "bedroomString" }), 
    STATIC_URLS("^(?:all-builders|google_page_12.php|google_page_13.php|google_page_14.php|google_page_15.php|google_page_4.php|google_page_8.php|all-cities|compare|all-projects|news|contactus|aboutus|nri|vaastu|homeloan|documents|faqs|builderpartner|ourservices|management-team|proptiger-media|privacy-policy|user-agreement|careers|sitemap.php|testimonials|emi|disclaimer|404|server-error|sites-plots-sale|projects-in-india|apartments-in-india|flats-in-india|property-in-india|new-launch-projects-in-india|ongoing-projects-in-india|completed-projects-in-india|new-projects-for-sale-in-india|upcoming-flats-for-sale-in-india|ready-to-move-flats-in-india|resale-apartments-in-india|new-apartments-for-sale-in-india|((apartments-flats-sale|property-sale|villas-sale)(/[\\d]{1,2}bhk)?))|mobile-apps$", new String[]{}),
    PORTFOLIO_URLS("^portfolio/(?:index|dashboard|enquiredproperty|savedsearches|recentlyviewed|myfavorites|property/([\\d]+))$", new String[]{"portfolioId"}),
    NEWS_URLS("^(?:news|blog)(?:/category/([\\w]+)|(?:/category/(?:real-estate|home-loan|real-estate-news|real-estate-tips|interior-decorating-design|home-loans))|/(?:[\\w]+(?:\\-[\\w]+)*)|/(?:\\?p=(\\d+)))?/?$",new String[] {"cityName"}),
    DIWALI_MELA_URL("^(mega-property-sale|transaction|google_gosf)(.*)?$", new String[]{}),
    InvalidUrl("(.*)", new String[]{"URL"});

    private String   regex;
    private String[] URLDetailFields;

    private PageType(String regex, String[] URLDetailFields) {
        this.regex = regex;
        this.URLDetailFields = URLDetailFields;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String[] getURLDetailFields() {
        return URLDetailFields;
    }

    public void setURLDetailFields(String[] uRLDetailFields) {
        URLDetailFields = uRLDetailFields;
    }
}
