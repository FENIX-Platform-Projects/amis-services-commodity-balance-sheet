package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.configurations.dataCreator;

import java.util.ArrayList;

public class DataCreator {

    private ArrayList<Object[]> data ;

    private String datasource, season, country;

    public DataCreator( ArrayList<Object[]> dataTrue, String season, String dataSource, String region) {
        this.data = dataTrue;
        this.datasource = dataSource;
        this.season = season;
        this.country = region;
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