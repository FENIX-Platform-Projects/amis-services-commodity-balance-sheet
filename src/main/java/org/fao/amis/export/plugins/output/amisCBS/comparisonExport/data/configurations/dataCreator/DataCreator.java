package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.configurations.dataCreator;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DataCreator {

    private ArrayList<Object[]> data ;

    private String datasource, season, country;

    public DataCreator( ArrayList<Object[]> dataTrue, LinkedHashMap filterData) {
        this.data = dataTrue;
        this.datasource = filterData.get("datasource").toString();
        this.season = filterData.get("season").toString();
        this.country = filterData.get("region").toString();
    }

    public ArrayList<Object[]> getData() {
        return this.data;
    }

    public String getDatasource() {
        return datasource;
    }

    public String getSeason() {
        return season;
    }

    public String getCountry() {
        return country;
    }
}