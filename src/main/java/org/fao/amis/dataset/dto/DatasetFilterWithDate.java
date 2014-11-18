package org.fao.amis.dataset.dto;

public class DatasetFilterWithDate
{
    private Integer region;
    private Integer product;
    private Integer year;
    private String date;

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

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}