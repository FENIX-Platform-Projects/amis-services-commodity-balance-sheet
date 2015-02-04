package org.fao.amis.dataset.dto;


public class PopulationDataset {

    private PopulationFilter filter;
    private Object[][] data;


    public Object[][] getData() {
        return data;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }

    public PopulationFilter getFilter() {
        return filter;
    }

    public void setFilter(PopulationFilter filter) {
        this.filter = filter;
    }
}
