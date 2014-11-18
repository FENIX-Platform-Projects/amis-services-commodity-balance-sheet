package org.fao.amis.dataset.dto;

public class DatasetUpdatePrevSeason
{
    private DatasetFilterComplete filter;
    private Object[][] data;

    public DatasetFilterComplete getFilter()
    {
        return this.filter;
    }

    public void setFilter(DatasetFilterComplete filter) {
        this.filter = filter;
    }

    public Object[][] getData() {
        return this.data;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }
}