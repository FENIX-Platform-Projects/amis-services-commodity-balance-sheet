package org.fao.amis.dataset.dto;

public class DatasetUpdate
{
    private DatasetFilterWithSeason filter;
    private Object[][] data;

    public DatasetFilterWithSeason getFilter()
    {
        return this.filter;
    }

    public void setFilter(DatasetFilterWithSeason filter) {
        this.filter = filter;
    }

    public Object[][] getData() {
        return this.data;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }
}