package org.fao.amis.dataset.dto;


public class DatasetAnnualForecast {

    private Integer region;
    private Integer product;
    private String datasource;

    private AnnualFilter[] filters;

    public Integer getRegion() {
        return region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    public Integer getProduct() {
        return product;
    }

    public void setProduct(Integer product) {
        this.product = product;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public AnnualFilter[] getFilters() {
        return filters;
    }

    public void setFilters(AnnualFilter[] filters) {
        this.filters = filters;
    }
}
