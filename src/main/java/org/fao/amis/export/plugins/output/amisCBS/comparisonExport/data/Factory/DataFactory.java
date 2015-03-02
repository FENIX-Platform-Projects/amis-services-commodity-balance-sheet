package org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.Factory;


import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.configurations.dataCreator.DataCreator;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.forecast.Forecast;
import org.fao.amis.export.plugins.output.amisCBS.comparisonExport.data.query.AMISQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;


public class DataFactory {

    private DataCreator fakeConstructor;

    private AMISQuery qvo;

    private Forecast forecasts;

    public DataFactory( ArrayList<Object[]> data, LinkedHashMap filterData){
        try {

            this.fakeConstructor = new DataCreator(data, filterData);

            qvo = new AMISQuery();
            qvo.init();

            forecasts = new Forecast();
            forecasts.initData(this.fakeConstructor.getData());

            forecasts.createOrderedMaps(qvo.getFoodBalanceElements(), "national");
            forecasts.createOrderedMaps(qvo.getItyElements(), "international");
            forecasts.createOrderedMaps(qvo.getOtherElements(), "others");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public DataCreator getDataCreatorIstance(){
        return  this.fakeConstructor;
    }


    public AMISQuery getAMISQueryIstance() {
        return this.qvo;
    }


    public Forecast getForecastIstance() {
        return this.forecasts;
    }

}
