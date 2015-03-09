package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.natMarkBean;


public class HarvestBean {

    private String beginningOfHarvest;
    private String endOfHarvest;
    private String beginningHarvestYear;
    private String endHarvestYear;

    public HarvestBean(){}

    public HarvestBean(String beginningOfHarvest,String beginningHarvestYear, String endOfHarvest,  String endHarvestYear) {
        this.beginningOfHarvest = beginningOfHarvest;
        this.endOfHarvest = endOfHarvest;
        this.beginningHarvestYear = beginningHarvestYear;
        this.endHarvestYear = endHarvestYear;
    }

    public String getBeginningOfHarvest() {
        return beginningOfHarvest;
    }

    public void setBeginningOfHarvest(String beginningOfHarvest) {
        this.beginningOfHarvest = beginningOfHarvest;
    }

    public String getEndOfHarvest() {
        return endOfHarvest;
    }

    public void setEndOfHarvest(String endOfHarvest) {
        this.endOfHarvest = endOfHarvest;
    }

    public String getBeginningHarvestYear() {
        return beginningHarvestYear;
    }

    public void setBeginningHarvestYear(String beginningHarvestYear) {
        this.beginningHarvestYear = beginningHarvestYear;
    }

    public String getEndHarvestYear() {
        return endHarvestYear;
    }

    public void setEndHarvestYear(String endHarvestYear) {
        this.endHarvestYear = endHarvestYear;
    }
}
