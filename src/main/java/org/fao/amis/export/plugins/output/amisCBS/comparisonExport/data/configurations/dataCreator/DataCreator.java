package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.configurations.dataCreator;

import org.fao.amis.dataset.dto.MostRecentDateForProductResult;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.utils.commodity.CommodityParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DataCreator {

    private ArrayList<ArrayList<Object>> data ;

    private String datasource, season, country;

    private HashMap<String,String> mapProductDate;

    private CommodityParser commodityParser;


    public DataCreator( ArrayList<ArrayList<Object>> dataTrue, LinkedHashMap filterData) {
        commodityParser = new CommodityParser();
        this.data = dataTrue;
        this.datasource = filterData.get("datasource").toString();
        this.season = filterData.get("season").toString();
        this.country = filterData.get("region").toString();
        fillMapProductDate((ArrayList<Object>)filterData.get("mostRecentDateByProducts"));
/*
        System.out.println(filterData.get("mostRecentDateByProducts")))
*/

    }

    private void fillMapProductDate(ArrayList<Object> list) {
        this.mapProductDate = new HashMap<String,String>();
        for(int i=0; i<list.size(); i++) {
            String key =commodityParser.getCommodityLabel(((LinkedHashMap) list.get(i)).get("productCode").toString());
            mapProductDate.put(key, ((LinkedHashMap) list.get(i)).get("date").toString());
        }

    }

    public ArrayList<ArrayList<Object>> getData() {
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

    public HashMap<String, String> getMapProductDate() {
        return mapProductDate;
    }



}