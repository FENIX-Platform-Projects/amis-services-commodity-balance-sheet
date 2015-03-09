package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.natMarkBean;


public class NationalMarketingBean {

    private String nmyMonths;
    private String ityMonths;

    private String nmyStartingYear;
    private String nmyEndingYear;

    private String ityStartingYear;
    private String ityEndingYear;

    private HarvestBean harvestBean;

    public NationalMarketingBean(String nmyMonths, String nmyStartingYear, String nmyEndingYear,String ityMonths,String ityStartingYear, String ityEndingYear, HarvestBean harvestBean) {
        this.nmyMonths = nmyMonths;
        this.ityMonths = ityMonths;
        this.nmyStartingYear = nmyStartingYear;
        this.nmyEndingYear = nmyEndingYear;
        this.ityStartingYear = ityStartingYear;
        this.ityEndingYear = ityEndingYear;
        this.harvestBean = harvestBean;
    }

    public String getNmyMonths() {
        return nmyMonths;
    }

    public void setNmyMonths(String nmyMonths) {
        this.nmyMonths = nmyMonths;
    }

    public String getItyMonths() {
        return ityMonths;
    }

    public void setItyMonths(String ityMonths) {
        this.ityMonths = ityMonths;
    }

    public String getNmyStartingYear() {
        return nmyStartingYear;
    }

    public void setNmyStartingYear(String nmyStartingYear) {
        this.nmyStartingYear = nmyStartingYear;
    }

    public String getNmyEndingYear() {
        return nmyEndingYear;
    }

    public void setNmyEndingYear(String nmyEndingYear) {
        this.nmyEndingYear = nmyEndingYear;
    }

    public String getItyStartingYear() {
        return ityStartingYear;
    }

    public void setItyStartingYear(String ityStartingYear) {
        this.ityStartingYear = ityStartingYear;
    }

    public String getItyEndingYear() {
        return ityEndingYear;
    }

    public void setItyEndingYear(String ityEndingYear) {
        this.ityEndingYear = ityEndingYear;
    }

    public HarvestBean getHarvestBean() {
        return harvestBean;
    }

    public void setHarvestBean(HarvestBean harvestBean) {
        this.harvestBean = harvestBean;
    }
}
