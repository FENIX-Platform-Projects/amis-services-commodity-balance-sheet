package org.fao.amis.dataset.dto;

public class DatasetNationalFilter
{
    private Integer region;
    private Integer element;
    private Integer year;

    public Integer getRegion()
    {
        return this.region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    public Integer getElement() {
        return this.element;
    }

    public void setElement(Integer element) {
        this.element = element;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}