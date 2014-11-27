package org.fao.amis.dataset.dto;

public class DatasetFilterWithSeason
{
    private Integer region;
    private Integer product;
    private Integer year;
    private String season;
    private String datasource;

    public Integer getRegion()
    {
        return this.region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    public Integer getProduct() {
        return this.product;
    }

    public void setProduct(Integer product) {
        this.product = product;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getSeason() {
        return this.season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }
}