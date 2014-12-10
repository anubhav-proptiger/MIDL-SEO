package com.proptiger.seo.enums;

public enum BuilderPropertyTypes {
    Apartments("apartments"), Villas("villas"), Plots("plots"), ResaleProperty("resale-property"), ReadyToMoveProperty("ready-to-move-property"), 
    ReadyToMoveApartments("ready-to-move-apartments"), ResaleFlats("resale-flats"), UnderConstructionProperty("under-construction-property"), 
    UpcomingProperty("upcoming-property"), NewLaunchProject("new-launch-project"), CompletedProperty("completed-property"), UpcomingProject("upcoming-project"),
    NewFlats("new-flats"), NewProject("new-project"), LuxuryProjects("luxury-projects"), LowBudgetFlats("low-budget-flats"), AffordableFlats("affordable-flats"),
    OngoingProject("ongoing-project");
    
    String urlAlias;

    private BuilderPropertyTypes(String urlAlias) {
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
