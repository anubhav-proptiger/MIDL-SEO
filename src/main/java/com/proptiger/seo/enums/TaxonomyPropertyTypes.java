package com.proptiger.seo.enums;

public enum TaxonomyPropertyTypes {
    ResaleProperty("resale-property"), ReadyToMoveInProperty("ready-to-move-property"), UnderConstructionProperty("under-construction-property"), UpcomingProjects("upcoming-projects"),
    UpcomingProperty("upcoming-property"), luxuryProjects("luxury-projects"), lowBudgetFlats("low-budget-flats"), affordableFlats("affordable-flats"), newProjects("new-projects"), 
    societyFlats("society-flats"), upcomingFlatsForSale("upcoming-flats-for-sale"), newProjectsForSale("new-projects-for-sale"), UpcomingFlats("upcoming-flats"), ReadyToMoveFlats("ready-to-move-flats"),
    ResaleApartments("resale-apartments"), newApartments("new-apartments"), newApartmentsForSale("new-apartments-for-sale"), residentialProperty("residential-property");
    
    String urlAlias;

    private TaxonomyPropertyTypes(String urlAlias) {
        this.urlAlias = urlAlias;
        // TODO Auto-generated constructor stub
    }

    public String getUrlAlias() {
        return urlAlias;
    }

    public void setUrlAlias(String urlAlias) {
        this.urlAlias = urlAlias;
    }
}
